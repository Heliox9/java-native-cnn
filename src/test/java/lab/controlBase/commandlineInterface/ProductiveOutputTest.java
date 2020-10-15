package lab.controlBase.commandlineInterface;


import org.junit.jupiter.api.Test;


public class ProductiveOutputTest extends CommandlineTest {


    @Test
    public void print() {
        int expected = 42;
        ProductiveOutput subject = new ProductiveOutput(expected, 0.53);

        subject.print();

        assertContains("42");
        assertContains("53");
    }
}