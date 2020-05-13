import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

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

        System.out.println(filename);
//        String jsonOverviewCourse = Files.readString(Paths.get(filename), StandardCharsets.UTF_8);

        Scanner scanner = new Scanner( new File(filename), "UTF-8" );
        String jsonOverviewCourse = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject obj = new JSONObject(jsonOverviewCourse);

        this.courseTitle = obj.getString("courseTitle");
        this.chapterNomination = obj.getString("chapterNomination");

        JSONArray arr = obj.getJSONArray("chapters");

        for (int i = 0; i < arr.length(); ++i) {
            JSONObject cObj = arr.getJSONObject(i);
            int id = cObj.getInt("ID");
            int nCourses = cObj.getInt("nCourses");
            int nQuestions = cObj.getInt("nQuestions");
            int nQuizes = cObj.getInt("nQuizes");
            String title = cObj.getString("chapterTitle");
            String folder = cObj.getString("folder");

            ArrayList<String> courses = new ArrayList<>();
            JSONArray coursesArr = cObj.getJSONArray("courses");

            for (int j = 0; j < coursesArr.length(); ++j)
                courses.add(coursesArr.getString(j));

            ChapterOverview chapOv = new ChapterOverview(id, title, nCourses, nQuestions, nQuizes, folder, courses);
            chapters.add(chapOv);
        }
    }

    class ChapterOverview {
        private int ID;
        private String chapterTitle;
        private int nCourse;
        private int nQuestions;
        private int nQuizes;
        private String folder;

        private ArrayList<String> courses;

        ChapterOverview(int _id, String _title, int _nCourse, int _nQuestions, int _nQuizes, String _folder, ArrayList<String> _courses) {
            this.ID = _id;
            this.chapterTitle = _title;
            this.nCourse = _nCourse;
            this.nQuestions = _nQuestions;
            this.nQuizes = _nQuizes;
            this.folder = _folder;
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

        public String getFolder() {
            return folder;
        }
    }
}
