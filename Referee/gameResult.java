package Referee;

import Players.IPlayer;

import java.util.ArrayList;

public class gameResult {

    ArrayList<IPlayer> winners = new ArrayList<>();
    ArrayList<IPlayer> kickedOut = new ArrayList<>();

    public gameResult(ArrayList<IPlayer> winners, ArrayList<IPlayer> kickedOut) {
        this.winners = winners;
        this.kickedOut = kickedOut;

    }

    public ArrayList<IPlayer> getWinners() {
        return winners;
    }

    public ArrayList<IPlayer> getKickedOut() {
        return kickedOut;
    }

    @Override
    public String toString() {
        String fullResult = "[";
        String winnersStr = "[";

        for (IPlayer player : winners) {
            winnersStr += player.name();
            winnersStr += ",";
        }

        winnersStr = winnersStr.substring(0, winnersStr.length() - 1);
        winnersStr += "]";

        fullResult += winnersStr;

        String kickedOutStr = "[";

        for (IPlayer player : kickedOut) {
            kickedOutStr += player.name();
            kickedOutStr += ",";
        }

        kickedOutStr = kickedOutStr.substring(0, kickedOutStr.length() - 1);
        kickedOutStr += "]";

        fullResult += kickedOutStr;

        fullResult += "]";

        return fullResult;
    }
}
