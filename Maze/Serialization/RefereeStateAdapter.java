package Serialization;

import Common.Board.Board;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.RefState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Type Adapter to convert RefState objects to and from JSON.
 */
public class RefereeStateAdapter extends TypeAdapter<RefState> {
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(RefState.class, new RefereeStateAdapter());
    }
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

    @Override
    public void write(JsonWriter jsonWriter, RefState refState) throws IOException {
        JsonRefState jsonRefState = new JsonRefState(
                refState.getBoardState(),
                refState.getSpareTile(),
                refState.getPublicPlayerInfos(),
                refState.getPrivatePlayerInfos(),
                refState.getLastMove()
        );
        jsonWriter.jsonValue(gson.toJson(jsonRefState));
    }

    @Override
    public RefState read(JsonReader jsonReader) throws IOException {
        JsonRefState refState = gson.fromJson(jsonReader, JsonRefState.class);
        ArrayList<PlayerInfoPublic> publicPlayerInfos = new ArrayList<>();
        ArrayList<PlayerInfoPrivate> privatePlayerInfos = new ArrayList<>();
        for (RefereePlayer refPlayer : refState.plmt) {
            publicPlayerInfos.add(new PlayerInfoPublic(refPlayer.home, refPlayer.current, refPlayer.color));
            privatePlayerInfos.add(new PlayerInfoPrivate(refPlayer.dest, refPlayer.dest.equals(refPlayer.home)));
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
     * Represents the referee state created from the information given by the JSON input.
     */
    private static class JsonRefState {
        public Board board;
        public Tile spare;
        public ArrayList<RefereePlayer> plmt;
        public Optional<ShiftInfo> last;

        public JsonRefState(Board board,
                            Tile spare,
                            ArrayList<PlayerInfoPublic> publicPlayerInfos,
                            ArrayList<PlayerInfoPrivate> privatePlayerInfos,
                            Optional<ShiftInfo> last) {
            this.board = board;
            this.spare = spare;
            this.last = last;
            this.plmt = new ArrayList<>();
            for (int i = 0; i < publicPlayerInfos.size(); i++) {
                this.plmt.add(new RefereePlayer(publicPlayerInfos.get(i), privatePlayerInfos.get(i)));
            }
        }
    }

    /**
     * Represents the player created from the information given by the JSON input.
     */
    private static class RefereePlayer {
        Coordinate current;
        Coordinate home;
        @SerializedName("goto")
        Coordinate dest;
        GameColors color;

        public RefereePlayer(PlayerInfoPublic playerInfoPublic, PlayerInfoPrivate playerInfoPrivate) {
            this.current = playerInfoPublic.getCurrentCoord();
            this.home = playerInfoPublic.getHome();
            this.color = playerInfoPublic.getColor();
            this.dest = playerInfoPrivate.isGoingHome() ? this.home : playerInfoPrivate.getGoal();
        }
    }
}
