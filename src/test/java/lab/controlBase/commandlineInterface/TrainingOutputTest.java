package lab.controlBase.commandlineInterface;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TrainingOutputTest extends CommandlineTest {

    static TrainingOutput subject;
    static int key;
    static double value;

    @BeforeAll
    static void setUpAll() {
        key = 42;
        value = 80.80;

        subject = new TrainingOutput();

    }

    @Test
    public void fullFlow() {
        // Add pair
        subject.putProbability(key, value);

        Assertions.assertEquals(1,
                subject.getProbabilities().size());

        // Output
        subject.print();
        // assertContains(key + "");

        // Remove pair
        subject.removeProbability(key);

        Assertions.assertEquals(0,
                subject.getProbabilities().size());
    }
}