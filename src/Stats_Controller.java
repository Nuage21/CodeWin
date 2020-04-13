import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Stats_Controller {

    @FXML
    private AreaChart<String, Number> activityAreaChart;

    @FXML
    private AreaChart<String, Number> progressionAreaChart;

    @FXML CategoryAxis activityChartXCategoryAxis;
    @FXML CategoryAxis progressionChartXCategoryAxis;

    @FXML NumberAxis activityChartYNumberAxis;
    @FXML NumberAxis progressionChartYNumberAxis;

    @FXML
    void initialize() {
        // Defining the X axis
        CategoryAxis xAxis = new CategoryAxis();

        // Defining the y Axis
        NumberAxis yAxis = new NumberAxis(0, 40, 2.5);
        yAxis.setLabel("Points");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Cours: Code de la Route");
        series1.getData().add(new XYChart.Data("Dimanche", 3));
        series1.getData().add(new XYChart.Data("Lundi", 10));
        series1.getData().add(new XYChart.Data("Mardi", 5));
        series1.getData().add(new XYChart.Data("Mercredi", 9));
        series1.getData().add(new XYChart.Data("Jeudi", 25));
        series1.getData().add(new XYChart.Data("Vendredi", 17));
        series1.getData().add(new XYChart.Data("Samedi", 36));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Un autre Cours");

        series2.getData().add(new XYChart.Data("Dimanche", 1));
        series2.getData().add(new XYChart.Data("Lundi", 7));
        series2.getData().add(new XYChart.Data("Mardi", 8));
        series2.getData().add(new XYChart.Data("Mercredi", 11));
        series2.getData().add(new XYChart.Data("Jeudi", 18));
        series2.getData().add(new XYChart.Data("Vendredi", 20));
        series2.getData().add(new XYChart.Data("Samedi", 16));


        XYChart.Series s3 = new XYChart.Series();
        s3.setName("Code de la Route");

        s3.getData().add(new XYChart.Data("Dimanche", 5));
        s3.getData().add(new XYChart.Data("Lundi", 3));
        s3.getData().add(new XYChart.Data("Mardi", 5));
        s3.getData().add(new XYChart.Data("Mercredi", 8));
        s3.getData().add(new XYChart.Data("Jeudi", 3));
        s3.getData().add(new XYChart.Data("Vendredi", 0));
        s3.getData().add(new XYChart.Data("Samedi", 10));

        XYChart.Series s4 = new XYChart.Series();
        s3.setName("Code de la Route");

        s4.getData().add(new XYChart.Data("Dimanche", 1));
        s4.getData().add(new XYChart.Data("Lundi", 2));
        s4.getData().add(new XYChart.Data("Mardi", 4));
        s4.getData().add(new XYChart.Data("Mercredi", 0));
        s4.getData().add(new XYChart.Data("Jeudi", 0));
        s4.getData().add(new XYChart.Data("Vendredi", 0));
        s4.getData().add(new XYChart.Data("Samedi", 7));

        s4.setName("Un autre Cours");


        activityAreaChart.getData().addAll(series1,series2);
        progressionAreaChart.getData().addAll(s3, s4);
        
    }
}
