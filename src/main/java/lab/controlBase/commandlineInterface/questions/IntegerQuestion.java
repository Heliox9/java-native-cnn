package lab.controlBase.commandlineInterface.questions;

/**
 * question for int input
 */
public class IntegerQuestion extends Question {

    public IntegerQuestion(String question) {
        super(question);
    }

    /**
     * prints questions and waits for integer input
     * if input is not castable or smaller 1 errors are caught and the question is repeated
     *
     * @return number greater 0
     */
    @Override
    public Integer ask() {
        super.ask();


        Integer num = null;

        try {
            num = Integer.parseInt(ipt.next());


            if (num < 0) {
                log.error("Illegal input: " + num + " (only full numbers greater or equal 0 allowed)");
                num = ask();
            }
        } catch (NumberFormatException n) {
            // catch non number input
            log.error("Illegal input: " + num + " (numbers only)");
            num = ask();
        }

        return num;
    }

    @Override
    public boolean isValid() {
        return question != null && question.length() > 0;
    }
}
