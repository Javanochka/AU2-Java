package ru.spbau.nikiforovskaya.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ru.spbau.nikiforovskaya.client.ClientManager;
import ru.spbau.nikiforovskaya.utils.ProtoMessage;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    public ComboBox parameterComboBox;
    public HBox firstChanging;
    public HBox secondChanging;
    public TextField ipInput;
    public TextField portInput;
    public ComboBox architectureComboBox;
    public TextField numberOfQueriesInput;
    public TextField changesFrom;
    public TextField changesTo;
    public TextField changesStep;
    public LineChart chart;
    public NumberAxis xAxis;
    private TextField firstFixed = new TextField();
    private TextField secondFixed = new TextField();


    @FXML
    void itemSelected() {
        //getSelectionModel().getSelectedItem().toString();
        String[] set = getSetParameters(
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

    private String[] getSetParameters(String s) {
        switch (s) {
            case "clients":
                return new String[] {"elements", "delta"};
            case "elements":
                return new String[] {"clients", "delta"};
            case "delta":
                return new String[] {"clients", "elements"};
        }
        return null;
    }

    @FXML
    void countStatistics() {
        String serverManagerHost = ipInput.getText();
        int serverManagerPort = Integer.parseInt(portInput.getText());
        ProtoMessage.Settings.ServerType type = getServerType();
        int numberOfQueries = Integer.parseInt(numberOfQueriesInput.getText());
        int[] startValues = getStartValues();
        int whoChanges = parameterComboBox.getSelectionModel().getSelectedIndex();
        int step = Integer.parseInt(changesStep.getText());
        int finalValue = Integer.parseInt(changesTo.getText());
        ClientManager manager = new ClientManager(serverManagerHost, serverManagerPort, type, numberOfQueries,
                startValues, whoChanges, step, finalValue);
        manager.run();
        buildCharts(manager.getClientTimeList(), manager.getProcessTimeList(), manager.getSortTimeList());
    }

    private void buildCharts(ArrayList<Pair<Integer, Integer>> clientTimeList, 
                             ArrayList<Pair<Integer, Integer>> processTimeList,
                             ArrayList<Pair<Integer, Integer>> sortTimeList) {
        chart.getData().clear();
        XYChart.Series<Integer, Integer> series1 = buildChart(sortTimeList, "Time of sorting");
        XYChart.Series<Integer, Integer> series2 = buildChart(processTimeList, "Time of processing");
        XYChart.Series<Integer, Integer> series3 = buildChart(clientTimeList, "Time on client");
        xAxis.setLabel(parameterComboBox.getSelectionModel().getSelectedItem().toString());
        chart.getData().addAll(series1, series2, series3);
    }

    private XYChart.Series<Integer, Integer> buildChart(List<Pair<Integer, Integer>> data, String name) {
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
        series.setName(name);

        for (Pair<Integer, Integer> pair : data) {
            series.getData().add(new XYChart.Data<>(pair.getKey(), pair.getValue()));
        }
        return series;
    }

    private int[] getStartValues() {
        int i = parameterComboBox.getSelectionModel().getSelectedIndex();
        switch (i) {
            case 0:
                return new int[] {Integer.parseInt(changesFrom.getText()),
                        Integer.parseInt(firstFixed.getText()),
                        Integer.parseInt(secondFixed.getText())};
            case 1:
                return new int[] {Integer.parseInt(firstFixed.getText()),
                        Integer.parseInt(changesFrom.getText()),
                        Integer.parseInt(secondFixed.getText())};
            case 2:
                return new int[] {Integer.parseInt(firstFixed.getText()),
                        Integer.parseInt(secondFixed.getText()),
                        Integer.parseInt(changesFrom.getText())};
        }
        return null;
    }

    private ProtoMessage.Settings.ServerType getServerType() {
        int i = architectureComboBox.getSelectionModel().getSelectedIndex();
        switch(i) {
            case 0:
                return ProtoMessage.Settings.ServerType.THREAD_EACH;
            case 1:
                return ProtoMessage.Settings.ServerType.THREAD_POOL;
            case 2:
                return ProtoMessage.Settings.ServerType.NONBLOCKING;
        }
        return null;
    }
}
