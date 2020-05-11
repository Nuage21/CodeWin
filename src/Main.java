import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Settings.appStage = primaryStage;
        Settings.application = this;
        if(Settings.SKIP_AUTHENTICATION)
        {
            User u = new User("Barack_Hawaii_123", "Hakim123l;", "obama@wh.gov", "Barack", "Obama", java.sql.Date.valueOf(LocalDate.of(2000, 11, 9)), "Honolulu, Hawaii, United States", "(860)-231-1234");
            LoggedIn_Controller.setUser(u);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
            Parent root = loader.load();
            Scene sc = new Scene(root);
            primaryStage.setScene(sc);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setFullScreen(Settings.FULLSCREEN_MODE);
            primaryStage.show();
        }
        else
            SceneLoader.loadAuthenticator();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
