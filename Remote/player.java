package Remote;

import Common.Board.Board;
import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PartsOfTurn.ShiftInfo;
import Common.State.PlayerGameState;
import Players.IPlayer;
import Serialization.ActionAdapter;
import Serialization.CoordinateAdapter;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;

/**
 * Implementation of a ProxyPlayer that defers all decision-making over the network to the client.
 */
public class player implements IPlayer {

    // Time to wait for player's response
    private static final int RESPONSE_WAIT_MS = 4000;

    private static final Gson gson;
    static {
        GsonBuilder builder = new GsonBuilder();
        StateAdapter.register(builder);
        CoordinateAdapter.register(builder);
        ActionAdapter.register(builder);
        gson = builder.create();
    }

    private final String name;
    private final Socket socket;

    public player(Socket socket, String name) {
        this.name = name;
        this.socket = socket;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Board proposeBoard0(int rows, int columns) throws Exception {
        return null;
    }

    @Override
    public void setup(Optional<PlayerGameState> state0, Coordinate goal) throws Exception {
        setUpOutput(state0, goal);
        Thread.sleep(RESPONSE_WAIT_MS);

        JsonReader readResponse = new JsonReader(new InputStreamReader(this.socket.getInputStream()));
        readResponse.setLenient(true);

        if (!readResponse.peek().equals(JsonToken.STRING)) {
            throw new IllegalArgumentException("Response must be a String");
        }
        String response = readResponse.nextString();

        if (!response.equals("void")) {
            throw new IOException("Player did not properly handle set up call.");
        }
    }

    /**
     * Sends the setUp method call over the connection
     * @param state0 the current state
     * @param goal the goal of the player
     * @throws IOException in the case of network errors
     */
    private void setUpOutput(Optional<PlayerGameState> state0, Coordinate goal) throws IOException {
        JsonWriter jsonWriter = openCommand("setup");

        if(state0.isPresent()){
            jsonWriter.jsonValue(gson.toJson(state0.get()));
        }
        else{
            jsonWriter.value(false);
        }
        jsonWriter.jsonValue(gson.toJson(goal));
        closeCommand(jsonWriter);
    }

    @Override
    public Optional<FullTurnInfo> takeTurn(PlayerGameState state) throws Exception {
        takeTurnOutput(state);

        Thread.sleep(RESPONSE_WAIT_MS);
        JsonReader readResponse = new JsonReader(new InputStreamReader(this.socket.getInputStream()));
        readResponse.setLenient(true);

        if (readResponse.peek().equals(JsonToken.STRING)) {
            String input = readResponse.nextString();
            if (!input.equals("PASS")) {
                throw new IllegalArgumentException("Illegal response.");
            }
            return Optional.empty();
        }
        else {
            FullTurnInfo fullTurnInfo = deserializeFullTurnInfo(readResponse);
            return Optional.of(fullTurnInfo);
        }
    }

    /**
     * Sends the takeTurn method call over the connection.
     * @param state the current state
     * @throws IOException in the case of network errors
     */
    private void takeTurnOutput(PlayerGameState state) throws IOException {
        JsonWriter jsonWriter = openCommand("take-turn");

        jsonWriter.jsonValue(gson.toJson(state));
        closeCommand(jsonWriter);
    }

    @Override
    public String win(boolean b) throws Exception {
        winOutput(b);

        Thread.sleep(RESPONSE_WAIT_MS);

        JsonReader readResponse = new JsonReader(new InputStreamReader(this.socket.getInputStream()));
        readResponse.setLenient(true);

        if (!readResponse.peek().equals(JsonToken.STRING)) {
            throw new IllegalArgumentException("Response must be a String");
        }
        String result = readResponse.nextString();

        if (!result.equals("void")) {
            throw new IOException("Player did not properly handle win call.");
        }

        return result;
    }

    /**
     * Sends the win method call over the connection.
     * @param b a boolean representing if the player won or not
     * @throws IOException in the case of network errors
     */
    private void winOutput(boolean b) throws IOException {
        JsonWriter jsonWriter = openCommand("win");

        jsonWriter.value(b);
        closeCommand(jsonWriter);
    }

    /**
     * Helper for the beginning of network to message calls.
     * @param method the name of the method to call on the player
     * @return a JsonWriter connecting to the client
     * @throws IOException if the connection has an error
     */
    private JsonWriter openCommand(String method) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(socket.getOutputStream()));
        jsonWriter.beginArray();
        jsonWriter.value(method);
        jsonWriter.beginArray();
        return jsonWriter;
    }

    /**
     * Helper for the end of network to message calls.
     * @param jsonWriter connects to the client
     * @throws IOException if the connection has an error
     */
    private void closeCommand(JsonWriter jsonWriter) throws IOException {
        jsonWriter.endArray();
        jsonWriter.endArray();
        jsonWriter.flush();
    }

    /**
     * Turns the input in the given JsonReader into a FullTurnInfo object.
     * @param jsonReader the JsonReader containing the JSON that represents a FullTurnInfo object
     * @return a FullTurnInfo object
     * @throws IOException if reading from the JsonReader goes wrong
     */
    private FullTurnInfo deserializeFullTurnInfo(JsonReader jsonReader) throws IOException {
        jsonReader.beginArray();
        if (!jsonReader.peek().equals(JsonToken.NUMBER)) {
            throw new IllegalArgumentException("Input must be a number");
        }
        int index = jsonReader.nextInt();

        Direction dir = gson.fromJson(jsonReader, Direction.class);

        if (!jsonReader.peek().equals(JsonToken.NUMBER)) {
            throw new IllegalArgumentException("Input must be a number");
        }
        int degree = jsonReader.nextInt();

        Coordinate coord = gson.fromJson(jsonReader, Coordinate.class);

        jsonReader.endArray();

        ShiftInfo shiftInfo = new ShiftInfo(index,dir);
        return new FullTurnInfo(degree, shiftInfo, coord);
    }
}
