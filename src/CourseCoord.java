import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseCoord implements Comparable<CourseCoord>
{
    public int chapterID;
    public int courseID;
    private String title;
    private String filePath;
    private String imagesPath;
    private String core;
    private boolean hasQuestions; // questions about this course ?
    private ArrayList<Integer> questions = new ArrayList<>(); // Course issued questions

    public static CourseOverview CO;

    public CourseCoord(int _chapID, int _courseID) throws IOException {
        this.chapterID = _chapID;
        this.courseID = _courseID;
        this.setPaths();

        Scanner scanner = new Scanner( new File(filePath), "UTF-8" );
        String jsonCourse = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject obj = new JSONObject(jsonCourse);

        this.title = obj.getString("title");
        this.core = obj.getString("core");

        this.core = this.core.replace('\n', ' ');
        this.core = this.core.replace("  ", " ");
//        this.core = this.core.strip();
        this.core = this.core.trim();

        this.hasQuestions = obj.getBoolean("hasQuestions");

        String qsts[] = obj.getString("questionsID").split("[~]");
        for(String q : qsts)
            this.questions.add(Integer.parseInt(q));

    }

    public boolean isLastCourseWithinChapter()
    {
        return courseID == CO.getChapters().get(chapterID).getnCourse() - 1;
    }

    public boolean isLastCourse()
    {
        return isLastCourseWithinChapter() && (chapterID == CO.getChapters().size() - 1);
    }

    public void setPaths() throws IOException {
        String folder = CO.getChapters().get(chapterID).getFolder();

        String json_file = Settings.dataPath + folder + "Overview.json";
//        String jsonOverviewString = Files.readString(Paths.get(json_file), StandardCharsets.UTF_8);  JAVA-11 !

        Scanner scanner = new Scanner( new File(json_file), "UTF-8" );
        String jsonOverviewString = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject obj = new JSONObject(jsonOverviewString);
        JSONArray files = obj.getJSONArray("files");

        filePath =  Settings.dataPath + folder + files.getString(courseID);
        String filename = files.getString(courseID);
        String withoutExtension = filename.split("[.]")[0];
        imagesPath =  Settings.dataPath + folder + "img\\" + withoutExtension + "\\";
    }

    public CourseCoord getNextCourse() throws IOException {
        if(this.isLastCourseWithinChapter())
        {
            CourseCoord cc = new CourseCoord(chapterID+1, 0);
            return cc;
        }
        CourseCoord cc = new CourseCoord(chapterID, courseID+1);
        return cc;
    }

    public CourseCoord getPreviousCourse() throws IOException {
        if(chapterID == 0 && courseID == 0)
            return this;
        if(courseID == 0)
        {
            CourseCoord cc = new CourseCoord(chapterID-1, CO.getChapters().get(chapterID-1).getnCourse() - 1);
            return cc;
        }
        CourseCoord cc = new CourseCoord(chapterID, courseID-1);
        return cc;
    }

    public String getCourseTitle()
    {
        return this.title;
    }

    @Override
    public int compareTo(CourseCoord courseCoord) {
        if(this.chapterID == courseCoord.chapterID && this.courseID == courseCoord.courseID)
            return  0;
        if(this.chapterID > courseCoord.chapterID  || (this.chapterID == courseCoord.chapterID && this.courseID > courseCoord.courseID))
            return 1;
        return -1;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean hasQuestions() {
        return hasQuestions;
    }

    public ArrayList<Integer> getQuestions(){ return this.questions; }
}