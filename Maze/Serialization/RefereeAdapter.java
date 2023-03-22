package Serialization;

import Referee.referee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert Referee objects to and from JSON.
 */
public class RefereeAdapter extends TypeAdapter<referee> {

    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(referee.class, new RefereeAdapter());
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
    public void write(JsonWriter out, referee value) throws IOException {

    }

    @Override
    public referee read(JsonReader in) throws IOException {
        return null;
    }
}
