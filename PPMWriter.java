import java.io.*;

public class PPMWriter {

    public void writeP3(String filename, int[][][] img) throws IOException {

        int height = img.length;
        int width  = img[0].length;

        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            // Header
            out.println("P3");
            out.println(width + " " + height);
            out.println(255);

            // Pixels (row-major order)
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int r = img[row][col][0];
                    int g = img[row][col][1];
                    int b = img[row][col][2];

                    out.println(r + " " + g + " " + b);
                }
            }

        }
    }

    
}
