import java.awt.image.BufferedImage;

public class SingleThreadEqualization {
    public static BufferedImage processImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int size = width * height;

        BufferedImage output = new BufferedImage(width, height, image.getType());

        for (int channel = 0; channel < 3; channel++) {
            int[] input = new int[size];
            int idx = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int value = (rgb >> (16 - 8 * channel)) & 0xff;
                    input[idx++] = value;
                }
            }

            int[] hist = HistogramEqualizer.computeHistogram(input, 256);
            int[] cumHist = HistogramEqualizer.computeCumulativeHistogram(hist);
            int[] mapped = HistogramEqualizer.mapImage(input, cumHist, 256, size);

            idx = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = output.getRGB(x, y);
                    int newVal = mapped[idx++];
                    int masked = rgb & ~(0xff << (16 - 8 * channel));
                    int updated = masked | (newVal << (16 - 8 * channel));
                    output.setRGB(x, y, updated);
                }
            }
        }

        return output;
    }
}
