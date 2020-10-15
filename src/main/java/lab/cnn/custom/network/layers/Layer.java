package lab.cnn.custom.network.layers;

import lab.cnn.custom.network.Enums;
import lab.cnn.custom.network.Node;
import org.apache.commons.math3.analysis.function.Sigmoid;

import java.io.Serializable;
import java.util.Arrays;

/**
 * abstract class for all layers
 */
public abstract class Layer implements Serializable {
    static int idCount;
    protected Node[] nodes;
    protected Object[] config;
    int numOfNodes;
    int id;
    Enums.ActivationFunction function;


    public Layer(Object[] config) {
        this.config = config;

        try {
            numOfNodes = (Integer) config[0];

        } catch (Exception e) {
            //Nothing to do here
        }

        function = Enums.ActivationFunction.SIGMOID;
        //function = (Enums.ActivationFunction) Enums.get((String) config[1]);
        id = idCount++;
    }

    public Node[] getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "\nLayer[" + id + "]{" +
                "nodes=" + Arrays.toString(nodes) +
                '}';
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public void setNumOfNodes(int numOfNodes) {
        this.numOfNodes = numOfNodes;
    }

    protected void genNodes(int numOfInputs) {
        if (numOfNodes < 0) {
            numOfNodes = numOfNodes * (-1);
        }

        nodes = new Node[numOfNodes];
        for (int i = 0; i < numOfNodes; i++) {
            nodes[i] = new Node(numOfInputs, this);
        }
    }

    /**
     * activation function for the layer
     * currently static rectified
     * https://machinelearningmastery.com/rectified-linear-activation-function-for-deep-learning-neural-networks/
     *
     * @param value any double value for node
     * @return flattened to 0 for negative, keep positive
     */
    public double activationFunction(double value) {
        if (function == Enums.ActivationFunction.RELU) {
            if (value < 0) {
                return 0;
            }
            return value;
        } else if (function == Enums.ActivationFunction.SIGMOID) {
            return new Sigmoid().value(value);

        }
        throw new RuntimeException("No activation function defined");
    }


    public double derivative(double value) {
        return new Sigmoid().derivative().value(value);

        // return value * (1 - value);
    }

    public double prevValueSum() {
        double value = 0;

        for (Node n : nodes
        ) {
            value += n.getLastValue();

        }
        return value;
    }
}
