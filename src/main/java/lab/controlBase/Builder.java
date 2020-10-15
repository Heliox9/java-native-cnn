package lab.controlBase;

import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.cnn.custom.network.layers.Layer;

/**
 * abstract base for object builders
 */
public abstract class Builder {
    protected Questionaire questionaire;
    protected Object[] config;


    public Questionaire getQuestionaire() {
        return questionaire;
    }

    /**
     * base implementation to be overriden by children
     * handles questionaire in order to have single point of change if the configuration menu is changed
     *
     * @param numPrevNodes number of nodes in previous layer
     * @return null because this function in incomplete
     */
    public Layer build(int numPrevNodes) {

        fillQuestionaire();
        config = questionaire.ask();

        return null;
    }

    protected abstract void fillQuestionaire();
}
