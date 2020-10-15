package lab.controlBase.commandlineInterface;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class CommandlineTest {

    private static ByteArrayOutputStream byteArrayOutputStream;
    private static PrintStream console;

    @BeforeEach
    public void setUp() throws Exception {
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            console = System.out;


            System.setOut(new PrintStream(byteArrayOutputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void teardown() throws Exception {
        System.setOut(console);
    }

    protected void input(String text) {
        InputStream sysInBackup = System.in;

        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes());
        System.setIn(in);


        System.setIn(sysInBackup);
    }


    protected String getOutput() {
        return byteArrayOutputStream.toString();
    }

    protected void assertContains(String expected) {
        Assertions.assertTrue(getOutput().contains(expected), getOutput() + " does not include: " + expected);
    }

    protected void assertNotContains(String expected) {
        Assertions.assertFalse(getOutput().contains(expected), getOutput() + " includes: " + expected);
    }


}
