import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static void loadAuthenticator()
    {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource("Authenticator.fxml"));
            LanguageManager.authenticatorLoader = loader;
            Parent root = loader.load();
            LanguageManager.resyncLanguage(loader, "authenticator");
            Stage stg = Settings.appStage;
            stg.centerOnScreen();
            Scene sc = new Scene(root);
            LoggedIn_Controller.setUser(null);
            stg.setScene(sc);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
