package krq;

import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;
import java.util.List;

import static krq.Room1.*;

public class ScoreUtil {



    public static float getScore(DenseMatrix64F qipan, boolean isGun){
        int gunMoves = getAvalibleGunMoves(qipan);
        int soldierCount = getSoldierCount(qipan);
        if(isGun){
            return gunMoves*20 - soldierCount*50;
        }else {
            return soldierCount*20- gunMoves*100;
        }
    }

    public static float getScore(ChessBoard qipan, boolean isGun){
        int gunMoves = qipan.getGunMoveCount();
        int soldierCount = qipan.getSoldierCount();
        if(isGun){
            return gunMoves*20 - soldierCount*150;
        }else {
            return soldierCount*120- gunMoves*20;
        }
    }




    public static int getAvalibleGunMoves(DenseMatrix64F qipan){
        List<Move> moves = GunAlg.getAvalibleGunMoves(qipan);
        return moves == null ? 0 : moves.size();
    }

    public static int getSoldierCount(DenseMatrix64F qipan){
        int count = 0;
        for(int i=0; i< qipan.numRows; i++){
            for(int j=0; j< qipan.numRows; j++){
                if(qipan.get(i, j) == SOLDIER){
                    count ++;
                }
            }
        }
        return count;
    }



    public static List<Zuobiao> getGunZuobiao(DenseMatrix64F qipan){
        List<Zuobiao> list = new ArrayList<>();
        for(int i =0 ; i< qipan.numRows; i++){
            for(int j =0 ; j< qipan.numCols; j++){
                if(qipan.get(i,j)==GUN){
                    list.add(new Zuobiao(i, j));
                    if(list.size() == 2){
                        return list;
                    }
                }
            }
        }
        return list;
    }
    public static DenseMatrix64F moveNewQipan(DenseMatrix64F qipan, Move move){
        qipan = qipan.copy();
        if(move != null){
            qipan.set(move.begin.x, move.begin.y, SPACE);
            qipan.set(move.end.x, move.end.y, move.isGun ? GUN: SOLDIER);
        }
        return qipan;
    }



}
