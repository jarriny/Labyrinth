package Serialization;

import Common.Direction;
import Common.PartsOfTurn.ShiftInfo;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Type Adapter to convert Action objects to and from JSON.
 */
public class ActionAdapter extends TypeAdapter<Optional<ShiftInfo>> {
    public static final Type type = TypeToken.getParameterized(Optional.class, ShiftInfo.class).getType();
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(type, new ActionAdapter());
    }
    @Override
    public void write(JsonWriter jsonWriter, Optional<ShiftInfo> shiftInfo) throws IOException {
        if(shiftInfo.isPresent()){
            jsonWriter.beginArray();
            jsonWriter.value(shiftInfo.get().getIndex());
            jsonWriter.jsonValue(new Gson().toJson(shiftInfo.get().getDirection()));
            jsonWriter.endArray();
        }
        else{
            jsonWriter.nullValue();
        }
    }

    @Override
    public Optional<ShiftInfo> read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek().equals(JsonToken.NULL)){
            jsonReader.nextNull();
            return Optional.empty();
        }
        else{
            jsonReader.beginArray();
            if (!jsonReader.peek().equals(JsonToken.NUMBER)) {
                throw new IllegalArgumentException("Invalid action index input, must be an integer");
            }
            int index = jsonReader.nextInt();
            if (index < 0) {
                throw new IllegalArgumentException("Invalid action index input, must be a natural number");
            }

            try {
                Direction dir = new Gson().fromJson(jsonReader, Direction.class);
                jsonReader.endArray();

                return Optional.of(new ShiftInfo(index, dir));
            }
            catch (JsonIOException | JsonSyntaxException e) {
                throw new IllegalArgumentException("Illegal direction input");
            }
        }
    }
}
