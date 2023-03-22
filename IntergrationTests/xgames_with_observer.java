package IntergrationTests;

import Common.PartsOfTurn.FullTurnInfo;
import Common.State.RefState;
import Players.IPlayer;
import Players.PlayerCompareByName;
import Referee.gameResult;
import Referee.observer;
import Referee.referee;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class xgames_with_observer {

    public static void main(String[] args) {
        //returns a ref that has runEntireGame
        ArrayList<IPlayer> winners = readXgamesJSON();
        winners.sort(new PlayerCompareByName());

        JsonArray jsonOutput = xhelpers.playersToJson(winners);
        System.out.println(jsonOutput);
    }

    private static ArrayList<IPlayer> readXgamesJSON() {
        ArrayList<IPlayer> winners = new ArrayList<>();

        try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            jsonReader.setLenient(true);

            //read player spec
            ArrayList<IPlayer> players = xhelpers.readPlayerSpec(jsonReader);

            // read ref state
            RefState state = xhelpers.readRefState(jsonReader);
            observer o = new observer();
            referee ref = new referee();
            ref.addObserver(o);

            gameResult result = ref.runEntireGame(state, players);
            return result.getWinners();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return winners;
    }

    private static JsonArray turnInfoToJSON(FullTurnInfo fullTurnInfo) {
        JsonArray array = new JsonArray();
        array.add(fullTurnInfo.getShiftInfo().getIndex());
        array.add(fullTurnInfo.getShiftInfo().getDirection().name());
        array.add(fullTurnInfo.getDegree());
        array.add(xhelpers.coordToJson(fullTurnInfo.getCoord()));
        return array;
    }

}
