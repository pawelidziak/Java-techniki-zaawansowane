package sample.strategies;


import sample.annotations.Level;
import sample.annotations.Strategy;
import sample.helpers.Configuration;
import sample.helpers.Pair;

@Strategy(name = "ATTACK")
public class StrategyTwo {

    public StrategyTwo() {
        System.out.println("STRATEGY: ATTACK");
    }

    @Level(name = "EASY")
    public Pair easy(String[][] board) {
        System.out.println("LEVEL: EASY");

        int counter = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                // sprawdzam czy w danym wierszu nie ma pola gracza
                for (int x = 0; x < board.length; x++) {
                    if (board[i][x].equals(Configuration.PLAYER_MOVE))
                        counter++;
                }
                if (board.length - counter < 5 && i < board.length - 1) {
                    i++;
                    j = 0;
                    counter = 0;
                }

                if (board[i][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(i, j);
                }
            }
        }

        return firstEmpty(board);
    }

    @Level(name = "MID")
    public Pair medium(String[][] board) {
        System.out.println("LEVEL: MID");

        int x = board.length / 2;
        int y = board.length / 2;

        if (board[x][y].equals(Configuration.EMPTY_BOARD_FIELD))
            return new Pair(x, y);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (x + i <= board.length && board[x + i][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(x + i, j);
                }
                if (x - i >= 0 && board[x - i][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(x - i, j);
                }

            }
        }
        return firstEmpty(board);
    }

    @Level(name = "HARD")
    public Pair hard(String[][] board) {
        System.out.println("LEVEL: HARD");

        int x = board.length / 2;
        int y = board.length / 2;

        for (int i = x; i < board.length; i++) {
            for (int j = y; j < board[0].length; j++) {

                if (board[x][y].equals(Configuration.EMPTY_BOARD_FIELD))
                    return new Pair(x, y);

                if (board[i][j].equals(Configuration.COMPUTER_MOVE)) {

                    if (i + 1 < board.length && board[i + 1][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i + 1, j);
                    }
                    if (i - 1 > 0 && board[i - 1][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i - 1, j);
                    }

//                    if (j + 1 < board.length && board[i][j + 1].equals(Configuration.EMPTY_BOARD_FIELD)) {
//                        return new Pair(i, j + 1);
//                    }
//                    if (j - 1 > 0 && board[i][j - 1].equals(Configuration.EMPTY_BOARD_FIELD)) {
//                        return new Pair(i, j - 1);
//                    }
                }
            }
        }
        return firstEmpty(board);
    }

    private Pair firstEmpty(String[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(i, j);
                }
            }
        }
        return null;
    }
}
