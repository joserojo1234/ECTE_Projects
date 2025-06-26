import java.awt.image.BufferedImage;

public class HistogramEqualizer {
    public static int[] computeHistogram(int[] image, int levels) {
        int[] histogram = new int[levels];
        for (int pixel : image) {
            histogram[pixel]++;
        }
        return histogram;
    }

    public static int[] computeCumulativeHistogram(int[] histogram) {
        int[] cumulative = new int[histogram.length];
        cumulative[0] = histogram[0];
        for (int i = 1; i < histogram.length; i++) {
            cumulative[i] = cumulative[i - 1] + histogram[i];
        }
        return cumulative;
    }

    public static int[] mapImage(int[] image, int[] cumulativeHist, int levels, int size) {
        int[] mapped = new int[size];
        for (int i = 0; i < cumulativeHist.length; i++) {
            cumulativeHist[i] = (cumulativeHist[i] * (levels - 1)) / size;
        }
        for (int i = 0; i < size; i++) {
            mapped[i] = cumulativeHist[image[i]];
        }
        return mapped;
    }
}
