package lab.controlBase.commandlineInterface;


import lab.controlBase.commandlineInterface.questions.ChooseQuestion;
import lab.controlBase.commandlineInterface.questions.Question;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//TODO
public class QuestionaireTest extends CommandlineTest {

    static Questionaire subject;
    static String questionText;
    static Question question;
    static String[] options;


    @BeforeAll
    static void setUpAll() {
        questionText = "This is a test";
        subject = new Questionaire("Test");

        options = new String[2];
        options[0] = "First Option";
        options[1] = "Second Option";

        question = new ChooseQuestion(questionText, options);

        subject.addQuestion(question);
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

    @Test
    public void addQuestion() {
        String newQuestionText = "This is a new test";


        Assertions.assertTrue(subject.addQuestion(new ChooseQuestion(newQuestionText, options)));
    }

    @Test
    public void removeQuestion() {
        Assertions.assertTrue(subject.removeQuestion(question));
    }


}