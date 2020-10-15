package lab.controlBase.commandlineInterface.questions;

import lab.cnn.custom.filehandlers.image.ImageData;
import lab.cnn.custom.filehandlers.image.ImageHandler;

/**
 * Wrapper class to use a question for the wizard
 */
public class ImageQuestion extends Question {

    private ImageHandler handler;

    public ImageQuestion(String question, ImageHandler handler) {
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
    public ImageData ask() {
        String answer;
        ImageData data;

        do {
            super.ask();
            answer = ipt.next();
            data = handler.handle(answer);

        } while (data == null);


        log.info("Option: " + answer + " selected");

        return data;
    }


}
