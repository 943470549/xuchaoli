package krq;

import org.ejml.data.DenseMatrix64F;

import java.util.*;

public class MoveUtils1 {

    public static Move randomMove(Room1 room, boolean isGun) {
        List<MoveNode> avalMoves;
        avalMoves = MoveUtils1.getAvliavleMoveList(room.qipan, isGun);
        Random random = new Random();
        int i = random.nextInt(avalMoves.size());
        return avalMoves.get(i).root;
    }

    public static Move bestMove(Room1 room, int times, boolean isGun) {
        Random random = new Random();
        HashMapTree tree = new HashMapTree();
        DenseMatrix64F root = room.qipan;
        DenseMatrix64F qipan = root;
        List<MoveNode> avalMoves;
        avalMoves = MoveUtils1.getAvliavleMoveList(qipan, isGun);
        Map<DenseMatrix64F, List<MoveNode>> initmap = new HashMap<>();
        initmap.put(qipan, avalMoves);
        tree.put(initmap);
        while (tree.getDeepth() < times * 2) {
            initmap = tree.getDeepestNodesMap(tree.getDeepth());
            Map<DenseMatrix64F, List<MoveNode>> map = new HashMap<>();
            for (Map.Entry<DenseMatrix64F, List<MoveNode>> entry : initmap.entrySet()) {
                avalMoves = entry.getValue();
                for (MoveNode move : avalMoves) {
                    List<MoveNode> enemyList = MoveUtils1.getEnemyMoveList(qipan, move.root, move.current);
                    if (enemyList != null && enemyList.size() > 0) {
                        map.put(MoveUtils1.getJumian(qipan, move.current), enemyList);
                    } else if (move.current.isGun == isGun) {
                        return move.root;
                    }
                }
            }
            tree.put(map);
        }

        Map<DenseMatrix64F, List<MoveNode>> resultMap = tree.getDeepestNodesMap();
        TreeMap<Float, Move> treeMap = new TreeMap();
        for (Map.Entry<DenseMatrix64F, List<MoveNode>> entry : resultMap.entrySet()) {
            DenseMatrix64F key = entry.getKey();
            TreeMap<Float, MoveNode> treeMapEnemy = new TreeMap();
            for (MoveNode moveNode : entry.getValue()) {
                DenseMatrix64F temp = MoveUtils1.getJumian(key, moveNode.current);
                float scoreEneny = ScoreUtil.getScore(temp, !isGun);
                treeMapEnemy.put(scoreEneny + random.nextInt(10), moveNode);
            }

            float keyScore = ScoreUtil.getScore(key, isGun);
            Move subRoot = treeMapEnemy.lastEntry().getValue().root;
            treeMap.put(subRoot.score + keyScore - treeMapEnemy.lastKey(), treeMapEnemy.lastEntry().getValue().root);

        }

        Move bestMove = treeMap.lastEntry().getValue();
        tree.destory();
        tree = null;
        return bestMove;

    }


    public static Move bestMove2(Room1 room, int times, boolean isGun) {

        return getMax(room.qipan, times, times, isGun, isGun, null).root;

    }

    public static MoveNode getMax2(DenseMatrix64F qipan, int deep, int initDeep, boolean isGun, boolean init, Move root) {
        Random random = new Random();
        List<MoveNode> avalMoves = MoveUtils1.getAvliavleMoveList(qipan, isGun, root);
        if (deep == initDeep - 1 && avalMoves.size() <= 0 && isGun != init) {
            Move end = new Move(-1, -1, -1, -1, !isGun);
            end.score = Float.MAX_VALUE;
            return new MoveNode(root, end);
        }
        if (avalMoves.size() <= 0) {
            Move end = new Move(-1, -1, -1, -1, !isGun);
            if (isGun == init) {
                root.score -= 100000;
            } else {
                root.score += 100000;
            }

            return new MoveNode(root, end);
        }
        if (deep <= 0 ) {
            return getMoveNode(qipan, init,isGun, random, avalMoves, initDeep);
        }
        if(isGun == init) {
            return avalMoves.stream().map(node -> getMax(getJumian(qipan, node.current), deep - 1, initDeep, !isGun, init, node.root))
                    .filter(no->no!=null).max(new TreeCompartor()).orElse(null);
        }else {
            return avalMoves.stream().map(node -> getMax(getJumian(qipan, node.current), deep - 1, initDeep, !isGun, init, node.root))
                    .filter(no->no!=null).min(new TreeCompartor()).orElse(null);
        }
    }

    public static MoveNode getMax(DenseMatrix64F qipan, int deep, int initDeep, boolean isGun, boolean init, Move root) {
        Random random = new Random();
        List<MoveNode> avalMoves = MoveUtils1.getAvliavleMoveList(qipan, isGun, root);
        if (deep == initDeep - 1 && avalMoves.size() <= 0 && isGun != init) {
            Move end = new Move(-1, -1, -1, -1, !isGun);
            end.score = Float.MAX_VALUE;
            return new MoveNode(root, end);
        }
        if (avalMoves.size() <= 0) {
            Move end = new Move(-1, -1, -1, -1, !isGun);
            if (isGun == init) {
                root.score -= 100000;
            } else {
                root.score += 100000;
            }

            return new MoveNode(root, end);
        }
        if (deep <= 0 ) {
            return getMoveNode(qipan, init,isGun, random, avalMoves, initDeep);
        }
        if(isGun == init) {
           return avalMoves.stream().map(node -> getMax(getJumian(qipan, node.current), deep - 1, initDeep, !isGun, init, node.root))
                   .filter(no->no!=null).max(new TreeCompartor()).orElse(null);
        }else {
            return avalMoves.stream().map(node -> getMax(getJumian(qipan, node.current), deep - 1, initDeep, !isGun, init, node.root))
                    .filter(no->no!=null).min(new TreeCompartor()).orElse(null);
        }
    }

    private static MoveNode getMoveNode(DenseMatrix64F qipan, boolean init, boolean isGun, Random random, List<MoveNode> avalMoves, int deep) {
        TreeMap<Float, MoveNode> treeMapMe = new TreeMap();
        for (MoveNode moveNode : avalMoves) {
            DenseMatrix64F temp = MoveUtils1.getJumian(qipan, moveNode.current);
            float scoreMe = ScoreUtil.getScore(temp, init);
            treeMapMe.putIfAbsent(scoreMe + random.nextInt(10), moveNode);
        }

        MoveNode result = treeMapMe.firstEntry().getValue();
        result.current.score = treeMapMe.firstEntry().getValue().root.score + (init == isGun ? treeMapMe.lastEntry():treeMapMe.firstEntry()).getKey();
        return result;
    }

    private static MoveNode getMoveNode2(DenseMatrix64F qipan, boolean init, Random random, List<MoveNode> avalMoves) {
        float keyScore = ScoreUtil.getScore(qipan, init);
        TreeMap<Float, MoveNode> treeMapEnemy = new TreeMap();
        TreeMap<Float, MoveNode> treeMapMe = new TreeMap();
        for (MoveNode moveNode : avalMoves) {
            DenseMatrix64F temp = MoveUtils1.getJumian(qipan, moveNode.current);
            float scoreEneny = ScoreUtil.getScore(temp, !init);
            treeMapEnemy.put(scoreEneny, moveNode);
            float scoreMe = ScoreUtil.getScore(temp, init);
            treeMapMe.putIfAbsent(scoreMe + random.nextInt(5), moveNode);
        }

        MoveNode result = treeMapEnemy.lastEntry().getValue();
        result.current.score = (treeMapEnemy.lastEntry().getValue().root.score) * 4 + keyScore - treeMapEnemy.lastEntry().getKey() ;
//        result.current.score = keyScore - treeMapEnemy.lastEntry().getKey();

        return result;
    }


    public static DenseMatrix64F getJumian(DenseMatrix64F qipan, Move initMove) {
        if (initMove.isGun) {
            qipan = GunAlg.getJumianByGunMove(qipan, initMove);
        } else {
            qipan = SoldiersAlg.getJumianBySoldierMove(qipan, initMove);
        }
        return qipan;
    }

    public static List<MoveNode> getEnemyMoveList(DenseMatrix64F qipan, Move root, Move initMove) {
        List<MoveNode> result = new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        qipan = getJumian(qipan, initMove);

        if (initMove.isGun) {
            moves = SoldiersAlg.getAvalibleSoldiersMoves(qipan);
        } else {
            moves = GunAlg.getAvalibleGunMoves(qipan);
        }
        for (Move move : moves) {
            result.add(new MoveNode(root, move));
        }
        return result;

    }

    public static List<MoveNode> getAvliavleMoveList(DenseMatrix64F qipan, boolean isGun) {
        List<MoveNode> result = new ArrayList<>();
        List<Move> moves = new ArrayList<>();

        if (!isGun) {
            moves = SoldiersAlg.getAvalibleSoldiersMoves(qipan);
        } else {
            moves = GunAlg.getAvalibleGunMoves(qipan);
        }
        for (Move move : moves) {
            move.score = ScoreUtil.getScore(getJumian(qipan, move), isGun);
            result.add(new MoveNode(move, move));
        }
        return result;
    }

    public static List<MoveNode> getAvliavleMoveList(DenseMatrix64F qipan, boolean isGun, Move root) {
        List<MoveNode> result = new ArrayList<>();
        List<Move> moves = new ArrayList<>();

        if (!isGun) {
            moves = SoldiersAlg.getAvalibleSoldiersMoves(qipan);
        } else {
            moves = GunAlg.getAvalibleGunMoves(qipan);
        }
        for (Move move : moves) {
            if (root == null) {
                move.score = ScoreUtil.getScore(getJumian(qipan, move), isGun);
            }
            result.add(new MoveNode(root == null ? move : root, move));
        }
        return result;
    }

}
