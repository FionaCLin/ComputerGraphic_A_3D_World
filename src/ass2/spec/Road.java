package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road
 *
 * @author malcolmr, BrandonSandoval, James Shin
 */
public class Road {
	private List<Double> myPoints;
	private double myWidth;

	private Texture myTexture;

	private Terrain myTerrain;

	/**
	 * Create a new road starting at the specified point
	 */
	public Road(double width, double x0, double y0) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		myPoints.add(x0);
		myPoints.add(y0);
	}

	/**
	 * Create a new road with the specified spine
	 *
	 * @param width
	 * @param spine
	 */
	public Road(double width, double[] spine, Terrain myTerrain) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		for (int i = 0; i < spine.length; i++) {
			myPoints.add(spine[i]);
		}
		this.myTerrain = myTerrain;
	}

	/**
	 * The width of the road.
	 *
	 * @return
	 */
	public double width() {
		return myWidth;
	}

	/**
	 * Add a new segment of road, beginning at the last point added and ending
	 * at (x3, y3). (x1, y1) and (x2, y2) are interpolated as bezier control
	 * points.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		myPoints.add(x1);
		myPoints.add(y1);
		myPoints.add(x2);
		myPoints.add(y2);
		myPoints.add(x3);
		myPoints.add(y3);
	}

	/**
	 * Get the number of segments in the curve
	 *
	 * @return
	 */
	public int size() {
		return myPoints.size() / 6;
	}

	/**
	 * Get the specified control point.
	 *
	 * @param i
	 * @return
	 */
	public double[] controlPoint(int i) {
		double[] p = new double[2];
		p[0] = myPoints.get(i * 2);
		p[1] = myPoints.get(i * 2 + 1);
		return p;
	}

	/**
	 * Get a point on the spine. The parameter t may vary from 0 to size().
	 * Points on the kth segment take have parameters in the range (k, k+1).
	 *
	 * @param t
	 * @return
	 */
	public double[] point(double t) {
		int i = (int) Math.floor(t);
		t = t - i;

		i *= 6;

		double x0 = myPoints.get(i++);
		double y0 = myPoints.get(i++);
		double x1 = myPoints.get(i++);
		double y1 = myPoints.get(i++);
		double x2 = myPoints.get(i++);
		double y2 = myPoints.get(i++);
		double x3 = myPoints.get(i++);
		double y3 = myPoints.get(i++);

		double[] p = new double[2];

		p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
		p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;

		return p;
	}

	/**
	 * Calculate the Bezier coefficients
	 *
	 * @param i
	 * @param t
	 * @return
	 */
	private double b(int i, double t) {

		switch (i) {

		case 0:
			return (1 - t) * (1 - t) * (1 - t);

		case 1:
			return 3 * (1 - t) * (1 - t) * t;

		case 2:
			return 3 * (1 - t) * t * t;

		case 3:
			return t * t * t;
		}

		// this should never happen
		throw new IllegalArgumentException("" + i);
	}

	public void drawRoad(GL2 gl) {
		double y = myTerrain.altitude(point(0)[0], point(0)[1]);

		// gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINES);
		// Turn on OpenGL texturing.
		// Load textures

		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());

		// gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_DONT_CARE);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

		//enable polygon offset for filled polygons
		gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
		//push this polygon to the front a little
		gl.glPolygonOffset(-1,-1);

		gl.glPushMatrix();
		{
			gl.glBegin(GL2.GL_QUAD_STRIP);
			{

				double[] start = point(0);
				double[] spinePoint;
				double rate = 0.5;
				for (double i = 0.01; i < 1; i += 0.01) {
					// fix y from getAtitute
					
					spinePoint = point(i);
					double[][] normals = normal(start, spinePoint);
					normals[0] = normalise(normals[0]);
					normals[1] = normalise(normals[1]);

					// get points on x,z coordinate
					double[] p = getPoint(normals[0], spinePoint, rate);
					double[] q = getPoint(normals[1], spinePoint, rate);

					// glTexCoord2d(i, 0);
					// gl.glVertex3d(p0], myTerrain.getGridAltitude(
					// (int)q[0],(int)q[1]), p[1]);
					// gl.glColor3f(0f,1f,0f);

					// gl.glTexCoord2d(i, 1);
					// gl.glVertex3d(q[0], myTerrain.altitude(q[0],q[1]),q[1]);
					// gl.glVertex3d(q[0], myTerrain.getGridAltitude( (int)q[0],
					// (int)q[1] ), q[1]);
					// gl.glColor3f(0f,1f,0f);

					gl.glTexCoord2d(i, 0);
					// gl.glVertex3d(p[0], myTerrain.altitude(
					// bound(p[0],myTerrain.size().width-0.5)
					gl.glVertex3d(q[0], y, q[1]);
					
					// System.out.println(val);

					gl.glTexCoord2d(i, 1);
				
					
					// ,bound(p[1],myTerrain.size().height) ), p[1]);
					gl.glVertex3d(p[0], y, p[1]);
					int val = bound(p[0], myTerrain.size().width);

					start = spinePoint;
				}

			}
			gl.glEnd();
		}
		gl.glPopMatrix();
		gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);

	}

	// check the bound for arrary myAttitude[][]
	// without checking would suck my life
	public int bound(double x, double upbound) {
		if (x > upbound)
			return (int) upbound;
		if (x < upbound && x > 0)
			return (int) x;
		else
			return 0;

	}

	public double[][] normal(double[] spinePoint1, double[] spinePoint2) {

		double[][] normals = new double[2][2];
		double dx = spinePoint2[0] - spinePoint1[0];
		double dz = spinePoint2[1] - spinePoint1[1];

		normals[0][0] = -dz;
		normals[0][1] = dx;
		normals[1][0] = dz;
		normals[1][1] = -dx;

		return normals;
	}

	public double[] getPoint(double[] normal, double[] spinePoint, double rate) {

		return new double[] { spinePoint[0] + normal[0] * myWidth * rate, spinePoint[1] + normal[1] * myWidth * rate };
	}

	public void setTextures(Texture road) {
		myTexture = road;

	}

	/*
	 * double getMagnitude(double [] n){ double mag = n[0]*n[0] + n[1]*n[1] +
	 * n[2]*n[2]; mag = Math.sqrt(mag); return mag; }
	 */

	public double[] normalise(double[] v) {

		double mag = Math.sqrt(v[0] * v[0] + v[1] * v[1]);

		double[] norm = new double[2];

		norm[0] = v[0] / mag;
		norm[1] = v[1] / mag;

		return norm;
	}

	/*
	 * double [] normalise(double [] n){ double mag = getMagnitude(n); double
	 * norm[] = {n[0]/mag,n[1]/mag,n[2]/mag}; return norm; }
	 * 
	 * double [] cross(double u [], double v[]){ double crossProduct[] = new
	 * double[3]; crossProduct[0] = u[1]*v[2] - u[2]*v[1]; crossProduct[1] =
	 * u[2]*v[0] - u[0]*v[2]; crossProduct[2] = u[0]*v[1] - u[1]*v[0];
	 * //System.out.println("CP " + crossProduct[0] + " " + crossProduct[1] +
	 * " " + crossProduct[2]); return crossProduct; }
	 * 
	 * double [] getNormal(double[] p0, double[] p1, double[] p2){ double u[] =
	 * {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]}; double v[] = {p2[0] -
	 * p0[0], p2[1] - p0[1], p2[2] - p0[2]};
	 * 
	 * return cross(u,v);
	 * 
	 * }
	 */

}