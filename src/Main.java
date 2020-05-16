import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings.appStage = primaryStage;
        Settings.application = this;

        LanguageManager.loadLangData(Settings.projectPath + "\\lang\\" +  Settings.appLang + ".xml");

        if(Settings.SKIP_AUTHENTICATION)
        {

            User u = new User("Barack_Hawaii_123", "Hakim123l;", "obama@wh.gov", "Barack", "Obama", java.sql.Date.valueOf(LocalDate.of(2000, 11, 9)), "Honolulu, Hawaii, United States", "(860)-231-1234");
            LoggedIn_Controller.setUser(u);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
            LanguageManager.loggedinLoader = loader;
            Parent root = loader.load();

            LanguageManager.resyncLanguage(loader, "loggedin");
            primaryStage.setScene(new Scene(root));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setFullScreen(Settings.FULLSCREEN_MODE);
            primaryStage.show();
        }
        else
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Authenticator.fxml"));
            Parent root = loader.load();
            LanguageManager.authenticatorLoader = loader;
            LanguageManager.resyncLanguage(loader, "authenticator");
            primaryStage.setTitle("CodeWin Sign in/up");
            Scene sc = new Scene(root);
            primaryStage.setScene(sc);
            primaryStage.setResizable(false);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        }
    }


    public static void main(String[] args) {

        launch(args);
    }

}
