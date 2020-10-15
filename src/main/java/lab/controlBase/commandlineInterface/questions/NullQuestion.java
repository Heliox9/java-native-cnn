package lab.controlBase.commandlineInterface.questions;

/**
 * placeholder to fill a questionaire for further extension
 */
public class NullQuestion extends Question {

    public NullQuestion() {
        super(null);
    }

    /**
     * null
     */
    @Override
    public String ask() {
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
