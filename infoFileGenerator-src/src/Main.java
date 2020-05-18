import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URISyntaxException;

public class Main extends Application {

    public static String jarPath;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("generatorPane.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            String rpath =  new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            String splitted[] = rpath.split("[\\\\/]");
            String path = "";
            for(int i = 0; i < splitted.length - 1; i++)
            {
                String s = splitted[i];
                path += s + "\\";
            }
            jarPath = path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
