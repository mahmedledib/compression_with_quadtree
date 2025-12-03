import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        String inputPath = "kira.ppm";
        String outputPath = "";
        boolean useQuadtree = false;
        boolean edgeDetection = false;
        boolean outline = false;
        float approximationLevel = 1E-3f;
        boolean manualCompressionLevels = false ;
        int numberOfCompressionLevels = 1;
        float[] compressionLevels = {0.002f, 0.004f,0.01f, 0.033f, 0.077f, 0.2f, 0.5f, 0.65f};

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    i++; 
                    inputPath = args[i];
                    break;
                case "-o":
                    i++; 
                    outputPath = args[i];
                    break;
                case "-c":
                    useQuadtree = true;
                    break;
                case "-e":
                    edgeDetection = true;
                    break;
                case "-t":
                    outline = true;
                    break;
                case "-a":
                    i++; 
                    approximationLevel = Float.parseFloat(args[i]);
                    break;
                case "-n":
                    i++; 
                    numberOfCompressionLevels = Integer.parseInt(args[i]);
                    manualCompressionLevels = true;
                    break;
            }
        }

        try {
            int[][][] img = PPMReader.readP3(inputPath); // converts ppm image to a 3d tensor
            
            ImageGenarator imageGenarator = new ImageGenarator();
            imageGenarator.setApproximationLevel(approximationLevel);
            int[][][][] generatedImgs;
            if(useQuadtree){
                // Set compressionLevels array
                if(manualCompressionLevels){
                    compressionLevels = new float[numberOfCompressionLevels];
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please enter " + numberOfCompressionLevels + " compression levels:");
                    for(int i = 0; i < numberOfCompressionLevels; i++){
                        float compressionLevel = scanner.nextFloat();
                        compressionLevels[i] = compressionLevel;
                    }
                }
                generatedImgs = imageGenarator.generateImgs(img, compressionLevels, outline);
            }else{
                generatedImgs = new int[1][][][];
                generatedImgs[0] = img;
            }
            if(edgeDetection){
                EdgeDetector edgeDetector = new EdgeDetector();

                int[][] filter = new int[3][3];
                for(int i = 0; i < 3; i++)
                    for(int j = 0; j < 3; j++)
                        filter[i][j] = -1;
                filter[1][1] = 8;

                generatedImgs = edgeDetector.detectEdges(generatedImgs, filter);
            }
            imageGenarator.saveImgs(generatedImgs, outputPath, outline,edgeDetection,useQuadtree);  

            


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}