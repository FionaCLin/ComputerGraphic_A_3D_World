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
		int w = (int) x;
		int d = (int) z;

		System.out.println("(" + x + ", " + myAltitude[w][d] + ", " + z + ")");

		return myAltitude[w][d];
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
	// public void vertex_mesh(GL2 gl) {
	// gl.glBegin(GL2.GL_TRIANGLES);
	// {
	//
	// gl.glColor4f(1, 1, 1, 1);
	// System.out.println('a' + " x " + 0 + " y " + 0+ " z " + 0);
	// System.out.println('b' + " x " + 0 + " y " + 0+ " z " + 1);
	// System.out.println('c' + " x " + 1 + " y " +0+ " z " + 1);
	// gl.glVertex3d(0, 0, 0);
	// gl.glVertex3d(0, 0, 1);
	// gl.glVertex3d(1, 1, 1);
	// System.out.println("-------------------------------");
	// System.out.println('c' + " x " + 1 + " y " + 1 + " z " + 1);
	// System.out.println('d' + " x " + 1 + " y " + 0 + " z " + 0);
	// System.out.println('a' + " x " + 0 + " y " + 0 + " z " + 0);
	// gl.glVertex3d(1, 1, 1);
	// gl.glVertex3d(1, 0, 0);
	// gl.glVertex3d(0, 0, 0);
	// System.out.println("-------------------------------");
	//
	// System.out.println('d' + " x " + 1 + " y " + 0 + " z " + 0);
	// System.out.println('c' + " x " + 1 + " y " + 1 + " z " + 1);
	// System.out.println('e' + " x " + 2 + " y " + 0 + " z " + 1);
	// gl.glVertex3d(1, 0, 0);
	// gl.glVertex3d(1, 1, 1);
	// gl.glVertex3d(2, 0, 1);
	// System.out.println("-------------------------------");
	// System.out.println('e' + " x " + 2 + " y " + 0 + " z " + 1);
	// System.out.println('f' + " x " + 2 + " y " + 0 + " z " + 0);
	// System.out.println('d' + " x " + 1 + " y " + 0 + " z " + 0);
	// gl.glVertex3d(2, 0, 1);
	// gl.glVertex3d(2, 0, 0);
	// gl.glVertex3d(1, 0, 0);
	// System.out.println("-------------------------------");
	//
	//
	// System.out.println('b' + " x " + 0 + " y " + 0 + " z " + 1);
	// System.out.println('h' + " x " + 0 + " y " + 0 + " z " + 2);
	// System.out.println('g' + " x " + 1 + " y " + 0 + " z " + 2);
	// gl.glVertex3d(0, 0, 1);
	// gl.glVertex3d(0, 0, 2);
	// gl.glVertex3d(1, 0, 2);
	// System.out.println("-------------------------------");
	//
	// System.out.println('g' + " x " + 1 + " y " + 0 + " z " + 2);
	// System.out.println('c' + " x " + 1 + " y " + 1 + " z " + 1);
	// System.out.println('b' + " x " + 0 + " y " + 0 + " z " + 1);
	// gl.glVertex3d(1, 0, 2);
	// gl.glVertex3d(1, 1, 1);
	// gl.glVertex3d(0, 0, 1);
	// System.out.println("-------------------------------");
	//
	//
	//
	// System.out.println('c' + " x " + 1 + " y " + 1 + " z " + 1);
	// System.out.println('g' + " x " + 1 + " y " + 0 + " z " + 2);
	// System.out.println('i' + " x " + 2 + " y " + 0 + " z " + 2);
	//
	// gl.glVertex3d(1, 1, 1);
	// gl.glVertex3d(1, 0, 2);
	// gl.glVertex3d(2, 0, 2);
	//
	// System.out.println("-------------------------------");
	//
	// System.out.println('i' + " x " + 2 + " y " + 0 + " z " + 2);
	// System.out.println('e' + " x " + 2 + " y " + 0 + " z " + 1);
	// System.out.println('c' + " x " + 1 + " y " + 1 + " z " + 1);
	//
	// gl.glVertex3d(2, 0, 2);
	// gl.glVertex3d(2, 0, 1);
	// gl.glVertex3d(1, 1, 1);
	//
	// }
	// gl.glEnd();
	//
	// System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
	// }

	public double[] normal_mesh() {
		int k = 0;
		int w = this.mySize.width;
		int d = this.mySize.height;
		double[] mesh = new double[w * d * 3];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < d; j++) {
				System.out.println("x: " + i + "y: " + this.myAltitude[i][j] + "z: " + j);
				mesh[k++] = j;
				mesh[k++] = this.myAltitude[j][i];
				mesh[k++] = i;
			}
		}
		return mesh;
	}
}
