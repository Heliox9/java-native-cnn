package lab.controlBase.commandlineInterface.questions;

import lab.controlBase.commandlineInterface.LoggingBase;

import java.util.ArrayList;

/**
 * contains multiple questions for consistent execution
 */
public class Questionaire extends LoggingBase {

    private String configName;

    private ArrayList<Question> questions;

    public Questionaire(String configName) {
        questions = new ArrayList<>();
        this.configName = configName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * add a question object to working set
     *
     * @param question object to add
     * @return true if add worked
     */
    public boolean addQuestion(Question question) {
        if (question.isValid()) {
            return questions.add(question);
        }
        return false;
    }


    /**
     * remove a question object from working set
     *
     * @param question object to remove
     * @return true if remove worked
     */
    public boolean removeQuestion(Question question) {
        for (Question q : questions) {
            if (q.getQuestion().equals(question.getQuestion())) {
                questions.remove(q);
                return true;
            }
        }
        return false;
    }

    /**
     * calls ask method of all questions and collects the results
     *
     * @return collected results from all questions
     */
    public Object[] ask() {
        printSolidHLine();
        log.info("Configuring: " + configName);

        ArrayList<Object> answers = new ArrayList<>();

        for (Question q : questions) {
            answers.add(q.ask());
        }

        return answers.toArray(new Object[questions.size()]);
    }
}
