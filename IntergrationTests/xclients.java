package IntergrationTests;

import Client.client;
import Players.BadPlayer;
import Players.BadPlayer2;
import Players.IPlayer;
import Players.player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Runs the integration tests for Milestone 9 and 10. Begins the clients.
 */
public class xclients implements IIntegrationtest {

    final int WAIT_BETWEEN_PLAYERS_S = 3;

    ArrayList<IPlayer> players;
    int port;
    String ipAddress;

    public xclients(int port, String ipAddress) {
        this.port = port;
        this.ipAddress = ipAddress;
        this.players = new ArrayList<>();
    }

    /**
     * Takes in the command line arguments to connect to the desired port and IP address,
     * and parses the input to begin the clients.
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

        String ipAddress;
        try {
            String ipAddressString = args[1];
            ipAddress = ipAddressString;
        }
        catch (Exception e) {
            ipAddress = "127.0.0.1";
        }

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            new xclients(port, ipAddress).run(jsonReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String run(JsonReader jsonreader) throws IOException {
        readInJson(jsonreader);
        createPlayers();
        //gameResult result = instance.runGame();
        return "";
    }

    /**
     * Reads in the input and compiles the list of players represented by the JSON.
     * @param jsonReader the stream that holds the JSON input
     */
    private void readInJson(JsonReader jsonReader) throws IOException {
        jsonReader.setLenient(true);

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        // read all players
        this.players = readPlayerSpec(jsonReader);
    }

    /**
     * Using the input from the given JsonReader, creates a list of IPlayers that are respresented by it.
     * @param jsonReader the stream that holds the JSON input
     * @return an ArrayList of IPlayers created from the input
     */
    private static ArrayList<IPlayer> readPlayerSpec(JsonReader jsonReader) throws IOException {
        ArrayList<IPlayer> players = new ArrayList<>();

        // begin the array of arrays
        jsonReader.beginArray();
        while(jsonReader.hasNext()){

            jsonReader.beginArray();

            // based on the contents of the spec, creates a player, a BadPlayer, or a BadPlayer2
            String name = jsonReader.nextString();
            String strat = jsonReader.nextString();
            IPlayer p = new player(strat, name);
            if(jsonReader.peek().equals(JsonToken.STRING)){
                BadPlayer.BadFM fm = new Gson().fromJson(jsonReader, BadPlayer.BadFM.class);
                if(jsonReader.peek().equals(JsonToken.NUMBER)){
                    p = new BadPlayer2(fm, jsonReader.nextInt(), p);
                }
                else{
                    p = new BadPlayer(fm, p);
                }
            }
            players.add(p);

            jsonReader.endArray();
        }
        jsonReader.endArray();

        return players;
    }

    /**
     * Creates independently running players.
     */
    private void createPlayers(){
        for (IPlayer player : players) {
            client client = new client("localhost", port, player);
            Thread thread = new Thread(client);
            thread.start();

            try {
                TimeUnit.SECONDS.sleep(WAIT_BETWEEN_PLAYERS_S);
            } catch (InterruptedException e) {
            }
        }
    }
}
