package krq;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class MoveUtils {


    public static Move bestMove(ChessBoard chessBoard, int times, boolean isGun) {

        return getMax(chessBoard, times, times, isGun, isGun);

    }

    public static Move getMax(ChessBoard qipan, int deep, int initDeep, boolean isGun, boolean init) {
        Random random = new Random();
        List<Move> avalMoves = qipan.getCanMoves(isGun);
//        if (deep == initDeep - 1 && avalMoves.size() <= 0 && isGun != init) {
//            Move end = new Move(-1, -1, -1, -1, !isGun);
//            end.score = Float.MAX_VALUE;
//            return end;
//        }
        if (avalMoves.size() <= 0) {
            Move end = new Move(-1, -1, -1, -1, isGun);
            float scoreMe = isGun == init ? -10000 : 10000;
            end.score = scoreMe;
            return end;
        }
        if (deep <= 0) {
            return getMoveNode(qipan, init, isGun, random, avalMoves, initDeep);
        }

        float scoreA = Float.MAX_VALUE;
        float scoreB = -Float.MAX_VALUE;
        Move temp = null;
        for (Move move : avalMoves) {
            if (isGun == init) {
                Move next = getMax(qipan.moveNewBoard(move), deep - 1, initDeep, !isGun, init);
                if (next != null && next.score > scoreB) {
                    scoreB = next.score;
                    temp = move;
                    temp.score = scoreB;
                }
            } else {
                Move next = getMax(qipan.moveNewBoard(move), deep - 1, initDeep, !isGun, init);
                if (next != null && next.score < scoreA) {
                    scoreA = next.score;
                    temp = move;
                    temp.score = scoreA;
                }
            }
        }
        return temp;
//        if(isGun == init) {
//           return avalMoves.stream().map(node -> getMax(qipan.moveNewBoard(node), deep - 1, initDeep, !isGun, init))
//                   .filter(no->no!=null).max(new TreeMoveCompartor()).orElse(null);
//        }else {
//            return avalMoves.stream().map(node -> getMax(qipan.moveNewBoard(node), deep - 1, initDeep, !isGun, init))
//                    .filter(no->no!=null).min(new TreeMoveCompartor()).orElse(null);
//        }
    }

    private static Move getMoveNode(ChessBoard qipan, boolean init, boolean isGun, Random random, List<Move> avalMoves, int deep) {
        TreeMap<Float, Move> treeMapMe = new TreeMap();
        for (Move moveNode : avalMoves) {
            ChessBoard temp = qipan.moveNewBoard(moveNode);
            float scoreMe = ScoreUtil.getScore(temp, init);
            scoreMe += random.nextInt(10);
            moveNode.score = scoreMe;
            treeMapMe.putIfAbsent(scoreMe, moveNode);
        }

        Move result = (init == isGun ? treeMapMe.lastEntry() : treeMapMe.firstEntry()).getValue();
//        result.score = (init == isGun ? treeMapMe.lastEntry() : treeMapMe.firstEntry()).getKey();
        return result;
    }


}
