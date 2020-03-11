import java.util.ArrayList;

class Course {
    String title; // Course title
    String core; // the core (explicative) text
    ArrayList<String> images_urls = new ArrayList<>(); // list of support paths
    ArrayList<Question> questions = new ArrayList<>(); // Course issued questions
}