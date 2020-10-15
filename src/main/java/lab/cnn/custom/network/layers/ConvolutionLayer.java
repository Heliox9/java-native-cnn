package lab.cnn.custom.network.layers;

import lab.cnn.custom.filehandlers.image.ImageData;
import lab.cnn.custom.filehandlers.image.Pixel;

/**
 * Edits data using given nodes and filters
 */
public class ConvolutionLayer extends ModifyingLayer {


    public ConvolutionLayer(Object[] config, int numPrevNodes) {
        super(config);

        genNodes(numPrevNodes);
    }


    @Override
    public ImageData modify(ImageData image) {

        // create input array
        ImageData newData = new ImageData(numOfNodes, 1);
        double[] inputs = image.getValues();

        // calculate new Pixels
        for (int i = 0; i < numOfNodes; i++) {
            newData.setPixel(i, 0, new Pixel(nodes[i].calculateValue(inputs)));
        }


        // return modified data
        return newData;
    }

}
