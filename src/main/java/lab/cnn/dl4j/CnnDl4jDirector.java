package lab.cnn.dl4j;

import lab.controlBase.Director;
import lab.controlBase.commandlineInterface.questions.DoubleQuestion;
import lab.controlBase.commandlineInterface.questions.IntegerQuestion;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.controlBase.commandlineInterface.questions.TextQuestion;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

/**
 * director for dl4j cnn generation
 */
public class CnnDl4jDirector extends Director {
    CnnDl4jBuilder builder;

    @Override
    /**
     * the main workflow of the class
     * uses questionnaires to create network
     */
    public CnnDl4j construct() {
        // get user input for configuration
        Object[] answers = createQuestionaire().ask();
        int numConvLayers = (int) answers[4];

        // create builder
        builder = new CnnDl4jBuilder(
                (double) answers[1],
                (int) answers[5],
                (int) answers[3],
                (int) answers[2]);

        // init builder configuration
        builder.buildConfig();

        // add convolution layers
        for (int i = 0; i < numConvLayers; i++) {
            builder.addConvolutionLayer(askNumOfNodes(i));
        }

        // build wrapper object
        return new CnnDl4j((String) answers[0],
                (int) answers[5], (int) answers[3],
                (int) answers[3],
                (int) answers[2]);
    }

    /**
     * finishes the network construction and returns the network
     * can only be executed after constructing the main logic;
     *
     * @return the finished network
     */
    public MultiLayerNetwork finishConstructingNetwork() {
        return builder.finishConfig();
    }

    /**
     * Question used for convolution layer configuration
     *
     * @param layerNum number of current layer
     * @return number of nodes for layer
     */
    private int askNumOfNodes(int layerNum) {
        return new IntegerQuestion("What's the size of convolutionLayer: " + layerNum + "?").ask();
    }

    /**
     * creates the questionnaire for basic configuration
     *
     * @return the questionnaire
     */
    private Questionaire createQuestionaire() {
        // config questionaire
        Questionaire quest = new Questionaire("CNN");

        quest.addQuestion(new TextQuestion("What is this CNN called?"));
        quest.addQuestion(new DoubleQuestion("What is the learning rate?"));
        quest.addQuestion(new IntegerQuestion("What are the incoming channels? (1=greyscale,3=rgb)"));
        quest.addQuestion(new IntegerQuestion("What's the size of one side?"));
        quest.addQuestion(new IntegerQuestion("Number of convolution layers"));
        quest.addQuestion(new IntegerQuestion("Number of outputs"));

        return quest;
    }
}
