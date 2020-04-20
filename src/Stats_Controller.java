import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

public class Stats_Controller {

    //region FXML
    @FXML
    private AreaChart<String, Number> activityAreaChart;

    @FXML
    private AreaChart<String, Number> progressionAreaChart;

    @FXML CategoryAxis activityChartXCategoryAxis;
    @FXML CategoryAxis progressionChartXCategoryAxis;

    @FXML NumberAxis activityChartYNumberAxis;
    @FXML NumberAxis progressionChartYNumberAxis;
    //endregion





    @FXML
    void initialize() throws ParseException {
        traceActivity(randomtrace(),activityAreaChart,"code");
        traceActivity(randomtrace(),progressionAreaChart,"code");


    }
    //cree un nouveau tracé a partir d une treemap ;
    public void traceActivity(TreeMap<Integer, Integer> informations,AreaChart chart,String nom) throws ParseException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LLLL-yyyy ");
        XYChart.Series s = new XYChart.Series();

        for(Map.Entry<Integer,Integer> entry : informations.entrySet()){


            LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochSecond(entry.getKey()), ZoneId.systemDefault());
            String formattedString = date.format(formatter);
            s.getData().add(new XYChart.Data(formattedString, entry.getValue()));

        }
        s.setName(nom);
        chart.getData().add(s);
        changeLineColor(s,"0,0,255");


    }
    public void changeLineColor(XYChart.Series s,String color){ //color in RGB format
        Node fill = s.getNode().lookup(".chart-series-area-fill"); // only for AreaChart
        Node line = s.getNode().lookup(".chart-series-area-line");


        fill.setStyle("-fx-fill: rgba(" + color + ", 0.15);");
        line.setStyle("-fx-stroke: rgba(" + color + ", 1.0);");
    }




    public TreeMap randomtrace(){

        int j = (int) (new Date().getTime()/1000);
        Random rand = new Random();
        TreeMap tree = new TreeMap();
        for(int i=0;i<15;i++){
            tree.put(j-80000*i,rand.nextInt()%100+100) ;
        }
        return tree ;
    }

    // sauveguarde le couple dateActuelle(Da) et value (qui est soit le nb de point today ou le temps passé )
    // format du treemap : <date,valeur>
    // pr le temps passé Tp :  recupérer le temp lors de la connextion avec :
    //  int j = (int) (new Date().getTime()/1000);
    //et avant la deconexion recuperer : TP =  (int) (new Date().getTime()/1000) - j ;


    // ajout d une nouvelle valeur
    public String saveTreemapBdd(int value , String chaine_intiale){
        int j = (int) (new Date().getTime()/1000);

        return chaine_intiale+"-"+Integer.toString(j)+","+Integer.toString(value);
    }

    public TreeMap loadTreemapBdd(String chaine_sauvgarde){
        TreeMap ret = new TreeMap( ) ;

        for(String s : chaine_sauvgarde.split("-")){
            String[] date = s.split(",");

            ret.put(Integer.parseInt(date[0]),Integer.parseInt(date[1]));
        }
        return ret ;
    }




}





