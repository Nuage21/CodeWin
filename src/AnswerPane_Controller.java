import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AnswerPane_Controller {

    @FXML
    private ImageView answerImageView;

    @FXML
    private Label answerLabel;

    @FXML
    private Pane holder;

    @FXML
    public void initialize() {

    }

    public void setToFalseAnswer() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(Settings.projectPath + "img\\unchecked.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        answerImageView.setImage(image);
        answerLabel.setText("Reponse Fausse !");
        ((DropShadow) holder.getEffect()).setColor(Color.valueOf("#892312"));
    }

    public void setVisible(boolean _visible)
    {
        holder.setVisible(_visible);
        holder.setManaged(_visible);
    }
}
