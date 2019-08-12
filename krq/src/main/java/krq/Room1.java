package krq;

import org.ejml.data.DenseMatrix64F;

public class Room1 {
    public static final int GUN = -1;
    public static final int SOLDIER = 1;
    public static final int SPACE = 0;

    int status;  // 0 未准备  1 开始  2 游戏中 3 游戏结束
    int soldierCount = 16;
    String soldierAccount;
    String gunAccount;

    int gunMoves = 0;
    int soldMoves = 0;
    Move [] soldLastMoves = new Move[4];
    Move [] gunLastMoves = new Move[4];


    DenseMatrix64F qipan = new DenseMatrix64F(6, 6);

    public void init() {
        qipan = originQipan.copy();
    }

    boolean checkRepeat(Move move){
        int count = 0;
        Move [] last = move.isGun ? gunLastMoves : soldLastMoves;
        for(Move m: last){
            if(m == null){continue;}
            if(m.begin.equal(move.begin) && m.end.equal(move.end)){
                count ++;
            }
        }
        return count >= last.length/2;
    }

    public void move(Move move){
        qipan.set(move.begin.x, move.begin.y, SPACE);
        qipan.set(move.end.x, move.end.y, move.isGun ? GUN: SOLDIER);
        if(move.isGun){
            gunLastMoves[gunMoves%gunLastMoves.length] = move;
            gunMoves++;
        }else {
            soldLastMoves[soldMoves%soldLastMoves.length] = move;
            soldMoves++;
        }
        printMove(move);
    }


    public static final DenseMatrix64F originQipan = new DenseMatrix64F(6, 6);

    static {
        originQipan.set(0, 1, SOLDIER);
        originQipan.set(0, 2, SOLDIER);
        originQipan.set(0, 3, SOLDIER);
        originQipan.set(0, 4, SOLDIER);
        originQipan.set(1, 0, SOLDIER);
        originQipan.set(1, 1, SOLDIER);
        originQipan.set(1, 2, SOLDIER);
        originQipan.set(1, 3, SOLDIER);
        originQipan.set(1, 4, SOLDIER);
        originQipan.set(1, 5, SOLDIER);
        originQipan.set(2, 0, SOLDIER);
        originQipan.set(2, 1, SOLDIER);
        originQipan.set(2, 2, SOLDIER);
        originQipan.set(2, 3, SOLDIER);
        originQipan.set(2, 4, SOLDIER);
        originQipan.set(2, 5, SOLDIER);
        originQipan.set(5, 2, GUN);
        originQipan.set(5, 3, GUN);
    }

    public void printQipan(){
        System.out.println("-----------------");
        for(int i =0 ; i< qipan.numRows; i++){
            for(int j =0 ; j< qipan.numCols; j++){
                double value = qipan.get(i,j);
                String str = "";
                if(value == SOLDIER){str = "兵";}
                if(value == SPACE){str = "__";}
                if(value == GUN){str = "炮";}
                System.out.print(str+ " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }
    public void printMove(Move move){
        System.out.println("------move----");
        System.out.println((move.isGun ? "炮" : "兵")+(move.begin.x +1)+","+(move.begin.y+1)+"->"+(move.end.x+1)+","+(move.end.y+1));
        System.out.println("-------move-----");
    }

    public boolean checkOver(int i){
        int gunCanmove = ScoreUtil.getAvalibleGunMoves(this.qipan);
        if(gunCanmove == 0){
            System.out.println("----------------小兵胜利"+i+"---------------");
            return true;
        }
        int soldCount = ScoreUtil.getSoldierCount(this.qipan);
        if(soldCount <= 3){
            System.out.println("----------------大炮胜利"+i+"---------------");
            return true;
        }
        return false;
    }

    public static void main(String args[]) throws Exception{
        Room1 room = new Room1();
        room.init();
        long t1 = System.nanoTime();
        for(int i =0 ;i <200; i++){

            Move move1 = MoveUtils1.bestMove2(room,  2,true);

            room.move(move1);

            room.printQipan();

            if(room.checkOver(i)){
                break;
            }

            Move move2 = MoveUtils1.bestMove2(room,6, false);

            room.move(move2);
            room.printQipan();


            if(room.checkOver(i)){
                break;
            }

        }
        long t2 = System.nanoTime();
        System.out.println((t2-t1)/1000000);


    }
}
