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
        ErrorBox_Controller.showErrorBox(Settings.appStage, "Probleme de Connexion", "Veuillez verifier votre connexion a Internet");
    }

    public static void showPwdError()
    {
        ErrorBox_Controller.showErrorBox(Settings.appStage, "Mot de Passe icorrecte", "Le mot de passe que vous avez fourni est incorrect. Veuillez reessayer");
    }


    }

    public boolean psswrdVer(PasswordField p) {
        String usr = p.getText();
        boolean lengh = usr.length() >= 8;
        boolean num = usr.matches("^.*[0-9].*.[0-9].*");
        boolean spec = usr.matches("^.*(?=.*[*#@_;,&��]).*$");
        return lengh && num && spec;
    }

    public boolean mailVer() {
        String email = EmailField.getText();
        String mail_format = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(mail_format);
        Matcher match = pattern.matcher(email);

        boolean valide_mail = match.matches();
        boolean is_not_used = true; // BDD request here
        if(Settings.ACTIVE_DB_MODE) {
            // valide_mail = User.userEmailExiste(email);
        }
        return valide_mail && is_not_used;
    }



}
