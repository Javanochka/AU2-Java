package ru.spbau.nikiforovskaya.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ftp-client application.
 * You can search for files and download them.
 */
public class Main extends Application{

    static String hostName;
    static int portNumber;
    static Stage primaryStage;

    /**
     * Initialize the work of application.
     * @param primaryStage a stage, where to put content
     * @throws Exception if something went wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main_scene.fxml"));
        primaryStage.setTitle("FTP client");
        Main.primaryStage = primaryStage;
        primaryStage.setMinHeight(240);
        primaryStage.setMinWidth(460);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Pass host name and port number to arguments
     * @param args -- args[0] -- host name, args[1] -- port number
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Pass two arguments: host name and port number");
            return;
        }
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        launch(args);
    }
}