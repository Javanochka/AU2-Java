package ru.spbau.nikiforovskaya.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import ru.spbau.nikiforovskaya.logger.Logger;

import java.io.IOException;

/** Controller of the statistics scene. */
public class StatisticsController {
    @FXML private TableView tableView;

    /** Fills the statistics table with data. */
    @SuppressWarnings("unchecked")
    @FXML
    public void initialize() {
        ObservableList<Logger.LogInfo> data = tableView.getItems();
        data.addAll(Logger.getRecords());
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
