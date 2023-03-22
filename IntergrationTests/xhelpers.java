package IntergrationTests;

import Common.Board.Board;
import Common.Board.Gem;
import Common.Board.GemPair;
import Common.Board.Tile;
import Common.Coordinate;
import Common.CoordinateCompareByRow;
import Common.Direction;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.IPlayerInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import Common.State.PlayerGameState;
import Common.State.RefState;
import Players.IPlayer;
import Players.player;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public final class xhelpers {

    public static PlayerGameState readState(JsonReader jsonReader) throws IOException {

        //begin state object
        jsonReader.beginObject();

        //start parsing board
        Tile[] boardTiles = new Tile[0];

        //read spare
        Tile spareTile = null;

        //read plmt
        ArrayList<PlayerInfoPublic> players = new ArrayList<>();

        //read last move
        Optional<ShiftInfo> lastMove = Optional.empty();

        while (jsonReader.hasNext()) {
            String next = jsonReader.nextName();
            switch (next) {
                case "board": {
                    boardTiles = boardReader(jsonReader);
                    break;
                }
                case "spare": {
                    spareTile = readSpareTile(jsonReader);
                    break;
                }
                case "plmt": {
                    players = readPlayers(jsonReader);
                    break;
                }
                case "last": {
                    lastMove = readLastMove(jsonReader);
                    break;
                }
            }
        }

        //end state object
        jsonReader.endObject();

        return new PlayerGameState(new Board(7, 7, boardTiles), spareTile, players,
                0, lastMove);
    }

    public static ArrayList<IPlayer> readPlayerSpec(JsonReader jsonReader) throws IOException{

        ArrayList<IPlayer> players = new ArrayList<>();

        //begin the array of arrays
        jsonReader.beginArray();
        while(jsonReader.hasNext()){

                jsonReader.beginArray();

                String name = jsonReader.nextString();
                String strat = jsonReader.nextString();
                players.add(new player(strat, name));

                jsonReader.endArray();
        }
        jsonReader.endArray();

        return players;
    }


    public static RefState readRefState(JsonReader jsonReader) throws IOException {

        //begin state object
        jsonReader.beginObject();

        //start parsing board
        Tile[] boardTiles = new Tile[0];

        //read spare
        Tile spareTile = null;

        //read plmt
        ArrayList<ArrayList<IPlayerInfo>> players = new ArrayList<>();

        ArrayList<PlayerInfoPublic> playersPublic = new ArrayList<>();

        ArrayList<PlayerInfoPrivate> playersPrivate = new ArrayList<>();


        //read last move
        Optional<ShiftInfo> lastMove = Optional.empty();

        while (jsonReader.hasNext()) {
            String next = jsonReader.nextName();
            switch (next) {
                case "board": {
                    boardTiles = boardReader(jsonReader);
                    break;
                }
                case "spare": {
                    spareTile = readSpareTile(jsonReader);
                    break;
                }
                case "plmt": {
                    players = readPlayersPrivate(jsonReader);
                    //need to also get all the public info
                    break;
                }
                case "last": {
                    lastMove = readLastMove(jsonReader);
                    break;
                }
            }
        }

        //end state object
        jsonReader.endObject();
        for(int p = 0; p < players.size(); p++){
            playersPublic.add((PlayerInfoPublic) players.get(p).get(0));
            playersPrivate.add((PlayerInfoPrivate) players.get(p).get(1));
        }


        return new RefState(new Board(7, 7, boardTiles), spareTile, playersPublic, playersPrivate,
                0, lastMove);
    }

    public static JsonArray playersToJson(ArrayList<IPlayer> players) {
        JsonArray array = new JsonArray();

        for (IPlayer p : players) {
            array.add(p.name());
        }

        return array;
    }

    public static ArrayList<ArrayList<IPlayerInfo>> readPlayersPrivate(JsonReader jsonReader) {
        ArrayList<ArrayList<IPlayerInfo>> players = new ArrayList<ArrayList<IPlayerInfo>>();
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                players.add(readPlayerPrivate(jsonReader));
            }
            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static ArrayList<IPlayerInfo> readPlayerPrivate(JsonReader jsonReader) {
        ArrayList<IPlayerInfo> allPlayerInfo = new ArrayList<>();
        PlayerInfoPublic playerPublic = null;
        PlayerInfoPrivate playerPrivate = null;
        try {
            jsonReader.beginObject();
            Coordinate currentValue = null;
            Coordinate homeValue = null;
            Coordinate gotoValue = null;
            String colorValue = "";
            while (jsonReader.hasNext()) {
                String next = jsonReader.nextName();
                switch (next) {
                    case "current": {
                        currentValue = readCoord(jsonReader);
                        break;
                    }
                    case "home": {
                        homeValue = readCoord(jsonReader);
                        break;
                    }
                    case "goto": {
                        gotoValue = readCoord(jsonReader);
                        break;
                    }
                    case "color": {
                        colorValue = jsonReader.nextString();
                        break;
                    }
                }
            }
            jsonReader.endObject();
            playerPublic = new PlayerInfoPublic(homeValue, currentValue, new GameColors(colorValue));
            playerPrivate = new PlayerInfoPrivate(gotoValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //public info first
        allPlayerInfo.add(playerPublic);
        allPlayerInfo.add(playerPrivate);
        return allPlayerInfo;
    }

    public static ArrayList<Coordinate> sortsCoords(ArrayList<Coordinate> readableCoords) {
        readableCoords.sort(new CoordinateCompareByRow());
        return readableCoords;
    }

    public static ArrayList<Coordinate> sortsPlayers(ArrayList<Coordinate> readableCoords) {
        readableCoords.sort(new CoordinateCompareByRow());
        return readableCoords;
    }

    public static JsonArray coordsToJson(ArrayList<Coordinate> sortedlistOfCoords) {
        JsonArray array = new JsonArray();

        for (Coordinate c : sortedlistOfCoords) {
            array.add(coordToJson(c));
        }

        return array;
    }

    public static JsonObject coordToJson(Coordinate coord) {
        JsonObject coordJSON = new JsonObject();
        coordJSON.addProperty("column#", coord.getCol());
        coordJSON.addProperty("row#", coord.getRow());
        return coordJSON;
    }

    public static Optional<ShiftInfo> readLastMove(JsonReader jsonReader) {
        Optional<ShiftInfo> shiftTurnInfo = Optional.empty();
        try {
            try {
                jsonReader.beginArray();
                int index = jsonReader.nextInt();
                String direction = jsonReader.nextString();
                shiftTurnInfo = Optional.of(new ShiftInfo(index, Direction.getStringToDirection(direction)));
                jsonReader.endArray();
            } catch (IllegalStateException e) {
                // expects null
                jsonReader.nextNull();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shiftTurnInfo;
    }

    public static ArrayList<PlayerInfoPublic> readPlayers(JsonReader jsonReader) {
        ArrayList<PlayerInfoPublic> players = new ArrayList<PlayerInfoPublic>();
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                players.add(readPlayer(jsonReader));
            }
            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static PlayerInfoPublic readPlayer(JsonReader jsonReader) {
        PlayerInfoPublic player = null;
        try {
            jsonReader.beginObject();
            Coordinate currentValue = null;
            Coordinate homeValue = null;
            String colorValue = "";
            while (jsonReader.hasNext()) {
                String next = jsonReader.nextName();
                switch (next) {
                    case "current": {
                        currentValue = readCoord(jsonReader);
                        break;
                    }
                    case "home": {
                        homeValue = readCoord(jsonReader);
                        break;
                    }
                    case "color": {
                        colorValue = jsonReader.nextString();
                        break;
                    }
                }
            }
            jsonReader.endObject();
            player = new PlayerInfoPublic(homeValue, currentValue, new GameColors(colorValue));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    public static Coordinate readCoord(JsonReader jsonReader) throws IOException {
        //begin coord object
        jsonReader.beginObject();

        int rowNum = 0;
        int colNum = 0;

        //start parsing coords

        while (jsonReader.hasNext()) {
            String next = jsonReader.nextName();
            switch (next) {
                case "row#": {
                    rowNum = jsonReader.nextInt();
                    break;
                }
                case "column#": {
                    colNum = jsonReader.nextInt();
                    break;
                }
            }
        }

        //end coord object
        jsonReader.endObject();

        return new Coordinate(rowNum, colNum);
    }

    public static Tile readSpareTile(JsonReader jsonReader) throws IOException {
        //begin spare object
        jsonReader.beginObject();

        String tileKey = "";
        String firstGem = "";
        String secondGem = "";

        while (jsonReader.hasNext()) {
            String next = jsonReader.nextName();
            switch (next) {
                case "tilekey": {
                    tileKey = jsonReader.nextString();
                    break;
                }
                case "1-image": {
                    firstGem = jsonReader.nextString();
                    break;
                }
                case "2-image": {
                    secondGem = jsonReader.nextString();
                    break;
                }
            }
        }
        //start parsing spare

        //end of spare
        jsonReader.endObject();

        return new Tile(xhelpers.findShapeRepresentation(tileKey),
                new GemPair(getName(firstGem), getName(secondGem)));

    }

    public static Tile[] boardReader(JsonReader jsonReader) {
        ArrayList<String> shapeList = new ArrayList<>();
        ArrayList<String> treasureList = new ArrayList<>();
        try {

            //begin board object
            jsonReader.beginObject();

            //start parsing connectors
            String connectors = jsonReader.nextName();

            //start of connectors
            jsonReader.beginArray();
            shapeList = xhelpers.parseShapes(jsonReader);

            //end of connectors
            jsonReader.endArray();

            //start parsing treasures
            String treasures = jsonReader.nextName();

            //start of treasures
            jsonReader.beginArray();
            treasureList = xhelpers.parseTreasures(jsonReader);

            //end of treasures
            jsonReader.endArray();

            //end of board
            jsonReader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Tile[] listOfTiles = new Tile[49];
        for (int t = 0; t < shapeList.size(); t++) {
            listOfTiles[t] = (new Tile(findShapeRepresentation(shapeList.get(t)),
                    new GemPair(getName(treasureList.get(t * 2))
                            , getName(treasureList.get((t * 2) + 1)))));
        }
        return listOfTiles;
    }

    public static Gem getName(String gem){
        for(Gem g: Gem.values()){
            if(g.getGemString().equals(gem)){
                return g;
            }
        }
        throw new IllegalArgumentException("Given gem does not exist");
    }

    public static boolean[] findShapeRepresentation(String shape) {
        switch (shape) {
            case "│":
                return new boolean[]{true, false, true, false};
            case "─":
                return new boolean[]{false, true, false, true};
            case "┐":
                return new boolean[]{false, false, true, true};
            case "└":
                return new boolean[]{true, true, false, false};
            case "┌":
                return new boolean[]{false, true, true, false};
            case "┘":
                return new boolean[]{true, false, false, true};
            case "┬":
                return new boolean[]{false, true, true, true};
            case "├":
                return new boolean[]{true, true, true, false};
            case "┴":
                return new boolean[]{true, true, false, true};
            case "┤":
                return new boolean[]{true, false, true, true};
            case "┼":
                return new boolean[]{true, true, true, true};
        }
        throw new IllegalArgumentException("Illegal shape given");
    }

    public static ArrayList<String> parseShapes(JsonReader reader) {
        ArrayList<String> shapes = new ArrayList<String>();
        String value;
        try {
            for (int x = 1; x <= 7; x++) {
                reader.beginArray();
                for (int y = 1; y <= 7; y++) {
                    value = reader.nextString();
                    shapes.add(value);
                }
                reader.endArray();
            }
        } catch (IOException ex) {
            System.out.println("illegal input given");
        }
        return shapes;
    }

    public static ArrayList<String> parseTreasures(JsonReader reader) {
        ArrayList<String> shapes = new ArrayList<String>();

        String t1;
        String t2;
        try {
            for (int x = 1; x <= 7; x++) {
                reader.beginArray();
                for (int y = 1; y <= 7; y++) {
                    reader.beginArray();
                    t1 = reader.nextString();
                    t2 = reader.nextString();
                    reader.endArray();
                    shapes.add(t1);
                    shapes.add(t2);
                }
                reader.endArray();
            }
        } catch (IOException ex) {
            System.out.println("illegal input given");
        }
        return shapes;
    }

}
