package krq;

import java.util.Comparator;

public class TreeCompartor implements Comparator<MoveNode> {
    @Override
    public int compare(MoveNode o1, MoveNode o2) {
        if(o1 == null){
            return -1;
        }
        if(o2 == null){
            return 1;
        }
        return new Float(o1.current.score - o2.current.score).intValue();
    }
}
