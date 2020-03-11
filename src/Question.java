import java.util.ArrayList;

public class Question{
    int ID; // unique ID for avery question
    String core; // Question declarative text ex: What sign does it represent ?
    ArrayList<String> propositions; // since all questions are QCMs ~ contains all propositions
    ArrayList<Boolean> correct_answers; // for each proposition ^^ a boolean indicating if correct or false
}
