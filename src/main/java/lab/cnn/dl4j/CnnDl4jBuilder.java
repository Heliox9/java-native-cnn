package lab.cnn.dl4j;

import lab.controlBase.Builder;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Builder for dl4j MultiLayerNetwork
 * Base configuration set at instance initiation
 * layer confguration through function calls
 */
public class CnnDl4jBuilder extends Builder {
    MultiLayerConfiguration config = null;
    int seed;    // number used to initialize a pseudorandom number generator.
    double learningRate;
    int outputNum;
    int height; // of input images
    int width; // of input images
    int channels; // of input images (1=greyscale, 3=rgb)
    private NeuralNetConfiguration.Builder confBuilder = null;
    private NeuralNetConfiguration.ListBuilder listBuilder = null;
    private int layerCount = -1;

    /**
     * minimal constructor
     *
     * @param learningRate greater 0, smaller 1
     * @param outputNum    number of output classes (possible outputs)
     * @param inputSize    size for one side of input images (images will be square)
     * @param channels     channels of input images (1=greyscale, 3=rgb)
     */
    public CnnDl4jBuilder(double learningRate, int outputNum, int inputSize, int channels) {
        this(learningRate, outputNum, inputSize, inputSize, channels);
    }

    public CnnDl4jBuilder(double learningRate, int outputNum, int height, int width, int channels) {
        this(learningRate, outputNum, height, width, channels, (int) (Math.random() * 10000));
    }

    /**
     * full constructor
     *
     * @param learningRate greater 0, smaller 1
     * @param outputNum    number of output classes (possible outputs)
     * @param height       size of y dimension for input images
     * @param width        size of x dimension for input images
     * @param channels     channels of input images (1=greyscale, 3=rgb)
     * @param seed         set constant initialization parameter
     */
    public CnnDl4jBuilder(double learningRate, int outputNum, int height, int width, int channels, int seed) {
        this.learningRate = learningRate;
        this.outputNum = outputNum;
        this.height = height;
        this.width = width;
        this.channels = channels;
        this.seed = seed;
    }

    @Override
    /**
     * unused
     */
    protected void fillQuestionaire() {

    }

    /**
     * configuration function for network builder
     * adds a convolution layer and matching pooling layer
     *
     * @param numOfNodes number of nodes for the convolution layer
     */
    public void addConvolutionLayer(int numOfNodes) {
        // check config initialization
        if (config != null) {
            layerCount = -1;
            config = null;
            listBuilder = null;
        }

        // fetch list builder on first call
        if (listBuilder == null) listBuilder = confBuilder.list();

        // configure convolution layer
        ConvolutionLayer.Builder builder = new ConvolutionLayer.Builder();
        if (layerCount < 0) {
            builder = builder.nIn(channels);
            layerCount++;
        }
        builder = builder
                .nOut(numOfNodes)
                .stride(1, 1)
                .activation(Activation.IDENTITY);

        // add convolution and pooling layer
        listBuilder = listBuilder
                .layer(layerCount++, builder.build())
                .layer(layerCount++, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .stride(2, 2)
                        .build());
    }

    /**
     * finalizes build process
     *
     * @return built and initialized network
     */
    public MultiLayerNetwork finishConfig() {
        // add dense layer to convert from convolution to output
        listBuilder = listBuilder
                .layer(layerCount++, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500)
                        .build());

        // add output layer with defined number of outputs
        listBuilder = listBuilder
                .layer(layerCount++, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .build());

        // complete setup
        config = listBuilder.setInputType(InputType.convolutionalFlat(height, width, channels))
                .build();

        // build and initialize network
        MultiLayerNetwork network = new MultiLayerNetwork(config);
        network.init();
        return network;
    }

    /**
     * create base configuration
     */
    public void buildConfig() {
        confBuilder = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.0005) // ridge regression value
                .learningRate(learningRate)
                .weightInit(WeightInit.XAVIER);
    }
}
