package sample.strategies;

import sample.annotations.*;
import sample.helpers.Configuration;
import sample.helpers.Pair;
import java.util.Random;

@Strategy(name = "DEFENSE")
public class StrategyOne {

    public StrategyOne() {
        System.out.println("STRATEGY: DEFENSE");
    }

    @Level(name="EASY")
    public Pair easy(String[][] board) {
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

    @Level(name="MID")
    public Pair medium(String[][] board) {
        System.out.println("MID");

        Random rand = new Random();
        int randomX = rand.nextInt(board.length);
        int randomY = rand.nextInt(board.length);

        while (!board[randomX][randomY].equals(Configuration.EMPTY_BOARD_FIELD)) {
            randomX = rand.nextInt(board.length);
            randomY = rand.nextInt(board.length);
        }

        return new Pair(randomX, randomY);
    }

    @Level(name="HARD")
    public Pair hard(String[][] board) {
        System.out.println("LEVEL: HARD");


        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if (board[i][j].equals(Configuration.PLAYER_MOVE)) {

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


    private Pair getNextBestDecision(String[][] board, String type) {
        Option bestOption = new Option(-1, -1, -1, -1);
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                Option newOption = getBestPointFor(board, x, y, type);
                if (newOption.score > bestOption.score) {
                    bestOption = newOption;
                } else if (newOption.score == bestOption.score && newOption.possibleScore > bestOption.possibleScore) {
                    bestOption = newOption;
                }
            }
        }

        if (bestOption.score == -1) {
            return getFreeRandomPoint(board);
        }

        return new Pair(bestOption.x, bestOption.y);
    }

    private Option getBestPointFor(String[][] board, int x, int y, String type) {
        if (!board[x][y].equals(Configuration.EMPTY_BOARD_FIELD)) {
            return new Option(-1, -1, -1, -1);
        }
        int strike = 4;
        // horizontal
        int horizontalScore = 0;
        int horizontalPossibleScores = 0;
        for (int i = x - 1, j = 0; i >= 0 && j != strike; i--, j++) {
            if (board[i][y].equals(type)) {
                horizontalScore++;
            } else if (board[i][y].equals(Configuration.EMPTY_BOARD_FIELD)) {
                horizontalPossibleScores++;
            } else {
                break;
            }

        }
        for (int i = x + 1, j = 0; i < board.length && j != strike; i++, j++) {
            if (board[i][y].equals(type)) {
                horizontalScore++;
            } else if (board[i][y].equals(Configuration.EMPTY_BOARD_FIELD)) {
                horizontalPossibleScores++;
            } else {
                break;
            }

        }
        int bestScore = -1;
        int bestPossibleScore = -1;

        if (horizontalScore > bestScore && horizontalScore + horizontalPossibleScores >= strike) {
            bestScore = horizontalScore;
            bestPossibleScore = horizontalPossibleScores;
        }

        // vertical
        int verticalScore = 0;
        int verticalPossibleScore = 0;
        for (int i = y - 1, j = 0; i >= 0 && j != strike; i--, j++) {
            if (board[x][i].equals(type)) {
                verticalScore++;
            } else if (board[x][i].equals(Configuration.EMPTY_BOARD_FIELD)) {
                verticalPossibleScore++;
            } else {
                break;
            }
        }
        for (int i = y + 1, j = 0; i < board.length && j != strike; i++, j++) {
            if (board[x][i].equals(type)) {
                verticalScore++;
            } else if (board[x][i].equals(Configuration.EMPTY_BOARD_FIELD)) {
                verticalPossibleScore++;
            } else {
                break;
            }
        }
        if (verticalScore > bestScore && verticalScore + verticalPossibleScore >= strike) {
            bestScore = verticalScore;
            bestPossibleScore = verticalPossibleScore;
        }

        // slant
        int slantScore = 0;
        int slantPossibleScore = 0;
        for (int xSlide = x - 1, ySlide = y - 1, j = 0; xSlide >= 0 && ySlide >= 0 && j != strike; xSlide--, ySlide--, j++) {
            if (board[xSlide][ySlide].equals(type)) {
                slantScore++;
            } else if (board[xSlide][ySlide].equals(Configuration.EMPTY_BOARD_FIELD)) {
                slantPossibleScore++;
            } else {
                break;
            }
        }
        for (int xSlide = x + 1, ySlide = y + 1, j = 0; xSlide < board.length && ySlide < board.length && j != strike; xSlide++, ySlide++, j++) {
            if (board[xSlide][ySlide].equals(type)) {
                slantScore++;
            } else if (board[xSlide][ySlide].equals(Configuration.EMPTY_BOARD_FIELD)) {
                slantPossibleScore++;
            } else {
                break;
            }
        }


        if (slantScore > bestScore && slantScore + slantPossibleScore >= strike) {
            bestScore = slantScore;
            bestPossibleScore = slantPossibleScore;
        }

        // slant2
        int slantScore2 = 0;
        int slantPossibleScore2 = 0;
        for (int xSlide = x + 1, ySlide = y - 1, j = 0; xSlide < board.length && ySlide >= 0 && j != strike; xSlide++, ySlide--, j++) {
            if (board[xSlide][ySlide].equals(type)) {
                slantScore2++;
            } else if (board[xSlide][ySlide].equals(Configuration.EMPTY_BOARD_FIELD)) {
                slantPossibleScore2++;
            } else {
                break;
            }
        }
        for (int xSlide = x - 1, ySlide = y + 1, j = 0; xSlide >= 0 && ySlide < board.length && j != strike; xSlide--, ySlide++, j++) {
            if (board[xSlide][ySlide].equals(type)) {
                slantScore2++;
            } else if (board[xSlide][ySlide].equals(Configuration.EMPTY_BOARD_FIELD)) {
                slantPossibleScore2++;
            } else {
                break;
            }
        }


        if (slantScore2 > bestScore && slantScore2 + slantPossibleScore2 >= strike) {
            bestScore = slantScore2;
            bestPossibleScore = slantPossibleScore2;
        }

        return new Option(x, y, bestScore, bestPossibleScore);
    }

    private Pair getFreeRandomPoint(String[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y].equals(Configuration.EMPTY_BOARD_FIELD)) {
                    return new Pair(x, y);
                }
            }
        }
        return  null;
    }

    private class Option {
        final int score;
        final int possibleScore;
        final int x;
        final int y;

        public Option(int x, int y, int score, int possibleScore) {
            this.x = x;
            this.y = y;
            this.score = score;
            this.possibleScore = possibleScore;
        }
    }

}
