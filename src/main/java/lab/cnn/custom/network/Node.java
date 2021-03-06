package lab.cnn.custom.network;

import lab.cnn.custom.network.layers.ConvolutionLayer;
import lab.cnn.custom.network.layers.Layer;
import lab.cnn.custom.network.layers.OutputLayer;

import java.io.Serializable;

/**
 * Represents a single node within a layer of a CNN
 */
public class Node implements Serializable {
    //forward
    double bias;
    int calcErrorCounter;
    private Filter filter;
    private Layer containingLayer;
    //backward
    private double lastValue;
    private double[] lastInputs;
    private double error;

    public Node(int numInputs, Layer containingLayer) {
        this.filter = new Filter(numInputs);
        bias = Math.random();
        this.containingLayer = containingLayer;

    }

    /**
     * calculates a single value for the node based on the inputs, weights and bias
     * calculated value is stored in lastValue to be used in backpropogation
     *
     * @param inputs array of values generated by nodes in the previous layer
     * @return single calculated value
     */
    public double calculateValue(double[] inputs) {
        lastInputs = inputs;
        double out = 0;

        // apply weighted filter
        out = filter.apply(inputs);

        // add the bias
        out += bias;

        lastValue = containingLayer.activationFunction(out);
        return lastValue;
    }

    public double getError() {
        return error;
    }

    public double getWeight(int position) {
        return filter.getWeights()[position];
    }

    public void resetError() {
        this.error = 0;
    }

    /**
     * calculates the error of a node
     * <p>
     * outputlayer:
     * the value based on the target activation of the node
     * <p>
     * convolutionLayer:
     * called for each weight of a following node
     *
     * @param value outputlayer : expected value 1/0
     *              * convLayer : error(following neuron)*weight(following neuron)
     */
    public void calculateError(double value) {
        if (containingLayer instanceof OutputLayer) {
            error = value - lastValue;
            error *= containingLayer.derivative(lastValue);

            calcErrorCounter = 1;
            // LoggingBase.log.trace("expected: " + value + " -> actual:" + lastValue);
        } else if (containingLayer instanceof ConvolutionLayer) {
            error += value * containingLayer.derivative(lastValue);
            calcErrorCounter++;
        }


    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    /**
     * adjusts all weights and the bias of the node based on the parameters and the error of the node
     * calculate error has to be called before attempting to update the weights
     *
     * @param learningRate the rate at which all weights are to be adjusted by (usually between 0-1)
     * @param prevLayer    the layer the precedes this one (needed to adjust weights based on the values of the previous run)
     */
    public void updateWeights(double learningRate, Layer prevLayer) {
        //calculate change factor
        double factor = learningRate * error;//* lastValue;

        // LoggingBase.log.trace("adjustment factor: " + factor);

        filter.updateWeights(factor, prevLayer);

        // adjust bias
        bias += factor;
    }

    @Override
    public String toString() {
        return "\nNode{" +
                "bias=" + bias +
                "," + filter +
                "}";
    }
}
