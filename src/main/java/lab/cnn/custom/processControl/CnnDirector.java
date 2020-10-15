package lab.cnn.custom.processControl;

import lab.cnn.custom.filehandlers.image.ImageHandler;
import lab.cnn.custom.network.layers.Layer;
import lab.controlBase.Builder;
import lab.controlBase.Director;
import lab.controlBase.commandlineInterface.LoggingBase;
import lab.controlBase.commandlineInterface.questions.DoubleQuestion;
import lab.controlBase.commandlineInterface.questions.IntegerQuestion;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.controlBase.commandlineInterface.questions.TextQuestion;
import lab.cnn.custom.network.CNN;


/**
 * Directs the creation of a new lab.custom.cnn
 */
public class CnnDirector extends Director {


    @Override
    public CNN construct() {

        Object[] answers = createQuestionaire().ask();

        createBuilders((Integer) answers[3]);

        return createCNN((String) answers[0], (double) answers[1], (Integer) answers[2]);
    }

    private CNN createCNN(String name, double learningRate, int inputPixelsSquared) {
        // starting construction
        LoggingBase.log.info("Constructing CNN");
        ImageHandler imageHandler = new ImageHandler(inputPixelsSquared);

        CNN cnn = new CNN(name, imageHandler);

        int prevLayer = imageHandler.getTargetSizeSquared();
        Layer newLayer = null;

        // building lab.custom.cnn
        try {
            for (Builder b : builders) {
                newLayer = b.build(prevLayer);
                cnn.addLayer(newLayer);
                prevLayer = newLayer.getNumOfNodes();
            }
        } catch (NullPointerException n) {
            LoggingBase.log.error("No builders initialized", n);
        }

        cnn.setLearningRate(learningRate);

        return cnn;
    }

    private void createBuilders(int numConvLayers) {
        // adding builders
        addBuilder(new NormalizingBuilder());
        for (int i = 0; i < numConvLayers; i++) {
            addBuilder(new ConvolutionBuilder());
        }
        addBuilder(new OutputBuilder());
    }

    private Questionaire createQuestionaire() {
        // config questionaire
        Questionaire quest = new Questionaire("CNN");

        quest.addQuestion(new TextQuestion("What is this CNN called?"));
        quest.addQuestion(new DoubleQuestion("What is the learning rate?"));
        quest.addQuestion(new IntegerQuestion("How many pixels (squared) are the images supposed to have?"));
        quest.addQuestion(new IntegerQuestion("Number of convolution layers"));

        return quest;
    }
}
