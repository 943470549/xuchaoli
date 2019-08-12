package krq;

import org.ejml.data.DenseMatrix64F;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private DenseMatrix64F dm ;
    public static final int GUN = -1;
    public static final int SOLDIER = 1;
    public static final int SPACE = 0;

    private int gunCount = 2;
    private int soldierCount = 16;
    private int gunMoveCount = 4;
    private int soldierMoveCount = 10;

    private Zuobiao gun1 = new Zuobiao(5, 2);
    private Zuobiao gun2 = new Zuobiao(5, 3);

    public ChessBoard(DenseMatrix64F dm) {
        this.dm = dm;
    }
    public ChessBoard() {
        this.dm = new DenseMatrix64F(6, 6);
    }

    public void init() {
        dm.set(0, 1, SOLDIER);
        dm.set(0, 2, SOLDIER);
        dm.set(0, 3, SOLDIER);
        dm.set(0, 4, SOLDIER);
        dm.set(1, 0, SOLDIER);
        dm.set(1, 1, SOLDIER);
        dm.set(1, 2, SOLDIER);
        dm.set(1, 3, SOLDIER);
        dm.set(1, 4, SOLDIER);
        dm.set(1, 5, SOLDIER);
        dm.set(2, 0, SOLDIER);
        dm.set(2, 1, SOLDIER);
        dm.set(2, 2, SOLDIER);
        dm.set(2, 3, SOLDIER);
        dm.set(2, 4, SOLDIER);
        dm.set(2, 5, SOLDIER);
        dm.set(5, 2, GUN);
        dm.set(5, 3, GUN);
    }

    public ChessBoard move(Move move){
        if (move.isGun) {
            if (this.dm.get(move.end.x, move.end.y) == SOLDIER) {
                this.setSoldierCount(this.getSoldierCount() - 1);
            }
            if (this.getGun1().equal(move.begin)) {
                this.setGun1(move.end);
            } else {
                this.setGun2(move.end);
            }
        }

        try {
            this.dm.set(move.begin.x, move.begin.y, SPACE);
            this.dm.set(move.end.x, move.end.y, move.isGun ? GUN : SOLDIER);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.gunMoveCount = getGunCanMoves().size();
        return this;
    }

    public ChessBoard moveNewBoard(Move move) {
        ChessBoard board = this.copy();
        return board.move(move);
    }

    public List<Move> getCanMoves(boolean isGun) {
        return isGun ? getGunCanMoves() : getSoldierCanMoves();
    }

    public List<Move> getGunCanMoves() {
        List<Move> moves = new ArrayList<>();

        moves.addAll(getEveryAvalibleGunMoves(this.dm,this.gun1.x, this.gun1.y));
        moves.addAll(getEveryAvalibleGunMoves(this.dm,this.gun2.x, this.gun2.y));
        return moves;
    }

    public List<Move> getSoldierCanMoves() {
        List<Move> moves = new ArrayList<>();
        int x = 0;
        for (int i = 0; i < this.dm.getNumRows(); i++) {
            for (int j = 0; j < this.dm.getNumCols(); j++) {
                if (this.dm.get(i, j) == SOLDIER) {
                    moves.addAll(getEverySoldierMoves(this.dm, i, j));
                    x++;
                    if (x >= 16) {
                        return moves;
                    }
                }
            }
        }

        return moves;
    }

    public List<Move> getEverySoldierMoves(DenseMatrix64F qipan, int x1, int y1) {
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


    public  List<Move> getEveryAvalibleGunMoves(DenseMatrix64F qipan, int x1, int y1) {
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

    public void printChessBoard(){
        System.out.println("-----------------");
        for(int i =0 ; i< this.dm.numRows; i++){
            for(int j =0 ; j< this.dm.numCols; j++){
                double value = this.dm.get(i,j);
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
        if(this.getGunMoveCount() == 0){
            System.out.println("----------------小兵胜利"+i+"---------------");
            return true;
        }
        if(this.getSoldierCount() <= 3){
            System.out.println("----------------大炮胜利"+i+"---------------");
            return true;
        }
        return false;
    }

    public ChessBoard copy() {
        ChessBoard board = new ChessBoard(this.dm.copy());
        board.setGunMoveCount(this.gunMoveCount);
        board.setSoldierCount(this.soldierCount);
        board.setGun1(new Zuobiao(this.gun1.getX(), this.gun1.getY()));
        board.setGun2(new Zuobiao(this.gun2.getX(), this.gun2.getY()));
        return board;
    }

    public int getSoldierCount() {
        return soldierCount;
    }

    public void setSoldierCount(int soldierCount) {
        this.soldierCount = soldierCount;
    }

    public int getGunMoveCount() {
        return gunMoveCount;
    }

    public void setGunMoveCount(int gunMoveCount) {
        this.gunMoveCount = gunMoveCount;
    }

    public Zuobiao getGun1() {
        return gun1;
    }

    public void setGun1(Zuobiao gun1) {
        this.gun1 = gun1;
    }

    public Zuobiao getGun2() {
        return gun2;
    }

    public void setGun2(Zuobiao gun2) {
        this.gun2 = gun2;
    }
}
