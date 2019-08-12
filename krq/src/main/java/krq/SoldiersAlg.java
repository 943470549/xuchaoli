package krq;


import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;
import java.util.List;

import static krq.Room1.SOLDIER;
import static krq.Room1.SPACE;


public class SoldiersAlg {


    public static DenseMatrix64F getJumianBySoldierMove(DenseMatrix64F qipan, List<Move> moves) {
        DenseMatrix64F temp = qipan.copy();
        for (Move move : moves) {
            temp.set(move.begin.x, move.begin.y, SPACE);
            temp.set(move.end.x, move.end.y, SOLDIER);
        }
        return temp;
    }

    public static DenseMatrix64F getJumianBySoldierMove(DenseMatrix64F qipan, Move move) {
        DenseMatrix64F temp = qipan.copy();
        temp.set(move.begin.x, move.begin.y, SPACE);
        temp.set(move.end.x, move.end.y, SOLDIER);
        return temp;
    }


    public static List<Move> getAvalibleSoldiersMoves(DenseMatrix64F qipan) {
        List<Move> moves = new ArrayList<>();
        int x = 0;
        for (int i = 0; i < qipan.getNumRows(); i++) {
            for (int j = 0; j < qipan.getNumCols(); j++) {
                if (qipan.get(i, j) == SOLDIER) {
                    moves.addAll(getEverySoldierMoves(qipan, i, j));
                    x++;
                    if (x >= 16) {
                        return moves;
                    }
                }
            }
        }

        return moves;
    }

    public static List<Move> getEverySoldierMoves(DenseMatrix64F qipan, int x1, int y1) {
        List<Move> moves = new ArrayList<>();
        if (x1 > 0 && qipan.get(x1 - 1, y1) == SPACE) {
            moves.add(new Move(x1, y1, x1 - 1, y1, false));
        }

        if (x1 + 1 < qipan.numCols && qipan.get(x1 + 1, y1) == SPACE) {
            moves.add(new Move(x1, y1, x1 + 1, y1, false));
        }

        if (y1 > 0 && qipan.get(x1, y1 - 1) == SPACE) {
            moves.add(new Move(x1, y1, x1, y1 - 1, false));
        }

        if (y1 + 1 < qipan.numCols && qipan.get(x1, y1 + 1) == SPACE) {
            moves.add(new Move(x1, y1, x1, y1 + 1, false));
        }

        return moves;
    }



}

