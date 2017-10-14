package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment HeightMap
 *
 * @author malcolmr
 */
public class Terrain {

	private Dimension mySize;
	private double[][] myAltitude;
	private List<Tree> myTrees;
	private List<Road> myRoads;
	private float[] mySunlight;

	/**
	 * Create a new terrain
	 *
	 * @param width
	 *            The number of vertices in the x-direction
	 * @param depth
	 *            The number of vertices in the z-direction
	 */
	public Terrain(int width, int depth) {
		mySize = new Dimension(width, depth);
		myAltitude = new double[width][depth];
		myTrees = new ArrayList<Tree>();
		myRoads = new ArrayList<Road>();
		mySunlight = new float[3];
	}

	public Terrain(Dimension size) {
		this(size.width, size.height);
	}

	public Dimension size() {
		return mySize;
	}

	public List<Tree> trees() {
		return myTrees;
	}

	public List<Road> roads() {
		return myRoads;
	}

	public float[] getSunlight() {
		return mySunlight;
	}

	/**
	 * Set the sunlight direction.
	 * 
	 * Note: the sun should be treated as a directional light, without a
	 * position
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setSunlightDir(float dx, float dy, float dz) {
		mySunlight[0] = dx;
		mySunlight[1] = dy;
		mySunlight[2] = dz;
	}

	/**
	 * Resize the terrain, copying any old altitudes.
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		mySize = new Dimension(width, height);
		double[][] oldAlt = myAltitude;
		myAltitude = new double[width][height];

		for (int i = 0; i < width && i < oldAlt.length; i++) {
			for (int j = 0; j < height && j < oldAlt[i].length; j++) {
				myAltitude[i][j] = oldAlt[i][j];
			}
		}
	}

	/**
	 * Get the altitude at a grid point
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public double getGridAltitude(int x, int z) {
		return myAltitude[x][z];
	}

	/**
	 * Set the altitude at a grid point
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public void setGridAltitude(int x, int z, double h) {
		myAltitude[x][z] = h;
	}

	/**
	 * Get the altitude at an arbitrary point. Non-integer points should be
	 * interpolated from neighbouring grid points
	 * 
	 * TO BE COMPLETED
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public double altitude(double x, double z) {
		double altitude = 0;
		int x1 = (int) x;
		int z1 = (int) z;
//		// A==> vertex {x1, this.myAltitued[x1][z1],z1}
//		// B==> vertex {x1, this.myAltitued[x1][z1+1],z1+1}
//		System.out.println("x1 " + x1 + " z1 " + z1 + " z " + z + " z2 " + (z1+1) + " y1 "+this.myAltitude[x1][z1] + " y2 " + this.myAltitude[x1][z1+1]);
		double ya = interpolate(z1, z, z1+1,this.myAltitude[x1][z1],this.myAltitude[x1][z1+1]);
//		System.out.println("z1 "+ z1 + " ya " + ya + " z2 "+ (z1+1) + " x1 " + x1);
//		// new vertex { x1, ya, z}

		// A==> vertex {x2, this.myAltitued[x2][z1],z1}
		// C==> vertex {x2, this.myAltitued[x2][z1+1],z1+1}
//		System.out.println("x2 " + (x1+1) +" z1 " + z1 + " z " + z + " z2 " + (x1+1) + " y3 "+this.myAltitude[x1+1][z1] + " y4 " + this.myAltitude[x1+1][z1+1]);
		double yb = interpolate(z1,z,z1+1,this.myAltitude[x1+1][z1],this.myAltitude[x1+1][z1+1]);
//		System.out.println("z1 "+ z1 + " yb " + yb + " z2 "+ (z1+1)+" x2 " + (x1 + 1));
		// new vertex {x2, yb, z}
		
//		System.out.println("z " + z + " x1 " + x1 + " x " + x + " x2 " + (x1 + 1) + " ya "+ ya + " yb " + yb);
		altitude = interpolate(x1, x, x1+1, ya, yb);
//		System.out.println("z "+ z + " y " + y + " x1 " + x1);
		
		return altitude;
	}
	private double interpolate(double x1,double x, double x2, double y1, double y2){
		return (x-x1)/(x2-x1)*y2+(x2-x)/(x2-x1)*y1;
	}

	/**
	 * Add a tree at the specified (x,z) point. The tree's y coordinate is
	 * calculated from the altitude of the terrain at that point.
	 * 
	 * @param x
	 * @param z
	 */
	public void addTree(double x, double z) {
		double y = altitude(x, z);
		Tree tree = new Tree(x, y, z);
		myTrees.add(tree);
	}

	/**
	 * Add a road.
	 * 
	 * @param x
	 * @param z
	 */
	public void addRoad(double width, double[] spine) {
		Road road = new Road(width, spine);
		myRoads.add(road);
	}

	public double[][][] vertex_mesh() {
		int i = 0;
		int w = this.mySize.width;
		int d = this.mySize.height;
		double[][][] verties = new double[(w-1) * (d-1)* 2][3][3];
		for (int x = 0; x < w - 1; x++) {
			for (int z = 0; z < d - 1; z++) {
//					(0,0,0)	 
//						+
//						|\   
//					 |	| \  
//					 V	|  \ 
//						+---+					
//					(0,0,1)->
				double [] vertexa = {x, this.myAltitude[x][z], z};
				verties[i][0] = vertexa;   
				double [] vertexb = {x, this.myAltitude[x][z + 1], z + 1};
				verties[i][1] =vertexb;
				double [] vertexc = {x + 1, this.myAltitude[x + 1][z + 1], z + 1};
				verties[i++][2] =vertexc;
//					(0,0,0) <- (1,0.5,0)
//						+---+
//						 \  | ^
//					  	  \ | |
//					  	   \|
//						    +					
//						(1,0.3,1)
				double [] vertexd = {x + 1, this.myAltitude[x + 1][z], z};
				verties[i][0] =vertexc;
				verties[i][1] =vertexd;
				verties[i++][2] =vertexa;
			}
		}

		

		return verties;
	}


}
