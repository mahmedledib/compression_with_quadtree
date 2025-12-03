public class EdgeDetector {
    int[][][] detectEdges(int[][][] img, int[][] filter){
        int imgXSize = img.length;
        int imgYSize = img[0].length;
        if(filter == null){
            filter = new int[3][3];
            for(int i = 0; i < 3; i++)
                for(int j = 0; j < 3; j++)
                    filter[i][j] = -1;
            filter[1][1] = 8;
        }
        int filterXSize = filter.length;
        int filterYSize = filter[0].length;
        int[][][] result = new int[imgXSize - filterXSize + 1][imgYSize - filterYSize + 1][3];
        for(int x = 0; x < imgXSize - filterXSize + 1; x++)
            for(int y = 0; y < imgYSize - filterYSize + 1; y++){
                // result[x][y][0] = 
                for(int i = 0; i < filterXSize; i++)
                    for(int j = 0; j < filterYSize; j++){
                        result[x][y][0] += img[x + i][y + j][0] * filter[i][j];
                        result[x][y][1] += img[x + i][y + j][1] * filter[i][j];
                        result[x][y][2] += img[x + i][y + j][2] * filter[i][j];
                    }
            }


        return result;
    }
    
    int[][][][] detectEdges(int[][][][] imgs, int[][] filter){
        int[][][][] result = new int[imgs.length][][][];
        
        for(int i = 0; i < imgs.length; i++){
            int[][][] img = imgs[i];
            result[i] = detectEdges(img, filter);
        }



        return result;
    }
}
