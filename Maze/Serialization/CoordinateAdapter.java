package Serialization;

import Common.Coordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Type Adapter to convert Coordinate objects to and from JSON.
 */
public class CoordinateAdapter extends TypeAdapter<Coordinate> {
    public static void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Coordinate.class, new CoordinateAdapter());
    }
    @Override
    public void write(JsonWriter jsonWriter, Coordinate coordinate) throws IOException {

        jsonWriter.jsonValue(new Gson().toJson(new JsonCoordinate(coordinate.getRow(), coordinate.getCol())));
    }

    @Override
    public Coordinate read(JsonReader jsonReader) throws IOException {
        JsonCoordinate jsonCoord =  new Gson().fromJson(jsonReader, JsonCoordinate.class);

        return new Coordinate(jsonCoord.row, jsonCoord.column);
    }

    private static class JsonCoordinate {
        @SerializedName("row#")
        Integer row;

        @SerializedName("column#")
        Integer column;

        public JsonCoordinate(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }
    }
}
