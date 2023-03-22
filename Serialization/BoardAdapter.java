package Serialization;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Type Adapter to convert Board objects to and from JSON.
 */
public class BoardAdapter extends TypeAdapter<Board> {
    private static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Gem.class, new GemAdapter());
        gson = gsonBuilder.create();
    }

    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Board.class, new BoardAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, Board board) throws IOException {
        jsonWriter.jsonValue(gson.toJson(new JsonBoard(board)));
    }

    @Override
    public Board read(JsonReader jsonReader) throws IOException {
        JsonBoard jsonBoard =  gson.fromJson(jsonReader, JsonBoard.class);

        ArrayList<Tile> tiles = new ArrayList<>();
        int rows = jsonBoard.connectors.size();
        int cols = jsonBoard.connectors.get(0).size();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tile tile = new Tile(
                        jsonBoard.connectors.get(row).get(col).shape,
                        new GemPair(
                                jsonBoard.treasures.get(row).get(col)[0],
                                jsonBoard.treasures.get(row).get(col)[1]
                        )
                    );
                tiles.add(tile);
            }
        }

        return new Board(rows, cols, tiles.toArray(new Tile[0]));
    }

    private static class JsonBoard {
        public ArrayList<ArrayList<Connector>> connectors;
        public ArrayList<ArrayList<Gem[]>> treasures;

        public JsonBoard(Board board) {
            this.connectors = new ArrayList<>();
            this.treasures = new ArrayList<>();

            Tile[][] tiles = board.getBoardRep();
            for (Tile[] row : tiles) {
                ArrayList<Connector> connectorRow = new ArrayList<>();
                ArrayList<Gem[]> gemRow = new ArrayList<>();
                for (Tile tile : row) {
                    connectorRow.add(Connector.fromShape(tile.getShape()));
                    gemRow.add(new Gem[] {tile.getGems().getFirstGem(), tile.getGems().getSecondGem()});
                }
                this.connectors.add(connectorRow);
                this.treasures.add(gemRow);
            }
        }

        public JsonBoard(ArrayList<ArrayList<Connector>> connectors, ArrayList<ArrayList<Gem[]>> treasures) {
            this.connectors = connectors;
            this.treasures = treasures;
        }
    }
}
