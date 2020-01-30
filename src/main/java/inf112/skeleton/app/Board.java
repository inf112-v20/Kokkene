package inf112.skeleton.app;

public class Board {

    public Pos gameBoard[][];

    public Board() {
        gameBoard = new Pos[12][12];
        for(int y = 0; y < gameBoard.length; y++) {
            for(int x = 0; x < gameBoard[y].length; x++) {
                gameBoard[x][y] = new Pos(x,y);
            }
        }
    }

}
