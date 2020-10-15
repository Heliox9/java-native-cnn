package lab.controlBase.commandlineInterface;

/**
 * Abstract base for any output
 */
public abstract class Output extends LoggingBase {
    private String wrapperText;

    public abstract void print();
}
