package lab.cnn.custom.processControl;

import lab.controlBase.Builder;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.cnn.custom.network.Enums;
import lab.cnn.custom.network.layers.NormalizingLayer;

public class NormalizingBuilder extends Builder {
    public NormalizingBuilder() {
        questionaire = new Questionaire("normalizing-layer");
    }

    @Override
    public NormalizingLayer build(int numPrevNodes) {
        config = new Object[2];
        config[0] = numPrevNodes;
        config[1] = Enums.ActivationFunction.SIGMOID + "";

        NormalizingLayer l = new NormalizingLayer(config, numPrevNodes);

        l.setNumOfNodes(numPrevNodes);
        return l;
    }

    @Override
    protected void fillQuestionaire() {
        //no Question
    }
}
