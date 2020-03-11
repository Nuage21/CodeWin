import java.util.ArrayList;

class Course {
    String title; // Course title
    String core; // the core (explicative) text
    boolean has_questions; // questions about this course ?
    String image_folder; // relative image folder ex: ./courses/theme1/img/course1/
    ArrayList<String> images = new ArrayList<>(); // list of support paths
    ArrayList<Question> questions = new ArrayList<>(); // Course issued questions

    Course(String json_path)
    {
        // load the class from the JSON @json_path
        // call load_json
    }

    public void load_json(String json_path)
    {

    }

    public void save_to_json(String json_path)
    {
        // save this class in the JSON file @json_path
    }
}