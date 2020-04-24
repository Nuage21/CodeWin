import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CourseCoord
{
    public int chapterID;
    public int courseID;
    public String filePath;
    public String imagesPath;
    public static CourseOverview CO;

    public CourseCoord(int _chapID, int _courseID) throws IOException {
        this.chapterID = _chapID;
        this.courseID = _courseID;
        this.setPaths();
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
        String jsonOverviewString = Files.readString(Paths.get(json_file), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonOverviewString);
        JSONArray files = obj.getJSONArray("files");

        filePath =  Settings.dataPath + folder + files.getString(courseID);
        String filename = files.getString(courseID);
        String withoutExtension = filename.split("[.]")[0];
        imagesPath =  Main.appSettings.dataPath + folder + "img\\" + withoutExtension + "\\";
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
        return CO.getChapters().get(chapterID).getCourses().get(courseID);
    }

}