import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import java.security.NoSuchAlgorithmException;

public class GeneratorPane_Controller {

    @FXML private Label pkeyLabel;
    @FXML private Button generateButton;
    @FXML private TextField uTextField;
    @FXML private ImageView copyImage;

    public static String SecretKey = ":#:CODEWIN1;3WINCODE?!.";


    @FXML public void initialize()
    {
        generateButton.setOnMouseClicked(mouseEvent -> {
            String username = uTextField.getText();
            String pkey = generatePKey(username);
            String pkeyFormatted = getFormattedPKey(pkey);
            pkeyLabel.setText(pkeyFormatted);
        });

        copyImage.setOnMouseClicked(mouseEvent -> {
            String myString = pkeyLabel.getText();
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
    }

    public static String generatePKey(String _username)
    {
        try {
            String fullPKey = Hasher.getSHA256_Of(_username + SecretKey);
            return fullPKey.substring(0, 20).toUpperCase();
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    public static String getFormattedPKey(String _pkey)
    {
        int l = _pkey.length();
        String formatted = "";
        for(int i = 0; i < l; ++i)
        {
            if(i % 4 == 0 && i > 0)
                formatted += '-'; // separator
            formatted += _pkey.charAt(i);
        }
        return formatted;
    }
}
