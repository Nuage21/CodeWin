import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;


import javax.naming.ldap.Control;
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
public class Stats_Controller implements Controller {

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

    @FXML
    Button activity_week;
    @FXML
    Button activity_month;
    @FXML
    Button points_week;
    @FXML
    Button points_month;

    //endregion


    // pr le temps passé Tp :  recupérer le temp lors de la connextion avec : int j = (int) (new Date().getTime()/1000);
    //et avant la deconexion recuperer : TP =  (int) (new Date().getTime()/1000) - j ; ** resultat en seconds

    // a la deconexxion (ou a n importe quel moment ) call updateStat(int point ,String stat)
    //  pour recevoir le string update des stats
    // il suffit donc par ex de fair user.stats_point =  updateStat(point,user.statspoint) quand il a une bonne rep ,ce qui permet
    //d avoir les stats qui s actualise en temp reel
    //
    // du coup le sync avec la bdd peux se fair a n importe quell moment , il suffit jsute de call la fnc qui est dans user

    String stats_points = "05/12,20-05/09,20-15/03,40-05/13,100"; // intitialisez ces 2 string a partir de la string recupérer dans user
    String stats_temps = "";


    @FXML
    public void initialize() throws ParseException {

        stats_points = LoggedIn_Controller.getUser().getStats_points();
        stats_temps = LoggedIn_Controller.getUser().getStats_activity();


        progressionChartXCategoryAxis.setAnimated(false);
        progressionChartYNumberAxis.setAnimated(true);
        activityChartXCategoryAxis.setAnimated(true);
        activityChartXCategoryAxis.setAnimated(false);

        activity_week.setOnAction(e -> {
            activityAreaChart.getData().clear();
            try {
                traceWeek(ConvertStringTotree(stats_temps), activityAreaChart, LanguageManager.getContentOf("uptitle"));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        activity_month.setOnAction(e -> {
            activityAreaChart.getData().clear();
            try {
                traceMonth(ConvertStringTotree(stats_temps), activityAreaChart, LanguageManager.getContentOf("uptitle"));


            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        points_week.setOnAction(e -> {
            progressionAreaChart.getData().clear();
            try {
                traceWeek(ConvertStringTotree(stats_points), progressionAreaChart, LanguageManager.getContentOf("uptitle"));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        });
        points_month.setOnAction(e -> {
            progressionAreaChart.getData().clear();
            try {
                Thread.currentThread().sleep(300);
                traceMonth(ConvertStringTotree(stats_points), progressionAreaChart, LanguageManager.getContentOf("uptitle"));
            } catch (ParseException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        //
        traceWeek(ConvertStringTotree(stats_points), progressionAreaChart, LanguageManager.getContentOf("uptitle"));
        traceWeek(ConvertStringTotree(stats_temps), activityAreaChart, LanguageManager.getContentOf("uptitle"));

        Platform.runLater(() -> {
            __setAreaChartWidth(activityAreaChart, Design.CENTRAL_PANE_WIDTH * 0.9);
            __setAreaChartWidth(progressionAreaChart, Design.CENTRAL_PANE_WIDTH * 0.9);
            if (Settings.SIDEBAR_STATE == Settings.SIDEBAR_SHRINKED) {
                double width = Settings.SIDEBAR_WIDTH * (1 - (1 / Settings.SIDEBAR_EXTEND_COEFF));
                this.resetAreaChartsWidth(width);

            }
        });

    }

    public void test() {
        TreeMap<String, Integer> s = new TreeMap();
        s.put("22/04", 10);
        s.put("12/01", 20);
        try {
            traceMonth(s, progressionAreaChart, "ff");
        } catch (Exception e) {
            Debug.debugException(e);
        }
    }

    //cree un nouveau tracé a partir d une treemap ;
    public void traceActivity(Map<String, Integer> informations, AreaChart chart, String nom) throws ParseException {
        XYChart.Series s = new XYChart.Series();

        for (Map.Entry<String, Integer> entry : informations.entrySet()) {


            s.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));

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
        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate tm = calendar.toZonedDateTime().toLocalDate();
        TreeMap tree = new TreeMap();
        for (int i = 0; i < 15; i++) {
            tree.put(tm.minusDays(i).format(DateTimeFormatter.ofPattern("MM/dd")), rand.nextInt() % 50 + 50);
        }

        return tree;
    }


    public void traceWeek(Map<String, Integer> data, AreaChart chart, String nom) throws ParseException { // trace the the last 15 days days starting from yesterday
        GregorianCalendar calendar = new GregorianCalendar();
        TreeMap<String, Integer> map = new TreeMap();
        LocalDate tm = calendar.toZonedDateTime().toLocalDate();
        // tm = tm.minusDays(15) ;  // nombre de jours a tracer  :
        for (int i = 0; i < 15; i++) {
            String s = tm.minusDays(i).format(DateTimeFormatter.ofPattern("MM/dd"));
            if (data.containsKey(s)) map.put(s, data.get(s));
            else map.put(s, 0);
        }
        traceActivity(map, chart, nom);

    }

    public void traceMonth(TreeMap<String, Integer> data, AreaChart chart, String nom) throws ParseException { // trace les 12 derniers mois (fck it au bout d une annees si t as pas ton code cque t est pas sencé l avoire)
        GregorianCalendar calendar = new GregorianCalendar();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        LocalDate tm = calendar.toZonedDateTime().toLocalDate();
        tm = tm.minusMonths(11);
        for (int i = 0; i < 12; i++) {
            int count = 0;
            String s = tm.plusMonths(i).format(DateTimeFormatter.ofPattern("MM/"));
            for (int j = 1; j <= 31; j++) {
                String st = j < 10 ? s + "0" + Integer.toString(j) : s + Integer.toString(j);
                if (data.containsKey(st)) count += data.get(st);

            }
            map.put(tm.plusMonths(i).getMonth().toString(), count);
        }
        traceActivity(map, chart, nom);
    }

    public void resetAreaChartsWidth(double _toadd) {
        this.__setAreaChartWidth(progressionAreaChart, progressionAreaChart.getWidth() + _toadd);
        this.__setAreaChartWidth(activityAreaChart, activityAreaChart.getWidth() + _toadd);
    }

    public void __setAreaChartWidth(AreaChart ac, double width) {
        ac.setMinWidth(width);
        ac.setMaxWidth(width);
        ac.setPrefWidth(width);
    }

    // call this whenever you want to update a certain string
    public static String updateStat(int value, String chaine_intiale) {
        GregorianCalendar calendar = new GregorianCalendar();
        LocalDate tm = calendar.toZonedDateTime().toLocalDate();
        String s = tm.format(DateTimeFormatter.ofPattern("MM/dd"));

        if(chaine_intiale.equals(""))
            return s + "," + Integer.toString(value);

        if (chaine_intiale.contains(s)) {
            int last = Integer.parseInt(chaine_intiale.substring(chaine_intiale.lastIndexOf(s) + 6));
            chaine_intiale = chaine_intiale.substring(0, chaine_intiale.indexOf(s) - 1);
            value += last;


        }
        Debug.debugMsg("" + chaine_intiale + "-" + s + "," + Integer.toString(value));


        return chaine_intiale + "-" + s + "," + value;
    }

    public TreeMap ConvertStringTotree(String chaine_sauvgarde) {
        TreeMap<String, Integer> ret = new TreeMap();
        if (chaine_sauvgarde.length() > 4) {
            for (String s : chaine_sauvgarde.split("-")) {
                String[] date = s.split(",");
                ret.put(date[0], Integer.parseInt(date[1]));
                // Debug.debugMsg("" + date[0]+"  "+date[1]);
            }
        }
        return ret;
    }
}


//    public void toggleButtons(Button weekBtn, Button monthBtn, int mode)
//    {
//        String weekStyle = "-fx-border-width: 0 !important; -fx-background-color:  #dddddd !important;"; // unselect
//        String monthStyle = "-fx-border-color:  #125699 !important; -fx-border-width: 0 0 0 1 !important; -fx-background-color:  #f3f3f3 !important;"; // select
//        if(mode == SHOWING_WEEK)
//        {
//            weekBtn.setStyle(weekStyle);
//            monthBtn.setStyle(monthStyle);
//        }
//        else
//        {
//            weekBtn.setStyle(monthStyle);
//            monthBtn.setStyle(weekStyle);
//        }
//
//    }