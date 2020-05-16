import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class FakeMain {
    public static void main(String[] args) {
        try {
            String rpath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            String splitted[] = rpath.split("[\\\\/]");
            String path = "";
            for (int i = 0; i < splitted.length - 1; i++) {
                String s = splitted[i];
                path += s + "\\";
            }
            Settings.setPaths(path, path + "courses\\");
        } catch (URISyntaxException e) {
            Debug.debugException(e);
        }

        LanguageManager.loadInstalledLanguages();

        try {
            File file = new File(Settings.projectPath + "\\host.inf");
            Scanner sc = new Scanner(file);
            String server = RSAUtil.decrypt(sc.nextLine().split("[=]")[1].trim(), RSAUtil.privateKey);
            String database = RSAUtil.decrypt(sc.nextLine().split("[=]")[1].trim(), RSAUtil.privateKey);
            String username = RSAUtil.decrypt(sc.nextLine().split("[=]")[1].trim(), RSAUtil.privateKey);
            String pwd = RSAUtil.decrypt(sc.nextLine().split("[=]")[1].trim(), RSAUtil.privateKey);
            String port = RSAUtil.decrypt(sc.nextLine().split("[=]")[1].trim(), RSAUtil.privateKey);

            User.CONN = "jdbc:mysql://" + server + ":" + port + "/" + database;
            User.USERNAME = username;
            User.PASSWORD = pwd;

            Debug.debugMsg("" + server);
            Debug.debugMsg("" + username);
            Debug.debugMsg("" + database);
            Debug.debugMsg("" + pwd);
            Debug.debugMsg("" + port);

            sc.close();
        } catch (Exception e) {
            Debug.debugException(e);
        }

        try {
            File file = new File(Settings.projectPath + "\\user.inf");
            Scanner sc = new Scanner(file);
            Settings.appLang = sc.nextLine().split("[=]")[1].trim();
            Settings.courseLang = sc.nextLine().split("[=]")[1].trim();
            Settings.dataPath += Settings.courseLang+ "\\";
            sc.close();
        } catch (Exception e) {
            Debug.debugException(e);
        }


        Main.main(args);
    }
}
