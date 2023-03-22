package Serialization;

import Common.Board.Gem;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert Gem objects to and from JSON.
 */
public class GemAdapter extends TypeAdapter<Gem> {
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Gem.class, new GemAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, Gem gem) throws IOException {
        jsonWriter.value(gem.toString());
    }

    @Override
    public Gem read(JsonReader jsonReader) throws IOException {
        return stringToGem(jsonReader.nextString());
    }

    public static Gem stringToGem(String gem) {
        for (Gem g: Gem.values()) {
            if (g.getGemString().equals(gem)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Given gem does not exist: " + gem);
    }


}
