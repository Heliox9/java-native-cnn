package lab.controlBase.commandlineInterface.questions;

import lab.controlBase.commandlineInterface.LoggingBase;
import lab.cnn.custom.filehandlers.image.ImageHandler;

import java.io.File;

/**
 * Wrapper class to use a question for the wizard
 */
public class FolderQuestion extends Question {

    private ImageHandler handler;

    public FolderQuestion(String question) {
        super(question);
        this.handler = handler;
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
        File folder = null;
        boolean error;

        do {

            error = false;
            super.ask();
            answer = ipt.next();
            folder = new File(answer);

            if (!folder.isDirectory()) {
                if (folder.isFile()) {
                    folder = folder.getParentFile();
                    LoggingBase.log.info("Entered a file! Using parent");
                } else {
                    LoggingBase.log.warn(folder.getAbsolutePath() + " is not a folder");
                    error = true;
                }
            }


        } while (error);
        log.info("Selected folder: " + folder.getAbsolutePath());

        return folder;
    }


}
