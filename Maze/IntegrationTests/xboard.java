package IntergrationTests;

import Common.Board.Board;
import Common.Coordinate;
import Serialization.BoardAdapter;
import Serialization.CoordinateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public class xboard implements IIntegrationtest {

  public static void main(String[] args) throws IllegalArgumentException {

    try (
            JsonReader jsonReader = new JsonReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

      System.out.println(new xboard().run(jsonReader));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String run(JsonReader jsonReader) throws IOException {
    jsonReader.setLenient(true);

    GsonBuilder gsonBuilder = new GsonBuilder();
    BoardAdapter.register(gsonBuilder);
    CoordinateAdapter.register(gsonBuilder);
    Gson gson = gsonBuilder.create();

    Board b = gson.fromJson(jsonReader, Board.class);

    Coordinate coordinate = gson.fromJson(jsonReader, Coordinate.class);

    Set<Coordinate> reachableCoords = b.reachable(new Coordinate(coordinate.getRow(), coordinate.getCol()));
    ArrayList<Coordinate> listOfReachableCoords = new ArrayList<Coordinate>(reachableCoords);
    ArrayList<Coordinate> sortedlistOfCoords = xhelpers.sortsCoords(listOfReachableCoords);
    JsonArray jsonOutput = xhelpers.coordsToJson(sortedlistOfCoords);

    return jsonOutput.toString();
  }

}