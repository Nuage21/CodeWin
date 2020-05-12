import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class GO_Course_Pane_Controller {

    @FXML
    private Label titleLabel;
    @FXML
    private Label nCoursesLabel;

    @FXML
    private Label nQuestionsLabel;

    @FXML private Pane holderPane;

    @FXML
    public void initialize() {
    }

    public void setAll(String title, int nCourse, int nQuestions, int nQuizes) {
        titleLabel.setText(title);
        nCoursesLabel.setText("" + nCourse);
        nQuestionsLabel.setText("" + nQuestions);
    }

    public Pane getHolderPane() {
        return holderPane;
    }
}
