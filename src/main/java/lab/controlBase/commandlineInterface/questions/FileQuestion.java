package lab.controlBase.commandlineInterface.questions;

import lab.controlBase.commandlineInterface.LoggingBase;

import java.io.File;

/**
 * Wrapper class to use a question for the wizard
 */
public class FileQuestion extends Question {


    public FileQuestion(String question) {
        super(question);
    }


    @Override
    public boolean isValid() {
        return question != null && question.length() > 0;
    }

    /**
     * Prints the question and the possible answers with their generated mapping.
     * Waits for a correct user input (as a number) and returns the corresponding answer.
     *
     * @return sing value from answers
     */
    @Override
    public File ask() {
        String answer;
        File file = null;
        boolean error;

        do {

            error = false;
            super.ask();
            answer = ipt.next();
            file = new File(answer);


            if (!file.isFile()) {
                LoggingBase.log.warn(file.getAbsolutePath() + " is not a file");
                error = true;
            }

        } while (error);


        log.info("Selected file: " + file.getAbsolutePath());

        return file;
    }


}
