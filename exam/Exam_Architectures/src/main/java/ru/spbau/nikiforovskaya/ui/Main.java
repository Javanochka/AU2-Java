package ru.spbau.nikiforovskaya.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    //static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main_scene.fxml"));
        primaryStage.setTitle("ServerManager manager");
       // Main.primaryStage = primaryStage;
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(500);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}