import java.util.Arrays;

public class ImageGenarator {
    float approximationLevel = (float) 0.25;
    
    public void saveImgs(int[][][][] imgs, String path, boolean outline, boolean edgeDetection, boolean useQuadtree){
        PPMWriter writer = new PPMWriter();
        path = prepareOutputPath(path, outline, edgeDetection, useQuadtree);
        try {
            int count = 0;
            for(int[][][] img : imgs){
                writer.writeP3(path + count + ".ppm", img);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][][][] generateImgs(int[][][] img, float[] compressionLevels, boolean outline){
        Arrays.sort(compressionLevels);
        float startingThreshold = 255 * 255;
        float[] bestThresholds = new float[compressionLevels.length];
        int counter = compressionLevels.length;
        for(int i = 0; i < compressionLevels.length; i++){
            float compressionLevel = compressionLevels[i];
            float bestThreshold = bestThreshold(img, compressionLevel, startingThreshold);
            bestThresholds[i] = bestThreshold;
            if(bestThreshold == -1) {
                System.out.printf("The compression level of: %2f is not reachable!!\n",compressionLevel);
                counter--;
            }
            else{
                Quadtree quadTree = new Quadtree(bestThreshold, img);
                float reachedCompressionLevel = quadTree.getCompressionLevel();
                System.out.printf("The compression level of: %2f have been reached as: %3f\n", compressionLevel, reachedCompressionLevel);
            }
        }

        float[] reachableBestThresholds = new float[counter];
        counter = 0;
        for(int i = 0; i < bestThresholds.length; i++){
            float bestThreshold = bestThresholds[i];
            if(bestThreshold == -1) continue;
            reachableBestThresholds[counter] = bestThreshold;
            counter++;
        }

        int[][][][] imgs = new int[reachableBestThresholds.length][][][];
        int count = 0;
        for(float bestThreshold : reachableBestThresholds){
            Quadtree quadtree = new Quadtree(bestThreshold, img);
            int[][][] newImg = quadtree.getImage(outline);
            imgs[count] = newImg;
            count ++;
        }


        return imgs;
    }

    float bestThreshold(int[][][] img, float compressionLevel, float startingThreshold){
        for(float threshold = startingThreshold, down = 0, up = threshold, mid = (down + up) / 2; threshold > 0;){
            
            Quadtree quadtree = new Quadtree(mid, img);
            float currentComparisonLevel = quadtree.getCompressionLevel();
            if(Math.abs(currentComparisonLevel - compressionLevel) < approximationLevel){
                return mid;
            }

            if(currentComparisonLevel < compressionLevel)
                up = mid;
            else 
                down = mid;
            mid = (up + down) / 2;

            if((up > 1) && (Math.abs(up - down) < approximationLevel))
                return -1;
            else if( (up < 1) && (down < 1) && (Math.abs(up - down) < approximationLevel * approximationLevel  ))
                return -1;

            
        }
        return -1;
    }

    public void setApproximationLevel(float approximationLevel) {
        this.approximationLevel = approximationLevel;
    }

    private String prepareOutputPath(String path, boolean outline, boolean edgeDetection, boolean useQuadtree){
        if(!useQuadtree)
            path += "noQuadtree_";
        else {
            path += "withQuadtree_";
            if(!outline)
                path += "noOutline_";
            else
                path += "withOutline_";
        }
        
        if(!edgeDetection)
            path += "noEdges_";
        else
            path += "withEdges_";
        return path;
    }

}
