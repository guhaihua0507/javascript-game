package com.ghh.chess;

import com.ghh.common.game.Game;
import com.ghh.common.game.Player;

public class Gobang extends Game {
    private final static int LENGTH  = 10;
    private final static int HEIGHT  = 10;

    private int[][]          chesses = new int[HEIGHT][LENGTH];
    private int              stepNo  = 0;
    private Step             lastStep;
    private int              winner  = -1;

    public Gobang(Long id) {
        super(id);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                chesses[i][j] = -1;
            }
        }
    }

    /**
     * put a chess
     * 
     * @param id
     * @param x
     * @param y
     */
    public void putChess(Long id, int x, int y) {
        Player p = getPlayer(id);
        if (p == null) {
            return;
        }
        if (chesses[y][x] != -1) {
            return;
        }
        stepNo++;
        int playNo = p.getPlayNo();
        chesses[y][x] = playNo;

        /*
         * update stepNo
         */
        lastStep = new Step();
        lastStep.setPlayer(p);
        lastStep.setStepNo(stepNo);
        lastStep.setX(x);
        lastStep.setY(y);

        if (isGameOver()) {
            winner = lastStep.getPlayer().getPlayNo();
            resetGame();
        } else if (!hasMoreSpace()) {
            winner = -1;
            resetGame();
        }
    }

    private boolean isGameOver() {
        if (lastStep == null) {
            return false;
        }
        int x = lastStep.getX();
        int y = lastStep.getY();

        int length = 0;
        length = geComboChess(x, y, -1, 0) + geComboChess(x, y, 1, 0);
        if (length >= 6) {
            return true;
        }
        length = geComboChess(x, y, 0, -1) + geComboChess(x, y, 0, 1);
        if (length >= 6) {
            return true;
        }
        length = geComboChess(x, y, -1, -1) + geComboChess(x, y, 1, 1);
        if (length >= 6) {
            return true;
        }
        length = geComboChess(x, y, -1, 1) + geComboChess(x, y, 1, -1);
        if (length >= 6) {
            return true;
        }

        return false;
    }

    /**
     * check if has more blank
     * 
     * @return
     */
    private boolean hasMoreSpace() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                if (chesses[i][j] == -1) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getWinner() {
        return winner;
    }

    private int geComboChess(int x, int y, int stepX, int stepY) {
        int amount = 0;
        int value = chesses[y][x];
        if (value == -1) {
            return amount;
        }
        amount++;
        int nx = x;
        int ny = y;
        while (true) {
            nx = nx + stepX;
            ny = ny + stepY;
            if (nx < 0 || ny < 0 || nx > LENGTH - 1 || ny > LENGTH - 1) {
                return amount;
            }
            int nValue = chesses[ny][nx];
            if (nValue != value) {
                return amount;
            }
            amount++;
        }
    }

    @Override
    protected void startGame() {
        super.startGame();
        stepNo = 0;
        winner = -1;
    }

    @Override
    protected void resetGame() {
        super.resetGame();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < LENGTH; j++) {
                chesses[i][j] = -1;
            }
        }
    }

    public int getStepNo() {
        return stepNo;
    }

    public Step getLastStep() {
        return lastStep;
    }

    @Override
    protected int getPlayerLimit() {
        return 2;
    }
}
