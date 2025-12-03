import java.util.Iterator;
import java.util.Stack;

class Quadtree implements Iterable<Quadtree.Node> {
    int borderThikness = 5;
    private Node root;
    private int imgXSize;
    private int imgYSize;
    private int numberOfLeaves;

    public Quadtree(float threshold, int[][][] img) {
        this.imgXSize = img.length;
        this.imgYSize = img[0].length;
        this.root = new Node(0,0,this.imgXSize,this.imgYSize,img);
        numberOfLeaves = 1;
        constructTreeAux(this.root, threshold, img);
    }

    public int getNumberOfLeaves() {
        return numberOfLeaves;
    }


    static public class Node {
        private int x;
        private int y;
        private int p;
        private int q;
        private Node quarterNode1 = null;
        private Node quarterNode2 = null;
        private Node quarterNode3 = null;
        private Node quarterNode4 = null;
        private int[] averageColor = new int[3];

        public int[] getAverageColor() {
            return averageColor;
        }

        public Node(int x, int y,int p, int q, int[][][] img) {
            this.x = x;
            this.y = y;
            this.p = p;
            this.q = q;
            this.averageColor = averageOfNode(this,img);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getP() {
            return p;
        }

        public int getQ() {
            return q;
        }

        public void setQuarterNode1(Node quarterNode1) {
            this.quarterNode1 = quarterNode1;
        }

        public void setQuarterNode2(Node quarterNode2) {
            this.quarterNode2 = quarterNode2;
        }

        public void setQuarterNode3(Node quarterNode3) {
            this.quarterNode3 = quarterNode3;
        }

        public void setQuarterNode4(Node quarterNode4) {
            this.quarterNode4 = quarterNode4;
        }

        boolean isLeaf(){
            return quarterNode1 == null && quarterNode2 == null && quarterNode3 == null && quarterNode4 == null;
        }

        public void setAverageColor(int[] averageColor) {
            this.averageColor = averageColor;
        }


    }



    private void constructTreeAux(Node root, float threshold, int[][][] img) {
        int[] average = root.getAverageColor();
        float error = errorOfANode(root, average, img);
        int x = root.getX();
        int y = root.getY();
        int p = root.getP();
        int q = root.getQ();

        if (error < threshold || p<2 || q<2) {
            // If the error is below the threshold, we keep the node as is
            return;
        }
        // If we reach here, we need to split the node
        

        
        Node quarterNode1 = new Node(x, y, p / 2, q / 2, img);
        Node quarterNode2 = new Node(x + p / 2, y, p / 2, q / 2, img);
        Node quarterNode3 = new Node(x, y + q / 2, p / 2, q / 2, img);
        Node quarterNode4 = new Node(x + p / 2, y + q / 2, p / 2, q / 2, img);
                
        root.setQuarterNode1(quarterNode1);
        root.setQuarterNode2(quarterNode2);
        root.setQuarterNode3(quarterNode3);
        root.setQuarterNode4(quarterNode4);

        root.setAverageColor(null);  // Clear the average color since it's not a leaf anymore
        numberOfLeaves += 3; // We are adding 4 new nodes but removing 1 leaf

        // Recursively construct the tree for each child
        constructTreeAux(root.quarterNode1, threshold, img);
        constructTreeAux(root.quarterNode2, threshold, img);
        constructTreeAux(root.quarterNode3, threshold, img);
        constructTreeAux(root.quarterNode4, threshold, img);
    }


    private static int[] averageOfNode(Node node,int[][][] img){
        int[] avrage = new int[3];
        int xStar = node.getX();
        int yStar = node.getY();
        int p = node.getP();
        int q = node.getQ();

        for(int x = xStar; x - xStar < p; x++)
            for(int y = yStar; y - yStar < q; y++){
                avrage[0]+=img[x][y][0];
                avrage[1]+=img[x][y][1];
                avrage[2]+=img[x][y][2];

            }
        int totalPixels = node.getP() * node.getQ();
        if(totalPixels == 0)
            System.err.println("totalPixels == 0");
        for(int i = 0; i < 3; i++) avrage[i]/=totalPixels;
        return avrage;
    }

    static float errorOfANode(Node node, int[] avrage, int[][][] img){
        int sum = 0;
        int xStar = node.getX();
        int yStar = node.getY();
        int p = node.getP();
        int q = node.getQ();

        for(int x = xStar; x - xStar < p; x++)
            for(int y = yStar; y - yStar < q; y++){
                sum += Math.pow((img[x][y][0]-avrage[0]),2);
                sum += Math.pow((img[x][y][1]-avrage[1]),2);
                sum += Math.pow((img[x][y][2]-avrage[2]),2);
            }
        int totalPixels = node.getP() * node.getQ();
        float error = (float) sum / (float) totalPixels;
        return error;
    }


    @Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            Stack<Node> stack = new Stack<>();

            {
                stack.push(root);
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            private Node nextLeaf(){
                while(!stack.isEmpty()){
                    Node current = stack.pop();
                    if(current.isLeaf()){
                        return current;
                    }
                    stack.push(current.quarterNode4);
                    stack.push(current.quarterNode3);
                    stack.push(current.quarterNode2);
                    stack.push(current.quarterNode1);
                }
                return null;
            }
            @Override
            public Node next() {
                Node current = nextLeaf();
                return current;
            }
        };
    }

    public int[][][] getImage(boolean outline) {
        if(outline) borderThikness = 1; else borderThikness = 0;
        int[][][] img = new int[this.imgXSize][this.imgYSize][3];
        for(Node node : this){
            int nodeX = node.getX();
            int nodeY = node.getY();
            int nodeP = node.getP();
            int nodeQ = node.getQ();
            int[] avgColor = node.getAverageColor();
            for(int x = nodeX; x - nodeX < nodeP - borderThikness; x++)
                for(int y = nodeY; y - nodeY < nodeQ - borderThikness; y++){
                    img[x][y][0] = avgColor[0];
                    img[x][y][1] = avgColor[1];
                    img[x][y][2] = avgColor[2];
                }
        }
        return img;
    }

    public float getCompressionLevel(){
        return (float) this.numberOfLeaves / (imgXSize * imgYSize);
    }
}