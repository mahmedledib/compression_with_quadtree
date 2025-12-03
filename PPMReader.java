import java.io.*;
import java.util.*;

public class PPMReader {

    public static int[][][] readP3(String filename) throws IOException {
        Scanner sc = new Scanner(new File(filename));

        // Skip comments and read the magic number
        String magic = nextNonComment(sc);
        if (!magic.equals("P3")) {
            throw new IOException("Not a P3 PPM file");
        }

        int width = Integer.parseInt(nextNonComment(sc));
        int height = Integer.parseInt(nextNonComment(sc));
        Integer.parseInt(nextNonComment(sc)); 

        int[][][] img = new int[height][width][3];

        // Read all pixel RGB values
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                img[row][col][0] = sc.nextInt(); // R
                img[row][col][1] = sc.nextInt(); // G
                img[row][col][2] = sc.nextInt(); // B
            }
        }

        sc.close();
        return img;
    }

    // Helper: skip comments (# ...)
    private static String nextNonComment(Scanner sc) {
        String token = sc.next();
        while (token.startsWith("#")) {
            sc.nextLine();     // skip rest of comment line
            token = sc.next();
        }
        return token;
    }

    public static void main(String[] args) {
        try {
            int[][][] img = readP3("image.ppm");
            System.out.println("Loaded image: " + img.length + " x " + img[0].length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
