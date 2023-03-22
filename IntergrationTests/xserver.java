package IntergrationTests;

import Common.Coordinate;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.RefState;
import Players.IPlayer;
import Referee.gameResult;
import Serialization.*;
import Server.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Runs the integration tests for Milestone 9 and 10. Begins the server.
 */
public class xserver implements IIntegrationtest{

    RefState state;
    int port;
    server server;

    ArrayList<Coordinate> goals;

    private static final Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();

        BoardAdapter.register(gsonBuilder);
        TileAdapter.register(gsonBuilder);
        ActionAdapter.register(gsonBuilder);
        CoordinateAdapter.register(gsonBuilder);
        ColorAdapter.register(gsonBuilder);

        gson = gsonBuilder.create();
    }

    public xserver(int port) {
        this.port = port;
    }

    /**
     * Takes in the command line arguments to connect to the desired port, and parses the input to begin a server.
     */
    public static void main(String[] args) throws IllegalArgumentException {
        String portString = args[0];
        int port;
        try {
            port = Integer.parseInt(portString);
        }
        catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Invalid port number");
        }

        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            new xserver(port).run(jsonReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String run(JsonReader jsonreader) throws IOException {
        readInJson(jsonreader);
        createServer();

        // return writeJson(server.getResult());
        return "Complete.";
    }

    /**
     * Reads in the input from the given JsonReader and converts it into a State object.
     * @param jsonReader the stream containing the input
     */
    private void readInJson(JsonReader jsonReader) throws IOException {
        jsonReader.setLenient(true);

        GsonBuilder gsonBuilder = new GsonBuilder();
        RefereeStateAdapter.register(gsonBuilder);
        Gson gson = gsonBuilder.create();

        // read ref state
        this.state = readRefereeState2(jsonReader);
    }

    /**
     * Given a JsonReader, creates a RefState object from the input. Also deals with the initialization
     * of the list of goals.
     * @param jsonReader the stream containing the input
     * @return a RefState object that is represented by the input
     */
    public RefState readRefereeState2(JsonReader jsonReader) throws IOException {
        // RefState2 created in order to include the list of goals
        JsonRefState2 refState = gson.fromJson(jsonReader, JsonRefState2.class);

        ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();
        ArrayList<PlayerInfoPrivate> privatePlayerInfos = new ArrayList<>();
        for (RefereePlayer refPlayer : refState.plmt) {
            publicPlayerInfos.add(new PlayerInfoPublic(refPlayer.home, refPlayer.current, refPlayer.color));
            privatePlayerInfos.add(new PlayerInfoPrivate(refPlayer.dest, refPlayer.dest.equals(refPlayer.home)));
        }

        // if there are no goals given by the input, set the goals list as empty
        if (refState.goals != null) {
            this.goals = refState.goals;
        }
        else {
            this.goals = new ArrayList<>();
        }

        return new RefState(
                refState.board,
                refState.spare,
                publicPlayerInfos,
                privatePlayerInfos,
                0,
                refState.last
        );
    }

    /**
     * Creates an independently running server.
     */
    private void createServer() throws IOException {
        this.server = new server(port, state, goals);
        Thread thread = new Thread(server);
        thread.start();
    }
}
