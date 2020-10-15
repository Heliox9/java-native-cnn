package lab.controlBase.commandlineInterface.questions;

public class DoubleQuestion extends Question {

    public DoubleQuestion(String question) {
        super(question);
    }

    /**
     * prints questions and waits for integer input
     * if input is not castable or smaller 1 errors are caught and the question is repeated
     *
     * @return number greater 0
     */
    @Override
    public Double ask() {
        super.ask();


        Double num = null;

        try {
            num = Double.parseDouble(ipt.next());


            if (num < 0) {
                log.error("Illegal input: " + num + " (0> allowed)");
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
