package IntergrationTests;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

public interface IIntegrationtest {

    String run(JsonReader jsonreader) throws IOException;
}
