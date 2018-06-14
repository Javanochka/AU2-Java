package ru.spbau.nikiforovskaya.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Tic-tac-toe desktop application.
 * Supports playing single (with a bot) or with a friend (hot seat).
 * Two levels of bots.
 * You can see statistics of the current session.
 */
public class Main extends Application{

    /**
     * Initialize the work of application.
     * @param primaryStage a stage, where to put content
     * @throws Exception if something went wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setMinHeight(240);
        primaryStage.setMinWidth(460);

        MainController.initialize(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
