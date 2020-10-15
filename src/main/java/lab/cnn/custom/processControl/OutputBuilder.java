package lab.cnn.custom.processControl;

import lab.controlBase.Builder;
import lab.controlBase.commandlineInterface.questions.IntegerQuestion;
import lab.controlBase.commandlineInterface.questions.NullQuestion;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.cnn.custom.network.layers.OutputLayer;

public class OutputBuilder extends Builder {
    public OutputBuilder() {
        questionaire = new Questionaire("output-layer");
    }

    @Override
    protected void fillQuestionaire() {
        questionaire = new Questionaire("output-layer");
        questionaire.addQuestion(new NullQuestion());
        questionaire.addQuestion(new NullQuestion());
//        questionaire.addQuestion(new ChooseQuestion("Activation function: ", Enums.ActivationFunction.class));
        questionaire.addQuestion(new IntegerQuestion("Lowest possible number: "));
        questionaire.addQuestion(new IntegerQuestion("Highest possible number: "));
    }

    @Override
    public OutputLayer build(int numPrevNodes) {
        super.build(numPrevNodes);
        return new OutputLayer(config, numPrevNodes);
    }
}
