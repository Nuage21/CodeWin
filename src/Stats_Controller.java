
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.json.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Stats_Controller {

    //region FXML
    @FXML
    private AreaChart<String, Integer> activityAreaChart;

    @FXML
    private AreaChart<String, Integer> progressionAreaChart;

    @FXML
    CategoryAxis activityChartXCategoryAxis;
    @FXML
    CategoryAxis progressionChartXCategoryAxis;

    @FXML
    NumberAxis activityChartYNumberAxis;
    @FXML
    NumberAxis progressionChartYNumberAxis;
    //endregion


    @FXML
    void initialize() throws ParseException {
        traceWeek(randomtrace(), activityAreaChart, "code");
        traceWeek(randomtrace(), progressionAreaChart, "code");


    }
    public void test(){
        TreeMap<String,Integer> s = new TreeMap() ;
        s.put("22/04",10);
        s.put("12/01",20);
        try {
            traceMonth(s,progressionAreaChart,"ff");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //cree un nouveau tracé a partir d une treemap ;
    public void traceActivity(Map<String, Integer> informations, AreaChart chart, String nom) throws ParseException {
        XYChart.Series s = new XYChart.Series();

        for (Map.Entry<String, Integer> entry : informations.entrySet()) {



            s.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));

        }
        s.setName(nom);
        chart.getData().add(s);
        changeLineColor(s, "0,0,255");


    }

    public void changeLineColor(XYChart.Series s, String color) { //color in RGB format
        Node fill = s.getNode().lookup(".chart-series-area-fill"); // only for AreaChart
        Node line = s.getNode().lookup(".chart-series-area-line");


        fill.setStyle("-fx-fill: rgba(" + color + ", 0.15);");
        line.setStyle("-fx-stroke: rgba(" + color + ", 1.0);");
    }

    public TreeMap randomtrace() {


        Random rand = new Random();
        GregorianCalendar calendar = new GregorianCalendar() ;
        LocalDate tm  = calendar.toZonedDateTime().toLocalDate();
        TreeMap tree = new TreeMap();
        for (int i = 0; i < 15; i++) {
            tree.put( tm.minusDays(i).format(DateTimeFormatter.ofPattern("dd/MM")), rand.nextInt() % 50 + 50);
        }

        return tree;
    }


    public void traceWeek(Map<String,Integer> data,AreaChart chart,String nom) throws ParseException { // trace the the last 15 days days starting from yesterday
        GregorianCalendar calendar = new GregorianCalendar() ;
        TreeMap<String,Integer> map = new TreeMap();
        LocalDate tm  = calendar.toZonedDateTime().toLocalDate();
        tm = tm.minusDays(15) ;  // nombre de jours a tracer  :
        for(int i=0;i<15;i++) {
            String s = tm.plusDays(i).format(DateTimeFormatter.ofPattern("dd/MM"));
            if(data.containsKey(s)) map.put(s,data.get(s));
            else map.put(s,0);
        }
        traceActivity(map,chart,nom);

    }

    public void traceMonth(TreeMap<String,Integer> data,AreaChart chart,String nom) throws ParseException { // trace les 12 derniers mois (fck it au bout d une annees si t as pas ton code cque t est pas sencé l avoire)
        GregorianCalendar calendar = new GregorianCalendar();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        LocalDate tm = calendar.toZonedDateTime().toLocalDate();
        tm = tm.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            int count = 0 ;
            String s = tm.plusMonths(i).format(DateTimeFormatter.ofPattern("/MM"));
            for(int j=1;j<=31;j++){
                String st = j<10? "0"+Integer.toString(j)+s  :  Integer.toString(j)+s ;
                if(data.containsKey(st)) count += data.get(st) ;

            }
            map.put(tm.plusMonths(i).getMonth().toString(),count);
        }
        traceActivity(map,chart,nom);
    }


}






