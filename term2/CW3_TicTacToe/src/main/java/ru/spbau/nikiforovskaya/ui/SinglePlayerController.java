package ru.spbau.nikiforovskaya.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ru.spbau.nikiforovskaya.logger.Logger;
import ru.spbau.nikiforovskaya.logic.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import ru.spbau.nikiforovskaya.logic.BotFactory;

import java.io.IOException;

/**
 * Controller of single player game.
 * Handles the actions during the game.
 */
public class SinglePlayerController {

    @FXML private RadioButton xChosen;
    @FXML private RadioButton easyChosen;
    @FXML private GridPane gameField;
    @FXML private Text gameLog;

    private static final BackgroundSize forImage
            = new BackgroundSize(100, 100, true,
            true, true, false);

    private final BackgroundImage xImage = new BackgroundImage(
            new Image(getClass().getResourceAsStream("/x_pic.png")),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, forImage);
    private final BackgroundImage oImage = new BackgroundImage(
            new Image(getClass().getResourceAsStream("/o_pic.png")),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, forImage);

    /**
     * Returns to main menu scene.
     * @throws IOException if something went wrong
     */
    @FXML
    public void backToMainMenu() throws IOException {
        if (bot != null) {
            bot.interrupt();
        }
        Parent layout = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(layout, MainController.getPrimaryStage().getScene().getWidth(),
                MainController.getPrimaryStage().getScene().getHeight());
        MainController.getPrimaryStage().setScene(scene);
    }

    private boolean userTurn;
    private volatile GameResult gameResult;
    private Thread bot;

    /** Starts a single game process. */
    @FXML
    public void startSingleGame() {
        if (bot != null) {
            bot.interrupt();
        }
        Button[][] buttons = new Button[3][3];
        CellState[][] field = new CellState[3][3];
        boolean userX = xChosen.isSelected();
        boolean userEasy = easyChosen.isSelected();
        userTurn = userX;
        gameResult = GameResult.PROCESS;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = CellState.EMPTY;
                buttons[i][j] = new Button("");
                buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                int fi = i;
                int fj = j;
                buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {

                    final int i = fi;
                    final int j = fj;

                    @Override
                    public void handle(ActionEvent event) {
                        synchronized (buttons) {
                            if (gameResult != GameResult.PROCESS) {
                                return;
                            }
                            gameLog.setText("");
                            if (!userTurn) {
                                gameLog.setText("Sorry, it's not your turn");
                                return;
                            }
                            if (field[i][j] != CellState.EMPTY) {
                                gameLog.setText("This button is pressed. Try another one.");
                                return;
                            }
                            field[i][j] = userX ? CellState.IS_X : CellState.IS_O;
                            if (field[i][j] == CellState.IS_X) {
                                buttons[i][j].setBackground(new Background(xImage));
                            } else {
                                buttons[i][j].setBackground(new Background(oImage));
                            }
                            userTurn ^= true;
                            gameResult = FieldFunctions.checkGameResult(field);
                            writeGameResult(gameResult, userX, userEasy);
                        }
                    }
                });
                gameField.add(buttons[i][j], i, j);
            }
        }

        Bot botPlayer = userEasy ? BotFactory.getEasyBot() : BotFactory.getHardBot();
        bot = new Thread(() -> {
            while (!Thread.interrupted() && gameResult == GameResult.PROCESS) {
                synchronized (buttons) {
                    if (userTurn || gameResult != GameResult.PROCESS) {
                        Thread.yield();
                        continue;
                    }
                    gameLog.setText("");
                    Pair<Integer, Integer> turn = botPlayer.makeMove(field, !userX);
                    int i = turn.getKey();
                    int j = turn.getValue();
                    field[i][j] = !userX ? CellState.IS_X : CellState.IS_O;
                    Platform.runLater(() -> {
                        if (field[i][j] == CellState.IS_X) {
                            buttons[i][j].setBackground(new Background(xImage));
                        } else {
                            buttons[i][j].setBackground(new Background(oImage));
                        }
                    });
                    userTurn ^= true;
                    gameResult = FieldFunctions.checkGameResult(field);
                    writeGameResult(gameResult, userX, userEasy);
                }
            }
        });
        bot.start();
    }

    private void writeGameResult(GameResult gameResult, boolean userX, boolean easy) {
        switch (gameResult) {
            case O_WINS: {
                gameLog.setText("O wins!");
                Logger.addRecord("single player",
                        easy ? "easy" : "hard", userX ? "X" : "O", "O won");
                return;
            }
            case X_WINS: {
                gameLog.setText("X wins!");
                Logger.addRecord("single player",
                        easy ? "easy" : "hard", userX ? "X" : "O", "X won");
                return;
            }
            case DRAW: {
                gameLog.setText("Draw.");
                Logger.addRecord("single player",
                        easy ? "easy" : "hard", userX ? "X" : "O", "Draw");
            }

        }
    }
}
