import java.awt.image.BufferedImage;

public class MultiThreadEqualization {

    static class EqualizerThread extends Thread {
        BufferedImage input;
        BufferedImage output;
        int startY, endY;

        public EqualizerThread(BufferedImage input, BufferedImage output, int startY, int endY) {
            this.input = input;
            this.output = output;
            this.startY = startY;
            this.endY = endY;
        }

        public void run() {
            int width = input.getWidth();
            int height = input.getHeight();
            int size = width * (endY - startY);

            for (int channel = 0; channel < 3; channel++) {
                int[] inputPixels = new int[size];
                int idx = 0;

                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = input.getRGB(x, y);
                        int val = (rgb >> (16 - 8 * channel)) & 0xff;
                        inputPixels[idx++] = val;
                    }
                }

                int[] hist = HistogramEqualizer.computeHistogram(inputPixels, 256);
                int[] cumHist = HistogramEqualizer.computeCumulativeHistogram(hist);
                int[] mapped = HistogramEqualizer.mapImage(inputPixels, cumHist, 256, size);

                idx = 0;
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        int rgb = output.getRGB(x, y);
                        int newVal = mapped[idx++];
                        int masked = rgb & ~(0xff << (16 - 8 * channel));
                        int updated = masked | (newVal << (16 - 8 * channel));
                        output.setRGB(x, y, updated);
                    }
                }
            }
        }
    }

    public static BufferedImage processImage(BufferedImage image, int numThreads) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage output = new BufferedImage(width, height, image.getType());

        EqualizerThread[] threads = new EqualizerThread[numThreads];
        int block = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startY = i * block;
            int endY = (i == numThreads - 1) ? height : (i + 1) * block;
            threads[i] = new EqualizerThread(image, output, startY, endY);
            threads[i].start();
        }

        for (EqualizerThread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return output;
    }
}
