package Serialization;

import Common.Board.Board;
import Common.Board.Tile;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Type Adapter to convert State objects to and from JSON.
 */
public class StateAdapter extends TypeAdapter<PlayerGameState> {
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(PlayerGameState.class, new StateAdapter());
    }
    private static final Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        BoardAdapter.register(gsonBuilder);
        TileAdapter.register(gsonBuilder);
        PlayerAdapter.register(gsonBuilder);
        ActionAdapter.register(gsonBuilder);
        gson = gsonBuilder.create();
    }

    @Override
    public void write(JsonWriter jsonWriter, PlayerGameState gameState) throws IOException {
        jsonWriter.flush();
        jsonWriter.jsonValue(gson.toJson(new JsonState(
                gameState.getBoardState(),
                gameState.getSpareTile(),
                gameState.getPublicPlayerInfos(),
                gameState.getLastMove()
        )));
        jsonWriter.flush();
    }

    @Override
    public PlayerGameState read(JsonReader jsonReader) throws IOException {
        JsonState jsonState = gson.fromJson(jsonReader, JsonState.class);
        return new PlayerGameState(
                jsonState.board,
                jsonState.spare,
                jsonState.plmt,
                0,
                jsonState.last == null ? Optional.empty() : jsonState.last
        );
    }

    private static class JsonState {
        public Board board;
        public Tile spare;
        public ArrayList<PlayerInfoPublic> plmt;
        public Optional<ShiftInfo> last;

        public JsonState(Board board, Tile spare, ArrayList<PlayerInfoPublic> plmt, Optional<ShiftInfo> last) {
            this.board = board;
            this.spare = spare;
            this.plmt = plmt;
            this.last = last;
        }
    }
}
