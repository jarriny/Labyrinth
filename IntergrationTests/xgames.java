package IntergrationTests;

import Common.PartsOfTurn.FullTurnInfo;
import Common.State.RefState;
import Players.IPlayer;
import Players.PlayerCompareByName;
import Referee.gameResult;
import Referee.referee;
import Serialization.RefereeStateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class xgames implements IIntegrationtest {

    public static void main(String[] args) {
        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.out.println(new xgames().run(jsonReader));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<IPlayer> readXgamesJSON(JsonReader jsonReader) throws IOException {
        referee ref = new referee();
        jsonReader.setLenient(true);

        GsonBuilder gsonBuilder = new GsonBuilder();
        RefereeStateAdapter.register(gsonBuilder);
        Gson gson = gsonBuilder.create();

        //read player spec
        ArrayList<IPlayer> players = xhelpers.readPlayerSpec(jsonReader);
        //TODO check over this

        // read ref state
        RefState state = gson.fromJson(jsonReader, RefState.class); //xhelpers.readRefState(jsonReader);
        //TODO this should be all good but can check over
        gameResult result = ref.runEntireGame(state, players);
        return result.getWinners();

    }

    private static JsonArray turnInfoToJSON(FullTurnInfo fullTurnInfo) {
        JsonArray array = new JsonArray();
        array.add(fullTurnInfo.getShiftInfo().getIndex());
        array.add(fullTurnInfo.getShiftInfo().getDirection().name());
        array.add(fullTurnInfo.getDegree());
        array.add(xhelpers.coordToJson(fullTurnInfo.getCoord()));
        return array;
    }

    @Override
    public String run(JsonReader jsonreader) throws IOException {
        //returns a ref that has runEntireGame
        ArrayList<IPlayer> winners = readXgamesJSON(jsonreader);
        winners.sort(new PlayerCompareByName());

        JsonArray jsonOutput = xhelpers.playersToJson(winners);
        return jsonOutput.toString();
    }
}
