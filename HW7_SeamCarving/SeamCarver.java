import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    private Color[][] color;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture " + picture + " is null");
        }
        this.picture = new Picture(picture);
        color = color(picture);
    }

    private Color[][] color(Picture picture) {
        int W = picture.width();
        int H = picture.height();
        Color[][] mat = new Color[W][H];
        for (int i = 0; i < W; i++)
            for (int j = 0; j < H; j++)
                mat[i][j] = picture.get(i, j);
        return mat;
    }

    public int[] findHorizontalSeam() {
        int W = picture.width();
        int H = picture.height();

        double[][] energy = new double[W][H];
        double[][] distTo = new double[W][H];
        int[][] pixelTo = new int[W][H];
        initArrays(energy, distTo, pixelTo);

        for (int i = 0; i < W - 1; i++) {
            for (int j = 0; j < H; j++) {
                if (j > 0) relax(energy, distTo, pixelTo, i, j, i + 1, j - 1);
                relax(energy, distTo, pixelTo, i, j, i + 1, j);
                if (j < H - 1) relax(energy, distTo, pixelTo, i, j, i + 1, j + 1);
            }
        }

        int[] seam = new int[W];
        seam[W - 1] = findMinIndex(distTo[W - 1]);
        for (int i = W - 2; i >= 0; i--) {
            seam[i] = pixelTo[i + 1][seam[i + 1]];
        }
        return seam;
    }

    private void initArrays(double[][] energy, double[][] distTo, int[][] pixelTo) {
        int W = picture.width();
        int H = picture.height();
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                energy[i][j] = energy(i, j);
                if (i == 0) distTo[i][j] = energy[i][j];
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    public double energy(int x, int y) {
        validate(x, y);
        double xGradSquared = gradSquared(x - 1, y, x + 1, y);
        double yGradSquared = gradSquared(x, y - 1, x, y + 1);
        return Math.sqrt(xGradSquared + yGradSquared);
    }

    private void validate(int x, int y) {
        int W = picture.width();
        int H = picture.height();
        if (x < 0 || x > W - 1) {
            throw new IllegalArgumentException("x " + x + " is not between 0 and " + (W - 1));
        }
        if (y < 0 || y > H - 1) {
            throw new IllegalArgumentException("y " + y + " is not between 0 and " + (H - 1));
        }
    }

    private double gradSquared(int x1, int y1, int x2, int y2) {
        int W = picture.width();
        int H = picture.height();
        if (x1 < 0) x1 = W - 1;
        if (x2 > W - 1) x2 = 0;
        if (y1 < 0) y1 = H - 1;
        if (y2 > H - 1) y2 = 0;
        Color color1 = color[x1][y1];
        Color color2 = color[x2][y2];
        int RedDiff = color1.getRed() - color2.getRed();
        int GreenDiff = color1.getGreen() - color2.getGreen();
        int BlueDiff = color1.getBlue() - color2.getBlue();
        return Math.pow(RedDiff, 2) + Math.pow(GreenDiff, 2) + Math.pow(BlueDiff, 2);
    }

    private void relax(double[][] energy, double[][] distTo, int[][] pixelTo,
                       int i1, int j1, int i2, int j2) {
        if (distTo[i2][j2] > distTo[i1][j1] + energy[i2][j2]) {
            distTo[i2][j2] = distTo[i1][j1] + energy[i2][j2];
            pixelTo[i2][j2] = j1;
        }
    }

    private int findMinIndex(double[] arr) {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public void removeHorizontalSeam(int[] seam) {
        validate(seam);
        int W = picture.width();
        int H = picture.height();
        Picture current = new Picture(W, H - 1);
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H - 1; j++) {
                if (j < seam[i]) current.set(i, j, color[i][j]);
                else current.set(i, j, color[i][j + 1]);
            }
        }
        picture = current;
        color = color(picture);
    }

    private void validate(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam " + seam + " is null");
        }
        int W = picture.width();
        int H = picture.height();
        int len = seam.length;
        if (len != W) {
            throw new IllegalArgumentException("seam length " + len + " is not " + W);
        }
        int prev = -1;
        for (int i = 0; i < len; i++) {
            int s = seam[i];
            if (s < 0 || s > H - 1) {
                throw new IllegalArgumentException("seam entry "
                                                   + s
                                                   + " is not between 0 and "
                                                   + (H - 1));
            }
            if (prev != -1 && Math.abs(prev - s) > 1) {
                throw new IllegalArgumentException("2 adjacent entries "
                                                   + prev
                                                   + " and "
                                                   + s
                                                   + " differ by more than 1");
            }
            prev = s;
        }
    }

    public int[] findVerticalSeam() {
        transposePicture();
        int[] seam = findHorizontalSeam();
        transposePicture();
        return seam;
    }

    public void removeVerticalSeam(int[] seam) {
        transposePicture();
        removeHorizontalSeam(seam);
        transposePicture();
    }

    private void transposePicture() {
        int W = picture.width(), H = picture.height();
        Picture transpose = new Picture(H, W);
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                transpose.set(i, j, color[j][i]);
            }
        }
        picture = transpose;
        color = color(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public static void main(String[] args) {

    }
}
