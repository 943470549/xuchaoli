package krq;

import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapTree {
    private int deepth = 0;
    private Map<Integer, Map<DenseMatrix64F, List<MoveNode>>> tree = new HashMap();

    public void put(Map<DenseMatrix64F, List<MoveNode>> map){
        tree.put(++deepth, map);
    }
    public Map<DenseMatrix64F, List<MoveNode>> getDeepestNodesMap(){
        if(deepth == 0){
            return new HashMap<>();
        }
        return tree.get(deepth);
    }

    public Map<DenseMatrix64F, List<MoveNode>> getDeepestNodesMap(int deep){
        if(deepth == 0){
            return new HashMap<>();
        }
        return tree.get(deep);
    }

    public int getDeepth() {
        return deepth;
    }

    public void setDeepth(int deepth) {
        this.deepth = deepth;
    }

    public List<MoveNode> getDeepestNodes(int deep){
        List<MoveNode> result = new ArrayList<>();
        if(deepth == 0){
            return result;
        }
        Map<DenseMatrix64F, List<MoveNode>> map = tree.get(deep);
        if(map == null){
            return result;
        }
        for(List<MoveNode> value : map.values()){
            result.addAll(value);
        }
        return result;
    }

    public void destory(){
        this.deepth =0;
        this.tree = null;
    }

}
