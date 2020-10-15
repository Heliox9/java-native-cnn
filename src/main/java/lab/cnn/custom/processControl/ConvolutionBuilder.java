package lab.cnn.custom.processControl;

import lab.controlBase.Builder;
import lab.controlBase.commandlineInterface.questions.IntegerQuestion;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.cnn.custom.network.layers.ConvolutionLayer;

public class ConvolutionBuilder extends Builder {
    private static int numOfConv;
    private int thisConvNum;

    public ConvolutionBuilder() {

    }

    @Override
    /**
     * builds a convolution layer based on the questionaire configuration
     */
    public ConvolutionLayer build(int numPrevNodes) {
        super.build(numPrevNodes);

        return new ConvolutionLayer(config, numPrevNodes);
    }

    @Override
    protected void fillQuestionaire() {
        thisConvNum = numOfConv;
        numOfConv++;
        questionaire = new Questionaire("convolution-layer(" + thisConvNum + ")");
        questionaire.addQuestion(new IntegerQuestion("Number of nodes: "));
        // questionaire.addQuestion(new ChooseQuestion("Activation function: ", Enums.ActivationFunction.class));
    }
}
