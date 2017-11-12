package sample.my_classes;

import sample.helpers.Configuration;
import sample.annotations.Defense;
import sample.annotations.Easy;
import sample.annotations.Hard;
import sample.annotations.Mid;
import sample.helpers.Pair;

import java.util.Random;

@Defense
public class StrategyOne {

    public StrategyOne() {
        System.out.println("STRATEGY: DEFENSE");
    }

    @Easy
    public Pair levelEasy(String[][] board) {
        System.out.println("LEVEL: EASY");

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(i, j);
                }
            }
        }
        return null;
    }

    @Mid
    public Pair levelMedium(String[][] board) {
        System.out.println("LEVEL: MID");

        Random rand = new Random();
        int randomX = rand.nextInt(board.length);
        int randomY = rand.nextInt(board.length);

        while(!board[randomX][randomY].equals(Configuration.EMPTY_BOARD_FIELD)){
            randomX = rand.nextInt(board.length);
            randomY = rand.nextInt(board.length);
        }

        return new Pair(randomX, randomY);
    }

    @Hard
    public Pair levelHard(String[][] board) {
        System.out.println("LEVEL: HARD");

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].equals(Configuration.PLAYER_MOVE)) {
//                    System.out.println("i: " + i + " j: " + j);
                    if (i + 1 <= board.length && board[i + 1][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i + 1, j);
                    }
                    if (i - 1 >= 0 && board[i - 1][j].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i - 1, j);
                    }
                    if (j + 1 <= board.length && board[i][j + 1].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i, j + 1);
                    }
                    if (j - 1 >= 0 && board[i][j - 1].equals(Configuration.EMPTY_BOARD_FIELD)) {
                        return new Pair(i, j - 1);
                    }
                }
            }
        }
        return null;
    }
}
