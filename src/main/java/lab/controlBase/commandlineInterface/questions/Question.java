package lab.controlBase.commandlineInterface.questions;

import lab.controlBase.commandlineInterface.LoggingBase;

import java.util.Scanner;

/**
 * abstract base for all question objects
 */
public abstract class Question extends LoggingBase {
    protected String question;
    protected Scanner ipt = new Scanner(System.in);

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    /**
     * function to be overriden.
     * prints a line and asks a question
     *
     * @return always null
     */
    public Object ask() {
        printHLine();
        log.info(question);

        return null;
    }

    public abstract boolean isValid();

}
