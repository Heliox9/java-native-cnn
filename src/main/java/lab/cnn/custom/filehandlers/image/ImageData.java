package lab.cnn.custom.filehandlers.image;

import java.io.Serializable;

/**
 * Object to represent the raw data of an image
 * uses a Pixel object for each pixel
 */
public class ImageData implements Serializable {
    Pixel[][] data;
    private int width;
    private int height;

    public ImageData(int width, int height) {
        data = new Pixel[width][height];
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Pixel[][] getData() {
        return data;
    }

    public void setPixel(int width, int height, Pixel pixel) {
        data[width][height] = pixel;
    }

    public Pixel getPixel(int width, int height) {
        return data[width][height];
    }

    /**
     * checks if all pixels are set
     *
     * @return false if a unset pixel is found
     */
    public boolean validateData() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (data[i][j] == null) {
                    // invalid if a pixel is not set
                    return false;
                }
            }
        }
        // all pixels are set
        return true;
    }

    /**
     * parses the 2dimensional data into a onedimensional array
     *
     * @return parsed array
     */
    public Pixel[] getOnedimensionalData() {
        normalize();
        Pixel[] oneDim = new Pixel[width * height];
        int fullcount = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                oneDim[fullcount] = data[i][j];
                fullcount++;
            }
        }

        return oneDim;
    }

    /**
     * builds number values for local pixel data
     *
     * @return double values of pixels
     */
    public double[] getValues() {
        // build 1d array
        Pixel[] inputPixels = getOnedimensionalData();
        int size = width * height;
        double[] inputs = new double[size];

        //fill input array
        for (int i = 0; i < size; i++) {
            inputs[i] = inputPixels[i].getGreyScale();
        }

        return inputs;
    }

    /**
     * min/max normalization between 0 and 1 for data
     */
    private void normalize() {
        double max = -99999;
        double min = 999999;
        double current;


        // find highest and lowest value
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                current = data[i][j].getGreyScale();
                if (current > max) max = current;
                if (current < min) min = current;
            }
        }


        // normalize between 0 and 1
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (min != 0) data[i][j].setGreyScale(data[i][j].getGreyScale() - min);
                if (max != 0) data[i][j].setGreyScale(data[i][j].getGreyScale() / max);
            }
        }


    }
}
