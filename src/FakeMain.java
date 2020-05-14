import java.io.File;
import java.net.URISyntaxException;

public class FakeMain {
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
            Settings.setPaths(path, path + "data\\");
        } catch (URISyntaxException e) {
            Debug.debugException(e);
        }

        LanguageManager.loadInstalledLanguages();

        Main.main(args);
    }
}
