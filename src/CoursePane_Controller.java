import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CoursePane_Controller {
    @FXML
    private VBox holderVBox;

    private String courseImagesFolder;

    @FXML void initialize() throws IOException {
    }

    public void displayFromJson(String filename) throws IOException {
        String jsonCourse = Files.readString(Paths.get(filename), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonCourse);

        String title = obj.getString("title");
        String core = obj.getString("core");

        // some preprocessing
        core = core.replace('\n', ' ');
        core = core.replace("  ", " ");
        core = core.strip();

        ArrayList<Parent> panes = new ArrayList<>();
        String gathered = "";

        int clng = core.length();
        int i = 0;
        while(i < clng)
        {
            char c = core.charAt(i);
            if(c == '#')
            {
                // append gathered text first
                if(!gathered.isEmpty())
                {
                    Pane p = this.appendText(gathered.strip());
                    panes.add(p);
                    gathered = ""; // empty
                }
                int depth = getHeadlineDepth(core, i);
                int endex = getEndex(core, i+depth, '#');
                String headline = getHeadline(core, i+depth, endex);
                // append new Headline
                FXMLLoader loader = new FXMLLoader(getClass().getResource("H" + depth + ".fxml"));
                Pane root = loader.load();
                H_Controller ctr = loader.getController();
                root.setMinHeight(root.getPrefHeight());
                ctr.setTitle(headline);
                panes.add((Parent) root);
                i = endex;
            }
            else if(c == '-' && i + 1< clng)
            {
                if(core.charAt(i + 1) == '>') // element list spotted !
                {
                    int endex = getEndex(core, i+2, '#');
                    String list_el = getHeadline(core, i+2, endex);
                    // append new Headline
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ListElement.fxml"));
                    Pane root = loader.load();
                    H_Controller ctr = loader.getController();
                    root.setMinHeight(root.getPrefHeight());
                    ctr.setTitle(list_el);
                    panes.add((Parent) root);
                    i = endex;
                }
            }
            else if(c == '<' && i + 1< clng)
            {
                if(core.charAt(i + 1) == '!') // image-?list spotted !
                {
                    int endex = getEndex(core, i+2, '!');
                    String list = getHeadline(core, i+2, endex);
                    String images[] = list.split("~");
                    HBox box = new HBox();
                    double avg_ratio =  0;
                    for(String file : images)
                    {
                        Image crop = new Image(new FileInputStream(courseImagesFolder + file));
                        avg_ratio += crop.getWidth() / crop.getHeight();
                    }
                    avg_ratio /= images.length;

                    for(String file : images)
                    {
                        StackPane holder = new StackPane();
                        ImageView img = new ImageView();
                        Image crop = new Image(new FileInputStream(courseImagesFolder + file));
                        img.setImage(crop);
                        img.setFitHeight(200);
                        img.setFitWidth(200 * avg_ratio);
                        holder.getChildren().add(img);
                        holder.setStyle("-fx-padding: 10; -fx-background-radius: 20; -fx-border-radius: 20; -fx-background-color:  #2378aa; -fx-border-color: #eeeeee; -fx-border-width: 1;");
                        Pane sup_holder = new Pane();
                        sup_holder.getChildren().add(holder);
                        sup_holder.setStyle("-fx-padding: 0 20 0 0;");
                        box.getChildren().add(sup_holder);
                    }
                    ScrollPane scp = new ScrollPane();
                    scp.setContent(box);
                    scp.setStyle("-fx-padding: 20; -fx-background-color: white;");
                    scp.setPrefHeight(270);
                    scp.setMinHeight(270);
                    panes.add((Parent) scp);
                    i = endex;
                }
            }
            else
                gathered += c;
            i++;
        }
        gathered = gathered.strip();
        if(!gathered.isEmpty())
            panes.add(appendText(gathered));
        holderVBox.getChildren().addAll(panes);
    }

    public Pane appendText(String _txt) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Text.fxml"));
        Pane root = loader.load();
        Text_Controller ctr = loader.getController();
        ctr.setText(_txt);
        return root;
    }

    public static int getHeadlineDepth(String core, int begIndex)
    {
        int counter = 0;
        int length = core.length();
        for(; begIndex < length && core.charAt(begIndex) == '#'; ++begIndex, ++counter);
        return counter;
    }

    public static int getEndex(String core, int begIndex, char sep)
    {
        int length = core.length();
        for(; begIndex < length && core.charAt(begIndex) != sep; ++begIndex);
        return begIndex;
    }

    public static String getHeadline(String core, int beg, int end)
    {
        return core.substring(beg, end).strip();
    }

    public void setCourseImagesFolder(String chapterFolder) {
        this.courseImagesFolder = chapterFolder;
    }
}
