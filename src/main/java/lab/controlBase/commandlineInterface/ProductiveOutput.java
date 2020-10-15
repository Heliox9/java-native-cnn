package lab.controlBase.commandlineInterface;

/**
 * Basic output for a productive run
 */
public class ProductiveOutput extends Output {

    private int solution;
    private double chance;

    public ProductiveOutput(int solution, double chance) {
        this.solution = solution;
        this.chance = chance;
    }

    @Override
    public void print() {
        printHLine();
        log.info("The CNN predicts: " + solution + " with a value of: " + chance);
    }
}
