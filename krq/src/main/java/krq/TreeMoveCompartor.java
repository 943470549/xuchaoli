package krq;

import java.util.Comparator;

public class TreeMoveCompartor implements Comparator<Move> {
    @Override
    public int compare(Move o1, Move o2) {
        if(o1 == null){
            return -1;
        }
        if(o2 == null){
            return 1;
        }
        return new Float(o1.score - o2.score).intValue();
    }
}
