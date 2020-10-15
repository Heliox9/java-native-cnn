package lab.cnn.custom.network.layers;

import lab.cnn.custom.filehandlers.image.ImageData;

import java.util.HashMap;
import java.util.Map;

/**
 * interprets previous layer into output options
 */
public class OutputLayer extends InterpretingLayer {
    int smallest;
    int largest;

    public OutputLayer(Object[] config, int numPrevNodes) {
        super(config);

        //set small and large values
        smallest = (Integer) config[2];
        largest = (Integer) config[3];
        if (smallest > largest) {
            int temp = smallest;
            smallest = largest;
            largest = temp;
        }

        //set num of nodes
        numOfNodes = largest - smallest + 1;

        // generate nodes
        genNodes(numPrevNodes);
    }

    /**
     * iteratively creates values from small to large and maps node values
     *
     * @param imageData image to interpret
     * @return mapped output to probability
     */
    @Override
    public Map<Integer, Double> interpret(ImageData imageData) {
        Map<Integer, Double> output = new HashMap<>();

        for (int i = 0; i < numOfNodes; i++) {
            output.put(i + smallest, nodes[i].calculateValue(imageData.getValues()));
        }

        return output;
    }

    /**
     * squashes all data to get values between 0 and 1
     */
    @Deprecated
    private Map<Integer, Double> normalizeOutput(Map<Integer, Double> data) {

        double largest = -9999999;
        double current;

        //finding top and bottom values
        for (Map.Entry<Integer, Double> e : data.entrySet()) {
            current = e.getValue();

            if (current > largest) {
                largest = current;
            }
        }

        Map<Integer, Double> newData = new HashMap<>();
        for (Map.Entry<Integer, Double> e : data.entrySet()) {
            newData.put(e.getKey(), (e.getValue()) / largest);
        }


        return newData;
    }
}
