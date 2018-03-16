package ru.spbau.nikiforovskaya.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import ru.spbau.nikiforovskaya.logger.Logger;
import ru.spbau.nikiforovskaya.logic.CellState;
import ru.spbau.nikiforovskaya.logic.FieldFunctions;
import ru.spbau.nikiforovskaya.logic.GameResult;

import java.io.IOException;

/**
 * Controller of the hot seat game.
 * Handles with actions happening during the game.
 */
public class HotSeatController {


    @FXML private Text gameLog;
    @FXML private GridPane gameField;
    private boolean xTurn;
    private GameResult gameResult;

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

    /** Starts hot seat game. */
    @FXML
    public void startHotSeatGame() {
        gameLog.setText("");
        Button[][] buttons = new Button[3][3];
        CellState[][] field = new CellState[3][3];
        gameResult = GameResult.PROCESS;
        xTurn = true;
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
                        if (!gameResult.equals(GameResult.PROCESS)) {
                            return;
                        }
                        gameLog.setText("");
                        if (!field[i][j].equals(CellState.EMPTY)) {
                            gameLog.setText("This button is pressed. Try another one.");
                            return;
                        }
                        field[i][j] = xTurn ? CellState.IS_X : CellState.IS_O;
                        if (field[i][j].equals(CellState.IS_X)) {
                            buttons[i][j].setBackground(new Background(xImage));
                        } else {
                            buttons[i][j].setBackground(new Background(oImage));
                        }
                        xTurn ^= true;
                        gameResult = FieldFunctions.checkGameResult(field);
                        writeGameResult(gameResult);
                    }
                });
                gameField.add(buttons[i][j], i, j);
            }
        }
    }

    private void writeGameResult(GameResult gameResult) {
        switch (gameResult) {
            case O_WINS: {
                gameLog.setText("O wins!");
                Logger.addRecord("hot seat", "-","-", "O won");
                return;
            }
            case X_WINS: {
                gameLog.setText("X wins!");
                Logger.addRecord("hot seat", "-", "-", "X won");
                return;
            }
            case DRAW: {
                gameLog.setText("Draw.");
                Logger.addRecord("hot seat", "-","-", "Draw");
            }

        }
    }

    /**
     * Returns to main menu scene.
     * @throws IOException if something went wrong.
     */
    @FXML
    public void backToMainMenu() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(layout, MainController.getPrimaryStage().getScene().getWidth(),
                MainController.getPrimaryStage().getScene().getHeight());
        MainController.getPrimaryStage().setScene(scene);
    }
}
