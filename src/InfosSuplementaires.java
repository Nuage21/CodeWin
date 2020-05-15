
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.TreeMap;

public class InfosSuplementaires implements Serializable {
    public String path  = "" ; //directory

    public  TreeMap<String,Integer> stats_points ;
    public  TreeMap<String,Integer> stats_activite ;
    public String user_name ;

    // load informations from user_name.info , call every login
    public  InfosSuplementaires(String user_name)  {

        try {
            FileInputStream fi = new FileInputStream(new File(path+user_name+".info"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            InfosSuplementaires infos = (InfosSuplementaires) oi.readObject();

            this.stats_points = infos.stats_points ;
            this.stats_activite= infos.stats_activite ;
            this.user_name = infos.user_name;
            oi.close();
            fi.close();
        } catch (FileNotFoundException e) {
            this.stats_activite = new TreeMap<>();
            this.stats_points= new TreeMap<>() ;
            this.user_name=user_name;


        } catch (IOException e) {
            Debug.debugException(e);
        } catch (ClassNotFoundException e) {
            Debug.debugException(e);
        }
    }

    public void updateMap(int value,TreeMap map){
        GregorianCalendar calendar = new GregorianCalendar() ;
        LocalDate tm  = calendar.toZonedDateTime().toLocalDate();
        String  s=tm.format(DateTimeFormatter.ofPattern("dd/MM"));

        map.put(s,value);
    }

    public void newDay(int nb_points,int nb_minutes){ // call cette fnc a la fin de chaque jours
        updateMap(nb_points,this.stats_points);
        updateMap(nb_minutes,this.stats_activite);

        try {
            FileOutputStream f = new FileOutputStream(new File(path+user_name + ".info"),false);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(this);
            o.close();
            f.close();
        }catch (Exception ex){}

    }

}
