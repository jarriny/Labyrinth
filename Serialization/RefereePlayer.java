package Serialization;

import Common.Coordinate;
import Common.PlayerInfo.GameColors;
import Common.PlayerInfo.PlayerInfoPrivate;
import Common.PlayerInfo.PlayerInfoPublic;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the player created from the information given by the JSON input.
 */
public class RefereePlayer {
    public Coordinate current;
    public Coordinate home;
    @SerializedName("goto")
    public Coordinate dest;
    public GameColors color;

    public RefereePlayer(PlayerInfoPublic playerInfoPublic, PlayerInfoPrivate playerInfoPrivate) {
        this.current = playerInfoPublic.getCurrentCoord();
        this.home = playerInfoPublic.getHome();
        this.color = playerInfoPublic.getColor();
        this.dest = playerInfoPrivate.isGoingHome() ? this.home : playerInfoPrivate.getGoal();
    }
}
