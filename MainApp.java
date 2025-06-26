import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainApp {
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("Rain_Tree.jpg"));

            long startTime = System.currentTimeMillis();
            BufferedImage outputSingle = SingleThreadEqualization.processImage(image);
            long endTime = System.currentTimeMillis();
            System.out.println("Single-threaded time: " + (endTime - startTime) + " ms");
            ImageIO.write(outputSingle, "jpg", new File("output_single.jpg"));

            int numThreads = 4;
            startTime = System.currentTimeMillis();
            BufferedImage outputMulti = MultiThreadEqualization.processImage(image, numThreads);
            endTime = System.currentTimeMillis();
            System.out.println("Multi-threaded time (" + numThreads + " threads): " + (endTime - startTime) + " ms");
            ImageIO.write(outputMulti, "jpg", new File("output_multi.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
