package ru.spbau.nikiforovskaya.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import ru.spbau.nikiforovskaya.logger.Logger;
import ru.spbau.nikiforovskaya.logic.CellState;
import ru.spbau.nikiforovskaya.logic.FieldFunctions;
import ru.spbau.nikiforovskaya.logic.GameResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Controller of net player game.
 * Handles the actions during the game.
 */
public class NetPlayerController {

    @FXML private Text gameLog;
    @FXML private GridPane gameField;
    @FXML private TextField inputUser;
    @FXML private Text textInfo;
    @FXML private RadioButton xChosen;

    private int port = 13000;

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

    private volatile GameResult gameResult;
    private boolean userTurn;
    private DataInputStream input;
    private DataOutputStream output;

    private boolean userX;
    private Button[][] buttons;
    private CellState[][] field;

    /**
     * Starts game
     * @throws IOException, if error while server-client interaction occurred.
     */
    @FXML
    public void startGame() throws IOException {
        userX = xChosen.isSelected();
        buttons = new Button[3][3];
        field = new CellState[3][3];
        gameResult = GameResult.PROCESS;
        userTurn = userX;
        if (userX) { //I am server
            ServerSocket server;
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                gameLog.setText("Didn't manage to start server =(");
                return;
            }
            server.setSoTimeout(5000);
            try (Socket friend = server.accept()) {
                input = new DataInputStream(friend.getInputStream());
                output = new DataOutputStream(friend.getOutputStream());

                initField();

                Thread runner = new Thread(() -> {
                    while (true) {
                        if (userTurn) {
                            continue;
                        }
                        int i = 0, j = 0;
                        try {
                            input = new DataInputStream(friend.getInputStream());
                            i = input.readInt();
                            j = input.readInt();
                        } catch (IOException e) {
                            gameLog.setText("Error=(");
                        }

                        if (i == -1) {
                            break;
                        }
                        field[i][j] = !userX ? CellState.IS_X : CellState.IS_O;
                        final int fi = i;
                        final int fj = j;
                        Platform.runLater(() -> {
                                    if (field[fi][fj] == CellState.IS_X) {
                                        buttons[fi][fj].setBackground(new Background(xImage));
                                    } else {
                                        buttons[fi][fj].setBackground(new Background(oImage));
                                    }
                                });
                        gameResult = FieldFunctions.checkGameResult(field);
                        userTurn ^= true;
                    }
                });
                runner.start();

            } catch (IOException e) {
                gameLog.setText("Error while accepting friend.");
                return;
            } finally {
                gameLog.setText("Disconnected.");
            }
        } else {
            String hostname = inputUser.getText();
            try {
                Socket friend = new Socket(hostname, port);
                output = new DataOutputStream(friend.getOutputStream());
                initField();

                Thread runner = new Thread(() -> {
                    while (true) {
                        if (userTurn) {
                            continue;
                        }
                        int i = 0, j = 0;
                        try {
                            input = new DataInputStream(friend.getInputStream());
                            i = input.readInt();
                            j = input.readInt();
                        } catch (IOException e) {
                           // gameLog.setText("Error=(");
                            continue;
                        }

                        if (i == -1) {
                            break;
                        }
                        field[i][j] = !userX ? CellState.IS_X : CellState.IS_O;
                        final int fi = i;
                        final int fj = j;
                        Platform.runLater(() -> {
                            if (field[fi][fj] == CellState.IS_X) {
                                buttons[fi][fj].setBackground(new Background(xImage));
                            } else {
                                buttons[fi][fj].setBackground(new Background(oImage));
                            }
                        });
                        gameResult = FieldFunctions.checkGameResult(field);
                        userTurn ^= true;
                    }
                });
                runner.start();

            } catch (IOException e) {
                gameLog.setText("Error while accepting friend.");
                return;
            }
        }
    }

    private void initField() {
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
                            try {
                                output.writeInt(i);
                                output.writeInt(j);
                            } catch (IOException e) {
                                gameLog.setText("Error while sending turn");
                                e.printStackTrace();
                            }

                            if (field[i][j] == CellState.IS_X) {
                                buttons[i][j].setBackground(new Background(xImage));
                            } else {
                                buttons[i][j].setBackground(new Background(oImage));
                            }
                            userTurn ^= true;
                            gameResult = FieldFunctions.checkGameResult(field);
                            try {
                                writeGameResult(gameResult, userX);
                            } catch (IOException e) {
                                gameLog.setText("Error while sending game result");
                            }
                        }
                    }
                });
                gameField.add(buttons[i][j], i, j);
            }
        }
    }

    /**
     * Returns to main menu
     * @throws IOException if couldn't find resource file
     */
    @FXML
    public void backToMainMenu() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(layout, MainController.getPrimaryStage().getScene().getWidth(),
                MainController.getPrimaryStage().getScene().getHeight());
        MainController.getPrimaryStage().setScene(scene);
    }

    private void writeGameResult(GameResult gameResult, boolean userX) throws IOException {
        switch (gameResult) {
            case O_WINS: {
                gameLog.setText("O wins!");
                output.writeInt(-1);
                output.writeInt(-1);
               // Logger.addRecord("single player",
                //        easy ? "easy" : "hard", userX ? "X" : "O", "O won");
                return;
            }
            case X_WINS: {
                gameLog.setText("X wins!");
                output.writeInt(-1);
                output.writeInt(-1);
              //  Logger.addRecord("single player",
                //        easy ? "easy" : "hard", userX ? "X" : "O", "X won");
                return;
            }
            case DRAW: {
                gameLog.setText("Draw.");
                output.writeInt(-1);
                output.writeInt(-1);
              //  Logger.addRecord("single player",
               //         easy ? "easy" : "hard", userX ? "X" : "O", "Draw");
            }

        }
    }
}
