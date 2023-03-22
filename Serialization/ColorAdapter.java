package Serialization;

import Common.PlayerInfo.GameColors;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert GameColor objects to and from JSON.
 */
public class ColorAdapter extends TypeAdapter<GameColors> {
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(GameColors.class, new ColorAdapter());
    }
    @Override
    public void write(JsonWriter jsonWriter, GameColors gameColors) throws IOException {
        jsonWriter.value(gameColors.getColorString());
    }

    @Override
    public GameColors read(JsonReader jsonReader) throws IOException {
        String colorStr = jsonReader.nextString();

        return new GameColors(colorStr);
    }
}
