package Players;

import Common.Board.Board;
import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.State.PlayerGameState;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public class BadPlayer implements IPlayer{

    private final BadFM bad;
    private final IPlayer player;

    public BadPlayer(BadFM bad, IPlayer p) {
        this.bad = bad;
        this.player = p;
    }

    @Override
    public String name() {
        return player.name();
    }

    @Override
    public Board proposeBoard0(int rows, int columns) throws Exception {
        return player.proposeBoard0(rows, columns);
    }

    @Override
    public void setup(Optional<PlayerGameState> state0, Coordinate goal) throws Exception {
        if(this.bad == BadFM.SETUP){
            int i = 1/0;
        }
        player.setup(state0, goal);
    }

    @Override
    public Optional<FullTurnInfo> takeTurn(PlayerGameState state) throws Exception{
        if(this.bad == BadFM.TAKETURN){
            int i = 1/0;
        }
        return player.takeTurn(state);
    }

    @Override
    public String win(boolean b) throws Exception{
        if(this.bad == BadFM.WIN){
            int i = 1/0;
        }
        return player.win(b);
    }

    public enum BadFM{
        @SerializedName("setUp")
        SETUP(),
        @SerializedName("takeTurn")
        TAKETURN(),
        @SerializedName("win")
        WIN()
    }
}
