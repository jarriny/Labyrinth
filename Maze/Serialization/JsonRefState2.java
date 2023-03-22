package Serialization;

import Common.Board.Board;
import Common.Board.Tile;
import Common.Coordinate;
import Common.PartsOfTurn.ShiftInfo;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents the State received from Milestone 10 inputs. Includes a list of Coordinates
 * that represents the additional goals.
 */
public class JsonRefState2 {
    public Board board;
    public Tile spare;
    public ArrayList<Coordinate> goals;
    public ArrayList<RefereePlayer> plmt;
    public Optional<ShiftInfo> last;

    public JsonRefState2(Board board,
                         Tile spare,
                         ArrayList<PlayerInfoPublic> publicPlayerInfos,
                         ArrayList<PlayerInfoPrivate> privatePlayerInfos,
                         ArrayList<Coordinate> goals,
                         Optional<ShiftInfo> last) {
        this.board = board;
        this.spare = spare;
        this.last = last;
        this.plmt = new ArrayList<>();
        for (int i = 0; i < publicPlayerInfos.size(); i++) {
            this.plmt.add(new RefereePlayer(publicPlayerInfos.get(i), privatePlayerInfos.get(i)));
        }
        this.goals = new ArrayList<>();
        for (Coordinate coord : goals){
            this.goals.add(coord);
        }
    }
}
