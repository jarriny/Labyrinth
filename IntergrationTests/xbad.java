package IntergrationTests;

import Common.State.RefState;
import Players.BadPlayer;
import Players.IPlayer;
import Players.player;
import Referee.gameResult;
import Referee.referee;
import Serialization.RefereeStateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class xbad implements IIntegrationtest{
    ArrayList<IPlayer> players;
    RefState state;


    public static void main(String[] args) {
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.out.println(new xbad().run(jsonReader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInJson(JsonReader jsonReader) throws IOException {
        jsonReader.setLenient(true);

        GsonBuilder gsonBuilder = new GsonBuilder();
        RefereeStateAdapter.register(gsonBuilder);
        Gson gson = gsonBuilder.create();

        //read bad players
        this.players = readPlayerSpec(jsonReader);


        // read ref state
        this.state = gson.fromJson(jsonReader, RefState.class);


    }

    public static ArrayList<IPlayer> readPlayerSpec(JsonReader jsonReader) throws IOException{

        ArrayList<IPlayer> players = new ArrayList<>();

        //begin the array of arrays
        jsonReader.beginArray();
        while(jsonReader.hasNext()){

            jsonReader.beginArray();

            String name = jsonReader.nextString();
            String strat = jsonReader.nextString();
            IPlayer p = new player(strat, name);
            if(jsonReader.peek().equals(JsonToken.STRING)){
                p = new BadPlayer(new Gson().fromJson(jsonReader, BadPlayer.BadFM.class), p);
            }
            players.add(p);

            jsonReader.endArray();
        }
        jsonReader.endArray();

        return players;
    }

    private gameResult runGame() {
        return new referee().runEntireGame(state, players);
    }

    private String writeJson(gameResult result) {
        ArrayList<String> winners = new ArrayList<>();
        for (IPlayer p: result.getWinners()) {
            winners.add(p.name());
        }
        ArrayList<String> kickedOut = new ArrayList<>();
        for (IPlayer p: result.getKickedOut()) {
            kickedOut.add(p.name());
        }
        Collections.sort(winners);
        Collections.sort(kickedOut);

        ArrayList<ArrayList<String>> resultArray = new ArrayList<>();
        resultArray.add(winners);
        resultArray.add(kickedOut);

        return new Gson().toJson(resultArray);
    }

    @Override
    public String run(JsonReader jsonreader) throws IOException {
        xbad instance = new xbad();
        instance.readInJson(jsonreader);
        gameResult result = instance.runGame();
        return instance.writeJson(result);

    }

}
