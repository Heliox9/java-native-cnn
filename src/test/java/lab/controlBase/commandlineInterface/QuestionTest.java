package lab.controlBase.commandlineInterface;


import lab.controlBase.commandlineInterface.questions.ChooseQuestion;
import lab.controlBase.commandlineInterface.questions.Question;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//TODO
public class QuestionTest extends CommandlineTest {

    static String questionText;
    static Question subject;
    static String[] options;


    @BeforeAll
    static void setUpAll() {
        questionText = "This is a test";

        options = new String[2];
        options[0] = "First Option";
        options[1] = "Second Option";

        subject = new ChooseQuestion(questionText, options);
    }

    /**
     * TODO input number to test
     */
    @Disabled
    @Test
    public void ask() {
        subject.ask();

        input("1");

        assertContains(questionText);
    }

}