package ru.spbau.nikiforovskaya.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A controller of main scene.
 * Handles actions with menu.
 */
public class MainController {

    private static Stage primaryStage;

    /**
     * Initialize primary stage. Can be used only in ui.
     * @param primaryStage the main stage
     */
    static void initialize(Stage primaryStage) {
        MainController.primaryStage = primaryStage;
    }

    /**
     * Returns primary stage of application.
     * @return primary stage of application.
     */
    static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Loads single player game.
     * @throws IOException if something went wrong.
     */
    @FXML
    public void newSinglePlayerScene() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/single_player_scene.fxml"));
        Scene scene = new Scene(layout, primaryStage.getScene().getWidth(),
                primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }

    /**
     * Loads hot seat game.
     * @throws IOException if something went wrong.
     */
    @FXML
    public void newHotSeatScene() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/hot_seat_scene.fxml"));
        Scene scene = new Scene(layout, primaryStage.getScene().getWidth(),
                primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }

    /**
     * Loads statistics.
     * @throws IOException if something went wrong.
     */
    @FXML
    public void newStatisticsScene() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/statistics_scene.fxml"));
        Scene scene = new Scene(layout, primaryStage.getScene().getWidth(),
                primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }

    @FXML
    public void newNetPlayerScene() throws IOException {
        Parent layout = FXMLLoader.load(getClass().getResource("/net_player_scene.fxml"));
        Scene scene = new Scene(layout, primaryStage.getScene().getWidth(),
                primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }
}
