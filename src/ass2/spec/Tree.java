package ass2.spec;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */


import com.jogamp.opengl.*;


public class Tree {

    private double[] myPos;

    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }

    public double[] getPosition() {
        return myPos;
    }

    public void loadTexture(GL2 gl){
        //Texture of the Cylinder
        // Texture of Sphere

    }

    public void drawTree(GL2 gl) {

    }

}
