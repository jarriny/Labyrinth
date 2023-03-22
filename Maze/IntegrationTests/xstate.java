package IntergrationTests;

import Common.Coordinate;
import Common.Direction;
import Common.State.PlayerGameState;

import com.google.gson.stream.JsonReader;
import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

// to make jar: mvn clean compile assembly:single

public class xstate implements IIntegrationtest{

  public static void main(String[] args) {
    readDoTurnPrintCoords();
  }

  private static void readDoTurnPrintCoords() {
    try (JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

      System.out.println(new xstate().run(jsonReader));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String run(JsonReader jsonReader) throws IOException{
    jsonReader.setLenient(true);
    // read state
    PlayerGameState state = xhelpers.readState(jsonReader);

    int moveIndex = jsonReader.nextInt();
    String moveDirection = jsonReader.nextString();
    int moveDegree = jsonReader.nextInt();

    state = state.tryTurn(Direction.getStringToDirection(moveDirection), moveIndex, moveDegree);
    Set<Coordinate> reachableCoords = state.getBoardState().reachable(state.getCurrentPublicPlayerInfo().getCurrentCoord());
    ArrayList<Coordinate> listOfReachableCoords = new ArrayList<Coordinate>(reachableCoords);
    ArrayList<Coordinate> sortedlistOfCoords = xhelpers.sortsCoords(listOfReachableCoords);
    JsonArray jsonOutput = xhelpers.coordsToJson(sortedlistOfCoords);

    return jsonOutput.toString();
  }
}
