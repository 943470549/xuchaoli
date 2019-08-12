package krq;

import org.ejml.data.DenseMatrix64F;

public class Room {
    public static final int GUN = -1;
    public static final int SOLDIER = 1;
    public static final int SPACE = 0;

    int status;  // 0 未准备  1 开始  2 游戏中 3 游戏结束

    String soldierAccount;
    String gunAccount;

    public void init() {
        chessBoard.init();
    }

    public final ChessBoard chessBoard = new ChessBoard();





    public static void main(String args[]) throws Exception{
        Room room = new Room();
        room.init();
        long t1 = System.nanoTime();
        ChessBoard board = room.chessBoard;
        for(int i =0 ;i <200; i++){

            System.out.println("--------------start1----------");
            long time1 = System.nanoTime();
            Move move1 = MoveUtils3.bestMove(board,  6,true);
            System.out.println("--------------end1----------" + (System.nanoTime() - time1)/1000000);
            board.move(move1);
            board.printMove(move1);
            board.printChessBoard();

            if(board.checkOver(i)){
                break;
            }

            Move move2 = MoveUtils3.bestMove(board,4, false);
            if(move2.getEnd().x == -1){
                if(board.checkOver(i)){
                    System.out.println("sssss");
                }
                System.out.println("bbbbbb");
            }
            board.move(move2);
            board.printMove(move2);
            board.printChessBoard();



            if(board.checkOver(i)){
                break;
            }

        }
        long t2 = System.nanoTime();
        System.out.println((t2-t1)/1000000);


    }
}
