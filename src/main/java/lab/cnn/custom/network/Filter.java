package lab.cnn.custom.network;

import lab.controlBase.commandlineInterface.LoggingBase;
import lab.cnn.custom.network.layers.Layer;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a filter that is defined for a layer of a CNN
 * Filters process data of previous nodes into new nodes
 * <p>
 * Bias and filter are initialized with random
 */
public class Filter implements Serializable {
    Double[] weights;
    int numOfWeights;
    private double prevValue;

    public Filter(int numOfWeights) {
        this.numOfWeights = numOfWeights;
        this.weights = new Double[numOfWeights];
        initWeights();
    }

    public Double[] getWeights() {
        return weights;
    }

    /**
     * initializes weights with random values between -1 and 1
     */
    private void initWeights() {
        for (int i = 0; i < numOfWeights; i++) {
            weights[i] = (Math.random() * 2) - 1;
        }
    }

    /**
     * apply the filter to a given set of inputs
     * calculates a single value based on inputs and weights
     *
     * @param inputs inputs of previous nodes ( has to be same size as numOfWeights)
     * @return single calculated value or exception if input doesn't match the format
     */
    public double apply(double[] inputs) throws RuntimeException {
        if (numOfWeights != inputs.length) {
            LoggingBase.log.error("Invalid filter configuration, weights and inputs don't have the same length (" + numOfWeights + "!=" + inputs.length + ")");
        }

        double output = 0;

        double input;
        for (int i = 0; i < inputs.length; i++) {
            input = inputs[i];

            if (input > 0) {
                output += input * weights[i];
            }
        }

        prevValue = output;

        return output;

    }

    /**
     * update all weights based on the change rate and the linked layer
     *
     * @param value     the rate to change the weights by
     * @param prevLayer the linked layer to get nodes connected to each weight
     */
    public void updateWeights(double value, Layer prevLayer) {
        // adjust weights

        for (int i = 0; i < numOfWeights; i++) {
            updateWeight(i, value, prevLayer.getNodes()[i]);
        }
    }

    /**
     * @param pos   position for the weight to be changed
     * @param value calculated value to change by
     * @param node  the node corresponding to the weight in order to customize rate of change
     */
    public void updateWeight(int pos, double value, Node node) {
        // LoggingBase.log.trace("Weight " + pos + " before: " + weights[pos]);
        weights[pos] = weights[pos] + (value * node.getLastValue());
        // LoggingBase.log.trace("Weight " + pos + " after: " + weights[pos]);
    }

    @Override
    public String toString() {
        return
                "weights{" + Arrays.toString(weights) +
                        '}';
    }
}
