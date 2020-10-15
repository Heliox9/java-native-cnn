package lab.cnn.custom.network.layers;

import lab.cnn.custom.filehandlers.image.ImageData;
import lab.cnn.custom.filehandlers.image.Pixel;

/**
 * normalizes data by bringing them to a single plane
 */
public class NormalizingLayer extends ModifyingLayer {

    public NormalizingLayer(Object[] config, int numPrevNodes) {
        super(config);

        genNodes(numPrevNodes);
    }

    /**
     * using dummy for activation
     *
     * @param value any double value for node
     * @return input value
     */
    @Override
    public double activationFunction(double value) {
        return value;
    }

    /**
     * maps the grayscale of the image to be between 0 and 1
     *
     * @param image image to modify
     * @return modified image
     */
    @Override
    public ImageData modify(ImageData image) {
        int width = image.getWidth();
        int heigth = image.getHeight();
        double value;
        int nodeCounter = 0;

        ImageData newData = new ImageData(width, heigth);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < heigth; j++) {
                value = image.getPixel(i, j).getGreyScale() / 255;

                newData.setPixel(i, j, new Pixel(value));

                nodes[nodeCounter].setLastValue(value);
                nodeCounter++;
            }
        }


        return newData;
    }


}
