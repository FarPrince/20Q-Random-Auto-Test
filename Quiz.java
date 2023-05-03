import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Quiz {
private final Map<String, String> termsAndDefinitions;
private List<String> quizDefinitions;

public Quiz() throws FileNotFoundException {
    // Import terms and definitions from file
    this.termsAndDefinitions = new HashMap<>();
    File file = new File("terms.txt");
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] parts = line.split(":");
        if (parts.length == 2) {
            String term = parts[0].trim();
            String definition = parts[1].trim();
            termsAndDefinitions.put(term, definition);
        }
    }

    // Select 20 random definitions for the quiz
    this.quizDefinitions = new ArrayList<>(termsAndDefinitions.values());
    Collections.shuffle(quizDefinitions);
    this.quizDefinitions = quizDefinitions.subList(0, 20);
}    

public void runQuiz() {
    Scanner scanner = new Scanner(System.in);
    int numCorrect = 0;

    for (String definition : quizDefinitions) {
        System.out.println("Definition: " + definition);
        System.out.print("Term: ");
        String term = scanner.nextLine();
        if (termsAndDefinitions.containsKey(term) && termsAndDefinitions.get(term).equals(definition)) {
            System.out.println("Correct!");
            numCorrect++;
        } else if (checkAnswerClose(term, definition)) {
            System.out.println("Close enough! The correct term is: " + getTermByDefinition(definition));
            numCorrect++;
        } else {
            System.out.println("Incorrect. The correct term is: " + getTermByDefinition(definition));
        }
    }

    System.out.println("Quiz complete. You got " + numCorrect + " out of 20 correct.");
    scanner.close();
}

private boolean checkAnswerClose(String answer, String definition) {
    String correctTerm = getTermByDefinition(definition);
    if (correctTerm.equalsIgnoreCase(answer)) {
        return true;
    }
    String[] correctTermParts = correctTerm.split("\\s+");
    for (String part : correctTermParts) {
        if (part.equalsIgnoreCase(answer)) {
            return true;
        }
    }
    return false;
}

private String getTermByDefinition(String definition) {
    for (Map.Entry<String, String> entry : termsAndDefinitions.entrySet()) {
        if (entry.getValue().equals(definition)) {
            return entry.getKey();
        }
    }
    return "";
}

public static void main(String[] args) {
    try {
        Quiz quiz = new Quiz();
        quiz.runQuiz();
    } catch (FileNotFoundException e) {
        System.err.println("Could not find terms.txt file.");
        e.printStackTrace();
    }
}
}