import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Settings appSettings = new Settings();

    @Override
    public void start(Stage primaryStage) throws Exception {

        appSettings.appStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Authenticator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CodeWin Sign in/up");
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
