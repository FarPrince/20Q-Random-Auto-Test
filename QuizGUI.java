import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QuizGUI implements ActionListener {
    private final Map<String, String> termsAndDefinitions;
    private List<String> quizDefinitions;
    private int currentQuestion = 0;
    private int numCorrect = 0;
    private JFrame frame;
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JTextArea definitionTextArea;
    private JPanel answerPanel;
    private JLabel answerLabel;
    private JTextField answerTextField;
    private JButton submitButton;

    public static void main(String[] args) {
        try {
            QuizGUI gui = new QuizGUI();
            gui.runQuiz();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
    

    public QuizGUI() throws FileNotFoundException {
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
        // Set up the GUI
        frame = new JFrame("Quiz");
        questionPanel = new JPanel();
        questionLabel = new JLabel();
        definitionTextArea = new JTextArea();
        answerPanel = new JPanel();
        answerLabel = new JLabel("Answer:");
        answerTextField = new JTextField();
        submitButton = new JButton("Submit");

        submitButton.addActionListener(this);
        frame.getRootPane().setDefaultButton(submitButton);

        questionPanel.setLayout(new GridLayout(2, 1));
        questionPanel.add(questionLabel);
        questionPanel.add(definitionTextArea);

        answerPanel.setLayout(new GridLayout(2, 1));
        answerPanel.add(answerLabel);
        answerPanel.add(answerTextField);

        frame.setLayout(new BorderLayout());
        frame.add(questionPanel, BorderLayout.NORTH);
        frame.add(answerPanel, BorderLayout.CENTER);
        frame.add(submitButton, BorderLayout.SOUTH);

        // Set font and size of text
        Font font = new Font("Cambria", Font.PLAIN, 20);
        questionLabel.setFont(font);
        definitionTextArea.setFont(font);
        answerLabel.setFont(font);
        answerTextField.setFont(font);
        submitButton.setFont(font);

        // Display the first question
        showQuestion();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void showQuestion() {
        String definition = quizDefinitions.get(currentQuestion);
        String questionText = "Question " + (currentQuestion + 1) + " of 20:";
        questionLabel.setText(questionText);
        definitionTextArea.setText(definition);
        answerTextField.setText("");
    }

    private void showResult(boolean correct) {
        String resultText;
        if (correct) {
            resultText = "Correct!";
            numCorrect++;
        } else {
        resultText = "Incorrect. The correct answer is: " + getTermByDefinition(quizDefinitions.get(currentQuestion));
        }
        answerLabel.setText(resultText);
        }

        private String getTermByDefinition(String definition) {
            for (Map.Entry<String, String> entry : termsAndDefinitions.entrySet()) {
                if (entry.getValue().equals(definition)) {
                    return entry.getKey();
                }
            }
            return "";
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String userAnswer = answerTextField.getText();
            String correctAnswer = getTermByDefinition(quizDefinitions.get(currentQuestion));
            boolean isCorrect = userAnswer.equalsIgnoreCase(correctAnswer);
            showResult(isCorrect);
            currentQuestion++;
            if (currentQuestion == quizDefinitions.size()) {
                // Quiz is over
                double score = (double) numCorrect / quizDefinitions.size() * 100;
                String message = String.format("Quiz complete! You got %d out of %d questions correct (%.1f%%)", numCorrect, quizDefinitions.size(), score);
                questionLabel.setText(message);
                definitionTextArea.setText("");
                answerTextField.setEditable(false);
                submitButton.setEnabled(false);
            } else {
                showQuestion();
            }
        }
    }