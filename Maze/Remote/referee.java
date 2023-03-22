package Remote;

import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.State.PlayerGameState;
import Players.IPlayer;
import Serialization.ActionAdapter;
import Serialization.ChoiceAdapter;
import Serialization.CoordinateAdapter;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;

/**
 * Implementation of a ProxyReferee that calls player methods when prompted
 */
public class referee {

    Socket socket;

    IPlayer player;

    private static final Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder();
        StateAdapter.register(builder);
        CoordinateAdapter.register(builder);
        ActionAdapter.register(builder);
        ChoiceAdapter.register(builder);
        gson = builder.create();
    }

    public referee(Socket socket, IPlayer player) {
        this.socket = socket;
        this.player = player;
    }

    /**
     * Runs the entire game from the perspective of a client, listening for messages from the server.
     * @throws Exception if there is a network problem
     */
    public void runEntireGame() throws Exception {
        JsonWriter remoteWriter = new JsonWriter(new OutputStreamWriter(socket.getOutputStream()));
        JsonReader remoteReader = new JsonReader(new InputStreamReader(this.socket.getInputStream()));
        remoteReader.setLenient(true);
        remoteWriter.setLenient(true);

        while(remoteReader.hasNext()){
            remoteReader.beginArray();

            if (!remoteReader.peek().equals(JsonToken.STRING)) {
                throw new IllegalArgumentException("Next input must be a String");
            }
            String methodName = remoteReader.nextString();

            remoteReader.beginArray();

            if (methodName.equals("setup")) {
                callSetUp(remoteReader, remoteWriter);

            } else if (methodName.equals("take-turn")) {
                callTakeTurn(remoteReader, remoteWriter);

            } else if (methodName.equals("win")) {
                callWin(remoteReader, remoteWriter);
            } else {
                throw new IllegalArgumentException("Unexpected method call given: " + methodName);
            }

            remoteReader.endArray();
            remoteReader.endArray();
            remoteWriter.flush();
        }
        remoteWriter.close();
        remoteReader.close();
    }

    /**
     * Helper when calling the win method.
     * @param remoteReader reads incomming messages
     * @param remoteWriter sends outgoing messages
     * @throws Exception if there is a network problem
     */
    private void callWin(JsonReader remoteReader, JsonWriter remoteWriter) throws Exception {
        boolean bool = remoteReader.nextBoolean();

        player.win(bool);

        remoteWriter.value("void");
    }

    /**
     * Helper when calling the takeTurn method.
     * @param remoteReader reads incomming messages
     * @param remoteWriter sends outgoing messages
     * @throws Exception if there is a network problem
     */
    private void callTakeTurn(JsonReader remoteReader, JsonWriter remoteWriter) throws Exception {
        PlayerGameState state = gson.fromJson(remoteReader, PlayerGameState.class);

        Optional<FullTurnInfo> turnInfo = player.takeTurn(state);
        remoteWriter.jsonValue(gson.toJson(turnInfo, ChoiceAdapter.type));
    }

    /**
     * Helper when calling the setup method.
     * @param remoteReader reads incomming messages
     * @param remoteWriter sends outgoing messages
     * @throws Exception if there is a network problem
     */
    private void callSetUp(JsonReader remoteReader, JsonWriter remoteWriter) throws Exception {
        Optional<PlayerGameState> state;
        if(remoteReader.peek().equals(JsonToken.BOOLEAN)){
            boolean input = remoteReader.nextBoolean();
            if (input) {
                throw new IllegalArgumentException("Value must be false");
            }
            state = Optional.empty();
        }else{
            state = Optional.of(gson.fromJson(remoteReader, PlayerGameState.class));
        }
        Coordinate coord = gson.fromJson(remoteReader, Coordinate.class);

        player.setup(state, coord);

        remoteWriter.value("void");
    }
}
