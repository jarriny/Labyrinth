import IntergrationTests.*;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class runAllIntegrationTests {


    public static void main(String[] args) throws IOException {
        String xBoardTestFiles = "3/GivenTests/";
        String xStateTestFiles = "4/GivenTests/";
        String xChoiceTestFiles = "5/GivenTests/";
        String xGamesTestFiles = "6/GivenTests/";
        String xBadTestFiles = "7/GivenTests/";
        String xBadTest2Files = "8/GivenTests/";
        String xNetworkFiles = "9/GivenTestsSplit/";

        System.out.println("Testing xboard");
        runIntegrationTest(new xboard(), xBoardTestFiles);
        System.out.println("Testing xstate");
        runIntegrationTest(new xstate(), xStateTestFiles);
        System.out.println("Testing xchoice");
        runIntegrationTest(new xchoice(), xChoiceTestFiles);
        System.out.println("Testing xgames");
        runIntegrationTest(new xgames(), xGamesTestFiles);
        System.out.println("Testing xbad");
        runIntegrationTest(new xbad(), xBadTestFiles);
        System.out.println("Testing xbad2");
        runIntegrationTest(new xbad2(), xBadTest2Files);
        //System.out.println("Testing xclients and xserver");
        //runIntegrationTestNetwork(xNetworkFiles);
        //System.out.println("All integration tests passed");

    }

    public static void runIntegrationTest(IIntegrationtest test, String testDirectory) throws IOException {

        for (String testName: getTestNames(testDirectory)) {
            String actual = test.run(new JsonReader(Files.newBufferedReader(Path.of(
                    testDirectory, testName + "-in.json"))));
            Reader expected = Files.newBufferedReader(Path.of(testDirectory, testName + "-out.json"));

            assertEquals(JsonParser.parseReader(expected), JsonParser.parseString(actual));
        }
    }

    public static void runIntegrationTestNetwork(String testDirectory) throws IOException {

        for (String testName: getTestNames(testDirectory)) {
            IIntegrationtest server = new xserver(12340 + Integer.parseInt(testName));
            IIntegrationtest client = new xclients(12340 + Integer.parseInt(testName),"127.0.0.1");
            try {
                String serverResult = server.run(new JsonReader(Files.newBufferedReader(Path.of(
                        testDirectory, testName + "server-in.json"))));

                String clientResult = client.run(new JsonReader(Files.newBufferedReader(Path.of(
                        testDirectory, testName + "client-in.json"))));

                Reader expected = Files.newBufferedReader(Path.of(testDirectory, testName + "-out.json"));

                System.out.println("Expected result:" + JsonParser.parseReader(expected));
                String runResult = String.valueOf(JsonParser.parseString(serverResult));

            }
            catch(Exception e){
                System.out.println("Error while running " + testName + "-in.json: " + e);
            }
        }

    }

    public static ArrayList<String> getTestNames(String testDirectory){

        //need to get all the files in a directory
        File directoryFile = new File(testDirectory);
        String[] allTests = directoryFile.list();

        HashSet<String> prefixes = new HashSet<>();
        assert allTests != null;
        for (String test: allTests) {
            if (test.charAt(0) != '.') {
                prefixes.add("" + test.charAt(0));
            }
        }
        return new ArrayList<>(prefixes);
    }

}
