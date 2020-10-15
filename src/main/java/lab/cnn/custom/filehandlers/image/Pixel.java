package lab.cnn.custom.filehandlers.image;

import java.io.Serializable;

/**
 * Object to represent a single pixel of an image
 */
public class Pixel implements Serializable {

    private double greyScale;

    /**
     * constructor for 1:1 mapping
     *
     * @param greyScale greyscale value for the pixel
     */
    public Pixel(double greyScale) {
        this.greyScale = greyScale;
    }

    /**
     * constructor to map rgb to greyscale
     *
     * @param red   value between 0 and 255
     * @param green value between 0 and 255
     * @param blue  value between 0 and 255
     */
    public Pixel(int red, int green, int blue) {
        //convert pixel to grayscale to compute more easily
        greyScale = 0.2990 * red + 0.5870 * green + 0.1140 * blue;
    }

    /**
     * greyscale allows to differentiate pixels based on a single value
     *
     * @return the single value
     */
    public double getGreyScale() {
        return greyScale;
    }

    public void setGreyScale(double greyScale) {
        this.greyScale = greyScale;
    }
}
