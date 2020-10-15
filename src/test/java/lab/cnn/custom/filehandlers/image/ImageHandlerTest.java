package lab.cnn.custom.filehandlers.image;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageHandlerTest {
    /**
     * disabled because loading files is not a deterministic test
     */
    @Test
    @Disabled
    void handle() {
        ImageHandler handler = new ImageHandler(300);
        ImageData data = handler.handle("datasets/train/1.png");

        assertTrue(data.validateData());
    }

    /**
     * disabled because loading files is not a deterministic test
     */
    @Test
    @Disabled
    void handleFolder() {
        ImageHandler handler = new ImageHandler(20);
        List<ImageData> data = handler.handleFolder("datasets/train", 50);

        for (ImageData i : data) {
            assertTrue(i.validateData());
        }

    }

    /**
     * Manual test to see image cropping
     */
    @Disabled
    @Test
    void resize() {
        ImageHandler handler = new ImageHandler(300);
        String imgNum = "1301";
        File image = new File("datasets/train/" + imgNum + ".png");


        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            bufferedImage = handler.resize(bufferedImage);


            String outputImagePath = "build/" + imgNum + "_test.png";
            // extracts extension of output file
            String formatName = outputImagePath.substring(outputImagePath
                    .lastIndexOf(".") + 1);

            // writes to output file
            ImageIO.write(bufferedImage, formatName, new File(outputImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}