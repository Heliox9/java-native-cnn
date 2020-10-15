package lab.controlBase.commandlineInterface.questions;

import java.util.Arrays;

/**
 * Wrapper class to use a question for the wizard
 */
public class ChooseQuestion extends Question {

    private String[] answers;

    public ChooseQuestion(String question, String[] answers) {
        super(question);
        this.answers = answers;
    }

    public ChooseQuestion(String question, Class<? extends Enum<?>> answerEnum) {
        //parses a Enum into a String[]
        this(question,
                Arrays.stream(answerEnum.getEnumConstants())
                        .map(Enum::name).toArray(String[]::new));
    }

    @Override
    public boolean isValid() {
        return question != null && question.length() > 0 && answers.length > 0;
    }

    /**
     * Prints the question and the possible answers with their generated mapping.
     * Waits for a correct user input (as a number) and returns the corresponding answer.
     *
     * @return sing value from answers
     */
    @Override
    public String ask() {
        super.ask();

        String answer = null;
        String input = null;
        int inputNum;
        boolean error;


        for (int i = 1; i <= answers.length; i++) {
            log.info("[" + i + "] " + answers[i - 1]);
        }

        do {
            error = false;
            try {
                input = ipt.next();
                inputNum = Integer.parseInt(input);
                answer = answers[inputNum - 1];
            } catch (Exception e) {
                log.info("Invalid input:" + input + " (Only numbers from 1 to " + answers.length + " are valid.)");
                error = true;
            }
        } while (error);

        log.info("Option: " + answer + " selected");

        return answer;
    }


    public String[] getAnswers() {
        return answers;
    }
}
