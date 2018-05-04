package ru.spbau.nikiforovskaya.pairgame.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ru.spbau.nikiforovskaya.pairgame.logic.GameController;

import java.util.Optional;


/**
 * A game, where your goal is to find all pairs.
 */
public class Main extends Application{

    /**
     * Initialize the work of application.
     * @param primaryStage a stage, where to put content
     * @throws Exception if something went wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parameters args = getParameters();
        int n = Integer.parseInt(args.getUnnamed().get(0));
        if (n < 2 || n % 2 == 1) {
            System.out.println("Sorry, I work only with even number more than 2");
            return;
        }

        primaryStage.setTitle("Find all pairs!");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(300);

        Button[][] buttons = new Button[n][n];

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        for (int r = 0; r < n; r++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }

        for (int c = 0; c < n; c++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                buttons[r][c] = new Button();
                buttons[r][c].setMaxHeight(Double.MAX_VALUE);
                buttons[r][c].setMaxWidth(Double.MAX_VALUE);
                grid.add(buttons[r][c], c, r);
            }
        }
        grid.setMaxHeight(Double.MAX_VALUE);
        grid.setMaxWidth(Double.MAX_VALUE);
        primaryStage.setScene(new Scene(grid));
        primaryStage.show();

        GameController controller = new GameController(buttons);
        controller.startNewGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}