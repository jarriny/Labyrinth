package Serialization;

import Common.Coordinate;
import Common.Direction;
import Common.PartsOfTurn.FullTurnInfo;
import Common.PartsOfTurn.ShiftInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Type Adapter to convert Choice objects to and from JSON.
 */
public class ChoiceAdapter extends TypeAdapter<Optional<FullTurnInfo>> {
    private static final Gson gson;
    public static final Type type;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        CoordinateAdapter.register(gsonBuilder);
        gson = gsonBuilder.create();
        type = TypeToken.getParameterized(Optional.class, FullTurnInfo.class).getType();
    }

    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(
                type,
                new ChoiceAdapter());
    }

    @Override
    public void write(JsonWriter jsonWriter, Optional<FullTurnInfo> fullTurnInfo) throws IOException {
        if (fullTurnInfo.isPresent()) {
            FullTurnInfo info = fullTurnInfo.get();
            jsonWriter.beginArray();
            jsonWriter.value(info.getShiftInfo().getIndex());
            jsonWriter.jsonValue(gson.toJson(info.getShiftInfo().getDirection()));
            jsonWriter.value(info.getDegree());
            jsonWriter.jsonValue(gson.toJson(info.getCoord()));
            jsonWriter.endArray();
        } else {
            jsonWriter.value("PASS");
        }
    }

    @Override
    public Optional<FullTurnInfo> read(JsonReader jsonReader) throws IOException {
        System.out.println("read0");
        if (jsonReader.peek().equals(JsonToken.STRING)) {
            System.out.println("read1");

            jsonReader.nextString();
            return Optional.empty();
        }
        System.out.println("read2");
        jsonReader.beginArray();

        System.out.println("read3");

        int index = jsonReader.nextInt();
        Direction dir = gson.fromJson(jsonReader, Direction.class);
        int degree = jsonReader.nextInt();
        Coordinate dest = gson.fromJson(jsonReader, Coordinate.class);

        System.out.println("read4");

        jsonReader.endArray();
        System.out.println("read5");


        return Optional.of(new FullTurnInfo(degree, new ShiftInfo(index, dir), dest));
    }
}
