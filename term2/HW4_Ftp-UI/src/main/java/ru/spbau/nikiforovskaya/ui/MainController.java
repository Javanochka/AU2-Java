package ru.spbau.nikiforovskaya.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import ru.spbau.nikiforovskaya.ftp.Client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * A controller for main scene. Manages with clicking on row.
 * Helps to show the info.
 */
public class MainController {

    @FXML private Text textInfo;
    @FXML private TableView tableView;

    private String currentPath = ".";

    /** Starts the working of scene. Shows all the files in root directory. */
    @SuppressWarnings("unchecked")
    @FXML
    public void initialize() {
        ByteArrayInputStream input = new ByteArrayInputStream("1 .\n0".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Client client = new Client(Main.hostName, Main.portNumber, input, output);
        try {
            client.runClient();
        } catch (Exception e) {
            textInfo.setText("Sorry, something wrong has happened, rerun the program.");
            System.err.println(e.getMessage());
        }

        String[] result = output.toString().trim().split("\n");
        Record[] records = makeRecordArrayFromStringArray(
                Arrays.copyOfRange(result, 1, result.length));
        ObservableList<Record> data = tableView.getItems();
        data.addAll(records);
    }

    /**
     * Handles with clicking on rows of the tables.
     * Only double clicks work.
     * @param mouseEvent -- an event happened.
     */
    @FXML
    public void handleMouseClickOnRow(MouseEvent mouseEvent) {
        Record selected = (Record) tableView.getSelectionModel().getSelectedItem();
        if (mouseEvent.getClickCount() != 2) {
            return;
        }
        if (selected.getType().equals("file")) {
            processFileDownload(selected.getName());
        } else {
            processPressedDirectory(selected.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void processPressedDirectory(String pressedName) {
        currentPath = Paths.get(currentPath, pressedName).normalize().toString();
        if (currentPath.equals("")) {
            currentPath = ".";
        }
        ByteArrayInputStream input = new ByteArrayInputStream(("1 " +
                 currentPath + "\n0").getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Client client = new Client(Main.hostName, Main.portNumber, input, output);
        try {
            client.runClient();
        } catch (Exception e) {
            textInfo.setText("Sorry, didn't manage to open directory.");
            System.err.println(e.getMessage());
        }
        String[] result = output.toString().trim().split("\n");
        Record[] records = makeRecordArrayFromStringArray(
                Arrays.copyOfRange(result, 1, result.length));
        ObservableList<Record> data = tableView.getItems();
        data.clear();
        if (!currentPath.equals(".")) {
            data.setAll(new Record("..", true));
        }
        data.addAll(records);
    }

    private void processFileDownload(String pressedName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose where to save file");
        File file = fileChooser.showSaveDialog(Main.primaryStage);

        if (file != null) {
            ByteArrayInputStream input = new ByteArrayInputStream(("2 " +
                    Paths.get(currentPath, pressedName).toString() +
                    "\n" + file.getAbsolutePath() + "\n0").getBytes());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Client client = new Client(Main.hostName, Main.portNumber, input, output);
            try {
                client.runClient();
            } catch (Exception e) {
                textInfo.setText("Sorry, didn't manage to save file.");
                System.err.println(e.getMessage());
            }
        }
    }

    private Record[] makeRecordArrayFromStringArray(String[] information) {
        Record[] records = new Record[information.length];
        for (int i = 0; i < information.length; i++) {
            String[] line = information[i].split(" ");
            String name = Paths.get(line[0]).getFileName().toString();
            records[i] = new Record(name, Boolean.parseBoolean(line[1]));
        }
        return records;
    }

    /** Class for saving record into table view. */
    public static class Record {
        public final SimpleStringProperty name;
        public final SimpleStringProperty type;

        /**
         * Creates record with the specified name and type.
         * @param name name of file
         * @param isFolder is file folder or not
         */
        public Record(String name, boolean isFolder) {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(isFolder ? "folder" : "file");
        }

        /**
         * Returns the file name.
         * @return the file name.
         */
        public String getName() {
            return name.get();
        }

        /**
         * Returns file type : folder or file.
         * @return file type.
         */
        public String getType() {
            return type.get();
        }
    }

}
