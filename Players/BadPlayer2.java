package Players;

import Common.Board.Board;
import Common.Coordinate;
import Common.PartsOfTurn.FullTurnInfo;
import Common.State.PlayerGameState;

import java.util.Optional;

public class BadPlayer2 implements IPlayer{
    private final BadPlayer.BadFM bad;
    private final IPlayer player;
    private int counter;

    public BadPlayer2(BadPlayer.BadFM bad, int count, IPlayer p) {
        this.bad = bad;
        this.player = p;
        this.counter = count;
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
        if(this.bad == BadPlayer.BadFM.SETUP) {
            this.counter--;
        }
        if (this.counter <= 0) {
            while (true){
                /*if(Thread.interrupted()){
                    throw new InterruptedException("");
                }*/
            }
        }
        this.player.setup(state0, goal);
    }

    @Override
    public Optional<FullTurnInfo> takeTurn(PlayerGameState state) throws Exception {
        if(this.bad == BadPlayer.BadFM.TAKETURN) {
            this.counter--;
        }
        if (this.counter <= 0) {
            while (true){
                /*if(Thread.interrupted()){
                    throw new InterruptedException("");
                }*/
            }
        }
        return this.player.takeTurn(state);
    }

    @Override
    public String win(boolean b) throws Exception {
        if(this.bad == BadPlayer.BadFM.WIN) {
            this.counter--;
        }
        if (this.counter <= 0) {
            while (true){
                /*if(Thread.interrupted()){
                    throw new InterruptedException("");
                }*/
            }
        }
        return this.player.win(b);
    }
}
