package ru.spbau.nikiforovskaya.pairgame.logic;

import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.Random;

/**
 * A game controller.
 * Can run game several times.
 */
public class GameController {

    private int[][] answerField;
    private Button[][] field;

    private int lastOpenedI;
    private int lastOpenedJ;

    private int progress;

    /**
     * Create a game controller.
     *
     * @param field -- a field of buttons which to change.
     */
    public GameController(Button[][] field) {
        this.field = field;
        answerField = new int[field.length][field.length];

    }

    /** Starts new game. */
    public void startNewGame() {
        Random r = new Random();
        lastOpenedI = -1;
        lastOpenedJ = -1;
        progress = 0;
        int[] listOfPossible = new int[field.length * field.length];
        int size = field.length * field.length;
        for (int i = 0; i < field.length * field.length / 2; i++) {
            listOfPossible[2 * i] = i + 1;
            listOfPossible[2 * i + 1] = i + 1;
        }
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                int next = r.nextInt(size);
                answerField[i][j] = listOfPossible[next];
                listOfPossible[next] = listOfPossible[--size];
            }
        }
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j].setDisable(false);
                final int fi = i;
                final int fj = j;
                field[i][j].setOnMouseClicked(event -> {
                    try {
                        handleOpenedCard(fi, fj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private synchronized void handleOpenedCard(int i, int j) throws InterruptedException {
        field[i][j].setText(answerField[i][j] + "");
        if (lastOpenedI == -1) {
            lastOpenedI = i;
            lastOpenedJ = j;
            return;
        }
        Button lastOpened = field[lastOpenedI][lastOpenedJ];
        if (field[i][j] == lastOpened) {
            field[i][j].setText("");
            lastOpenedI = -1;
            lastOpenedJ = -1;
            return;
        }
        if (answerField[i][j] == answerField[lastOpenedI][lastOpenedJ]) {
            field[i][j].setDisable(true);
            lastOpened.setDisable(true);
            progress += 2;
            lastOpenedI = -1;
            lastOpenedJ = -1;
            return;
        }
        Thread change = new Thread(() -> {
            synchronized (this) {
                try {
                    wait(500);
                } catch (InterruptedException ignored) {

                }
                Platform.runLater(() -> {
                    field[i][j].setText("");
                    lastOpened.setText("");
                });
            }
        });
        change.start();

        lastOpenedI = -1;
        lastOpenedJ = -1;
    }

    public boolean isFinished() {
        return progress == field.length * field.length;
    }
}
