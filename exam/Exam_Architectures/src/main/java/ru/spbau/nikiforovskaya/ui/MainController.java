package ru.spbau.nikiforovskaya.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MainController {

    public ComboBox parameterComboBox;
    public HBox firstChanging;
    public HBox secondChanging;
    private TextField firstFixed = new TextField();
    private TextField secondFixed = new TextField();


    @FXML
    void itemSelected() {
        //getSelectionModel().getSelectedItem().toString();
        String[] set = getSetParamaters(
                parameterComboBox.getSelectionModel().getSelectedItem().toString());
        firstChanging.getChildren().clear();
        secondChanging.getChildren().clear();
        Text t1 = new Text("Number of " + set[0]);
        t1.setWrappingWidth(250);
        firstChanging.getChildren().add(t1);
        firstChanging.getChildren().add(firstFixed);
        Text t2 = new Text("Number of " + set[1]);
        t2.setWrappingWidth(250);
        secondChanging.getChildren().add(t2);
        secondChanging.getChildren().add(secondFixed);

    }

    private String[] getSetParamaters(String s) {
        switch (s) {
            case "clients":
                return new String[] {"elements", "delta"};
            case "elements":
                return new String[] {"clients", "delta"};
            case "delta":
                return new String[] {"elements", "clients"};
        }
        return null;
    }
}
