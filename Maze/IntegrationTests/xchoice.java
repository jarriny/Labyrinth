package IntergrationTests;

import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.State.PlayerGameState;
import Players.Euclid;
import Players.Riemann;
import Players.Strategy;
import Serialization.ChoiceAdapter;
import Serialization.CoordinateAdapter;
import Serialization.StateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class xchoice implements IIntegrationtest {

    public static void main(String[] args) {
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            System.out.println(new xchoice().run(jsonReader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Strategy readXchoiceJSON(JsonReader jsonReader) throws IOException {
        Strategy strat = null;
        jsonReader.setLenient(true);
        GsonBuilder gsonBuilder = new GsonBuilder();
        StateAdapter.register(gsonBuilder);
        CoordinateAdapter.register(gsonBuilder);
        Gson gson = gsonBuilder.create();

        // read strategy choice
        String stratChoice = jsonReader.nextString();
        // read state
        PlayerGameState state = gson.fromJson(jsonReader, PlayerGameState.class);//xhelpers.readState(jsonReader);
        // read coord
        Coordinate coord = gson.fromJson(jsonReader, Coordinate.class); //xhelpers.readCoord(jsonReader);


        if (stratChoice.equals("Euclid")) {
            strat = new Euclid(state, state.getCurrentPublicPlayerInfo(), new PlayerInfoPrivate(coord));
        } else if (stratChoice.equals("Riemann")) {
            strat = new Riemann(state, state.getCurrentPublicPlayerInfo(), new PlayerInfoPrivate(coord));
        } else {
            throw new IllegalArgumentException("not valid strategy");
        }


        return strat;
    }

    private static String turnInfoToJSON(Optional<FullTurnInfo> fullTurnInfo) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        ChoiceAdapter.register(gsonBuilder);
        Gson gson = gsonBuilder.create();

        String json = gson.toJson(fullTurnInfo, ChoiceAdapter.type);
        return json;
    }

    @Override
    public String run(JsonReader jsonreader) throws IOException {
        Strategy strat = readXchoiceJSON(jsonreader);

        Optional<FullTurnInfo> turn = strat.findBestTurn();

        return turnInfoToJSON(turn);

    }
}
