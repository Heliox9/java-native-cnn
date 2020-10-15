package lab.cnn.dl4j;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class CnnDl4jTest {

    @Test
    void parseBoxCsvTest() {
        CnnDl4j subject = new CnnDl4j("", 0, 0, 0, 0);
        String name = "1.png";
        File path = new File("E:\\Projects\\T3101\\Info\\Datasets\\svhn\\svhn_train/box_mapping.csv");

        int[][] result = subject.parseBoxCsv(path, name);

        for (int[] i : result) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println("");
        }
    }

    @Test
    void cropTest() throws IOException {
        CnnDl4j subject = new CnnDl4j("", 0, 0, 0, 0);
        File file = new File("E:\\Projects\\T3101\\Info\\Datasets\\svhn\\svhn_train/1.png");

      /*
      int[][] data = new int[][]{
                new int[]{78, 296, 247, 327, 1},
                new int[]{82, 300, 324, 419, 9}
        };
        */
        int[] data = new int[]{82, 300, 324, 419, 9};

        ImageIO.write(subject.crop(ImageIO.read(file), data), "png", new File("build/test.png"));
    }
}