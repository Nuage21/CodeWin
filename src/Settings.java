import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Settings {

    public static boolean MOUNIR_MODE = false; // set to true if you're Brazilien working
    public Stage appStage;
    public String dataPath = "C:\\Users\\hbais\\Desktop\\P2CP\\CodeWin\\CodeWin\\src\\data\\";

    // display-what modes
    public static int DISPLAYING_COURSE_OVERVIEW = 0x00;
    public static int DISPLAYING_CHAPTER_OVERVIEW = 0x01;
    public static int DISPLAYING_COURSE = 0x02;
}
