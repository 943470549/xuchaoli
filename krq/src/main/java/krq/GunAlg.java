package krq;


import org.ejml.data.DenseMatrix64F;

import java.util.*;

import static krq.Room1.*;

public class GunAlg {



    public static DenseMatrix64F getJumianByGunMove(DenseMatrix64F qipan, List<Move> moves) {
        DenseMatrix64F temp = qipan.copy();
        for (Move move : moves) {
            temp.set(move.begin.x, move.begin.y, SPACE);
            temp.set(move.end.x, move.end.y, GUN);
        }
        return temp;
    }

    public static DenseMatrix64F getJumianByGunMove(DenseMatrix64F qipan, Move move) {
        DenseMatrix64F temp = qipan.copy();

        temp.set(move.begin.x, move.begin.y, SPACE);
        temp.set(move.end.x, move.end.y, GUN);

        return temp;
    }


    public static List<Move> getAvalibleGunMoves(DenseMatrix64F qipan) {
        List<Move> moves = new ArrayList<>();
        int x = 0;
        for (int i = 0; i < qipan.getNumRows(); i++) {
            for (int j = 0; j < qipan.getNumCols(); j++) {
                if (qipan.get(i, j) == GUN) {
                    moves.addAll(getEveryAvalibleGunMoves(qipan, i, j));
                    x++;
                    if (x >= 2) {
                        return moves;
                    }
                }
            }
        }

        return moves;
    }

    public static List<Move> getEveryAvalibleGunMoves(DenseMatrix64F qipan, int x1, int y1) {
        List<Move> moves = new ArrayList<>();
        if (qipan.isInBounds(x1 - 1, y1) && qipan.get(x1 - 1, y1) == SPACE) {
            moves.add(new Move(x1, y1, x1 - 1, y1, true));
            if (qipan.isInBounds(x1 - 2, y1) && qipan.get(x1 - 2, y1) == SOLDIER) {
                moves.add(new Move(x1, y1, x1 - 2, y1, true));
            }
        }

        if (qipan.isInBounds(x1 + 1, y1) && qipan.get(x1 + 1, y1) == SPACE) {
            moves.add(new Move(x1, y1, x1 + 1, y1, true));
            if (qipan.isInBounds(x1 + 2, y1) && qipan.get(x1 + 2, y1) == SOLDIER) {
                moves.add(new Move(x1, y1, x1 + 2, y1, true));
            }
        }


        if (qipan.isInBounds(x1, y1 - 1) && qipan.get(x1, y1 - 1) == SPACE) {
            moves.add(new Move(x1, y1, x1, y1 - 1, true));
            if (qipan.isInBounds(x1, y1 - 2) && qipan.get(x1, y1 - 2) == SOLDIER) {
                moves.add(new Move(x1, y1, x1, y1 - 2, true));
            }
        }



        if (qipan.isInBounds(x1, y1 + 1) && qipan.get(x1, y1 + 1) == SPACE) {
            moves.add(new Move(x1, y1, x1, y1 + 1, true));
            if (qipan.isInBounds(x1, y1 + 2) && qipan.get(x1, y1 + 2) == SOLDIER) {
                moves.add(new Move(x1, y1, x1, y1 + 2, true));
            }
        }


        return moves;
    }

    public static void main(String args[]) {
        DenseMatrix64F qipan = Room1.originQipan.copy();


    }


}

