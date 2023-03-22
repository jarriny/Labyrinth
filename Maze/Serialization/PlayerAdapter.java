package Serialization;

import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPublic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert PlayerInfoPublic objects to and from JSON.
 */
public class PlayerAdapter extends TypeAdapter<PlayerInfoPublic> {

    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(PlayerInfoPublic.class, new PlayerAdapter());
    }

    private static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Coordinate.class, new CoordinateAdapter());
        gsonBuilder.registerTypeAdapter(GameColors.class, new ColorAdapter());
        gson = gsonBuilder.create();
    }

    @Override
    public void write(JsonWriter jsonWriter, PlayerInfoPublic playerInfoPublic) throws IOException {
        jsonWriter.jsonValue(gson.toJson(new JsonPlayer(
                playerInfoPublic.getCurrentCoord(),
                playerInfoPublic.getHome(),
                playerInfoPublic.getColor())));
    }

    @Override
    public PlayerInfoPublic read(JsonReader jsonReader) throws IOException {
        JsonPlayer jsonPlayer = gson.fromJson(jsonReader, JsonPlayer.class);
        return new PlayerInfoPublic(jsonPlayer.home, jsonPlayer.current, jsonPlayer.color);
    }

    /**
     * Represents a player made from information received from the JSON input.
     */
    private static class JsonPlayer {
        public Coordinate current;
        public Coordinate home;
        public GameColors color;

        public JsonPlayer(Coordinate current, Coordinate home, GameColors color) {
            this.current = current;
            this.home = home;
            this.color = color;
        }
    }
}
