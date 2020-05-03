import DialogBoxes.ErrorBox_Controller;
import javafx.stage.Stage;

public class Checker {

    public static boolean isImage(String file) {
        String lowered = file.toLowerCase();
        String extensions[] = {".jpeg", ".jpg", ".png"};
        for (String ext : extensions) {
            if (lowered.contains(ext))
                return true;
        }
        return false;
    }

    public static void showConnexionError()
    {
        ErrorBox_Controller.showErrorBox(Settings.appStage, "Probleme de Connexion", "Veuillez verifier votre connexion avant d'essayer de vous connecter.");
    }
}
