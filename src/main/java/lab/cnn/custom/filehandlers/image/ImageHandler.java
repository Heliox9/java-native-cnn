package lab.cnn.custom.filehandlers.image;

import lab.controlBase.commandlineInterface.LoggingBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler to read images into custom format
 */
public class ImageHandler implements Serializable {

    private int targetSize;

    public ImageHandler(int targetSize) {
        this.targetSize = targetSize;
    }

    /**
     * parses the extension from a full filename
     *
     * @param fullName name of the file
     * @return the extension
     */
    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public int getTargetSizeSquared() {
        return targetSize * targetSize;
    }

    public int getTargetSize() {
        return targetSize;
    }

    /**
     * forwarding function with disabled file limit
     *
     * @param folderPath full or partial path to folder
     * @return list of mapped data for each image
     */
    public List<ImageData> handleFolder(String folderPath) {
        return handleFolder(folderPath, -42);
    }

    /**
     * checks if path is a folder
     * gets all subfiles and parses each for file ending png
     * calls handle for each file and stores results in List
     *
     * @param folderPath full or partial path to folder
     * @param maxFiles   limit for how many files are read
     * @return list of mapped data for each image
     */
    public List<ImageData> handleFolder(String folderPath, int maxFiles) {
        File folder = new File(folderPath);

        // check if folder
        if (!folder.isDirectory()) {
            return null;
        }

        // pull all filenames in folder
        String[] filesNames = folder.list();


        ArrayList<ImageData> images = new ArrayList<>();
        int counter = 0;

        // iterate filenames
        for (String s : filesNames) {
            // check for png image
            if (getFileExtension(s).equals("png")) {
                // add png path to list of images
                images.add(handle(folder.getPath() + "/" + s));

                counter++;
            }

            // break if file target is reached
            if (maxFiles > 0 && counter >= maxFiles) {
                break;
            }
        }

        return images;
    }


    /**
     * read an image to local format
     *
     * @param path path to single png file
     * @return ImageData for entered image
     */
    public ImageData handle(String path) {
        File image = new File(path);

        try {
            // read image to buffer
            BufferedImage bufferedImage = ImageIO.read(image);
            bufferedImage = resize(bufferedImage);

            // convert buffer to ImageData
            return marchThroughImage(bufferedImage);
        } catch (Exception e) {
            LoggingBase.log.error("File not readable: " + image.getAbsolutePath());
        }

        return null;
    }

    /**
     * resizes image to needed dimensions
     *
     * @param original original image
     * @return resized image
     */
    public BufferedImage resize(BufferedImage original) {
        // creates output image (as empty buffer space)
        BufferedImage outputImage = new BufferedImage(targetSize,
                targetSize, original.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(original, 0, 0, targetSize, targetSize, null);
        g2d.dispose();

        return outputImage;
    }

    /**
     * build ImageData from BufferedImage
     *
     * @param image image to convert
     * @return corresponding ImageData
     */
    private ImageData marchThroughImage(BufferedImage image) {
        // get dimensions
        int w = image.getWidth();
        int h = image.getHeight();

        // create dataset
        ImageData data = new ImageData(w, h);

        // parse image
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // read rgb
                int pixel = image.getRGB(j, i);

                // create pixel and store in data
                data.setPixel(j, i, readPixel(pixel));
            }
        }

        return data;
    }

    /**
     * convert java rgb value to Pixel
     *
     * @param pixel rgb value read from BufferedImage
     * @return Pixel object for value
     */
    private Pixel readPixel(int pixel) {
        // get color codes from rgb tag
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;

        // create and return Pixel object
        return new Pixel(red, green, blue);
    }


}
