import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CourseOverview {

    private String courseTitle;
    private String chapterNomination;
    private ArrayList<ChapterOverview> chapters = new ArrayList<>();


    CourseOverview(String jsonFile) throws IOException {
        this.parseFromJson(jsonFile);
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getChapterNomination() {
        return chapterNomination;
    }

    public ArrayList<ChapterOverview> getChapters(){
        return chapters;
    }

    public void parseFromJson(String filename) throws IOException {

        String jsonOverviewCourse = Files.readString(Paths.get(filename), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonOverviewCourse);

        this.courseTitle = obj.getString("courseTitle");
        this.chapterNomination = obj.getString("chapterNomination");

        JSONArray arr = obj.getJSONArray("chapters");

        for (int i = 0; i < arr.length(); ++i) {
            int id = arr.getJSONObject(i).getInt("ID");
            int nCourses = arr.getJSONObject(i).getInt("nCourses");
            int nQuestions = arr.getJSONObject(i).getInt("nQuestions");
            int nQuizes = arr.getJSONObject(i).getInt("nQuizes");
            String title = arr.getJSONObject(i).getString("chapterTitle");

            ArrayList<String> courses = new ArrayList<>();
            JSONArray coursesArr = arr.getJSONObject(i).getJSONArray("courses");

            for (int j = 0; j < coursesArr.length(); ++j)
                courses.add(coursesArr.getString(j));

            ChapterOverview chapOv = new ChapterOverview(id, title, nCourses, nQuestions, nQuizes, courses);
            chapters.add(chapOv);
        }
    }

    class ChapterOverview {
        private int ID;
        private String chapterTitle;
        private int nCourse;
        private int nQuestions;
        private int nQuizes;

        private ArrayList<String> courses;

        ChapterOverview(int _id, String _title, int _nCourse, int _nQuestions, int _nQuizes, ArrayList<String> _courses) {
            this.ID = _id;
            this.chapterTitle = _title;
            this.nCourse = _nCourse;
            this.nQuestions = _nQuestions;
            this.nQuizes = _nQuizes;
            this.courses = _courses;
        }

        public int getnCourse() {
            return nCourse;
        }

        public int getnQuestions() {
            return nQuestions;
        }

        public int getnQuizes() {
            return nQuizes;
        }

        public String getChapterTitle() {
            return chapterTitle;
        }

        public int getID() {
            return ID;
        }

        public ArrayList<String> getCourses() {
            return courses;
        }
    }
}
