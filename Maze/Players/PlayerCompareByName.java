package Players;

import java.util.Comparator;

public class PlayerCompareByName implements Comparator<IPlayer> {

    @Override
    public int compare(IPlayer p1, IPlayer p2) {
        return p1.name().compareTo(p2.name());
    }
}
