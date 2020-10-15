package lab.controlBase.commandlineInterface.questions;

/**
 * generic text String question
 */
public class TextQuestion extends Question {

    public TextQuestion(String question) {
        super(question);
    }

    /**
     * the input line or null
     */
    @Override
    public String ask() {
        super.ask();

        String text;
        text = ipt.nextLine();

        if (text.isEmpty()) return null;
        return text;
    }

    @Override
    public boolean isValid() {
        return question != null && question.length() > 0;
    }
}
