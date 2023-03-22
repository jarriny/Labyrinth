package Serialization;

import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert Tile objects to and from JSON.
 */
public class TileAdapter extends TypeAdapter<Tile> {
    private static final Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Gem.class, new GemAdapter());
        gson = gsonBuilder.create();
    }
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Tile.class, new TileAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, Tile tile) throws IOException {
        JsonTile jTile = new JsonTile(Connector.fromShape(tile.getShape()), tile.getGems().getFirstGem(),
                tile.getGems().getSecondGem());

        jsonWriter.jsonValue(gson.toJson(jTile));
    }

    @Override
    public Tile read(JsonReader jsonReader) throws IOException {
        JsonTile jsonTile = gson.fromJson(jsonReader, JsonTile.class);
        return new Tile(jsonTile.tileKey.shape, new GemPair(jsonTile.image1, jsonTile.image2));
    }


    private static class JsonTile {
        @SerializedName("tilekey")
        Connector tileKey;

        @SerializedName("1-image")
        Gem image1;

        @SerializedName("2-image")
        Gem image2;

        public JsonTile(Connector tileKey, Gem image1, Gem image2) {
            this.tileKey = tileKey;
            this.image1 = image1;
            this.image2 = image2;
        }
    }

}
