import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class generatorPane_Controller {

    @FXML private TextField urlField;
    @FXML private TextField usernameField;
    @FXML private TextField pwdField;
    @FXML private TextField dbField;
    @FXML private TextField portField;

    @FXML private Button generateButton;
    @FXML private Button quitButton;

    @FXML public void initialize()
    {
        quitButton.setOnMouseClicked(mouseEvent -> Platform.exit());

        generateButton.setOnMouseClicked(mouseEvent -> {
            String url = urlField.getText();
            String db = dbField.getText();
            String username = usernameField.getText();
            String pwd = pwdField.getText();
            String port = portField.getText();

            try {
                String urlEncrypted = Base64.getEncoder().encodeToString(RSAUtil.encrypt(url, RSAUtil.publicKey));
                String dbEncrypted = Base64.getEncoder().encodeToString(RSAUtil.encrypt(db, RSAUtil.publicKey));
                String usernameEncrypted = Base64.getEncoder().encodeToString(RSAUtil.encrypt(username, RSAUtil.publicKey));
                String pwdEncrypted = Base64.getEncoder().encodeToString(RSAUtil.encrypt(pwd, RSAUtil.publicKey));
                String portEncrypted = Base64.getEncoder().encodeToString(RSAUtil.encrypt(port, RSAUtil.publicKey));

                FileWriter myWriter = new FileWriter(Main.jarPath + "\\host.inf");

                myWriter.write("server = " + urlEncrypted + "\n");
                myWriter.write("database = " + dbEncrypted + "\n");
                myWriter.write("username = " + usernameEncrypted + "\n");
                myWriter.write("pwd = " + pwdEncrypted + "\n");
                myWriter.write("port = " + portEncrypted + "\n");

                myWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
