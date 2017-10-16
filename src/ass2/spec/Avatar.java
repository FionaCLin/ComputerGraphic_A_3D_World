package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private double[] myPos;

	public Avatar(double x, double y, double z) {
		myPos = new double[3];
		myPos[0] = x;
		myPos[1] = y;
		myPos[2] = z;
	}

	public double[] getPosition() {
		return myPos;
	}

	public void drawAvatar(GL2 gl, GLUT glut) {
		gl.glTranslated(myPos[0], myPos[1], myPos[2]);
		gl.glScaled(.25, .25, .25);
		gl.glTranslated(0, 2.5, 0);
		gl.glColor3f(102f / 255, 0, 51f / 255);
		glut.glutSolidSphere(1.0, 10, 10);
		gl.glTranslated(0, -.5, 0);
		gl.glRotated(90, 1, 0, 0);

		gl.glColor3f(102f / 255, 51f / 255, 0);
		glut.glutSolidCylinder(0.5, 2, 20, 20);

		gl.glColor3f(1, 1, 1);
		gl.glTranslated(0.25, -.5, 2.5);
		gl.glScaled(.5, .5, .5);
		glut.glutSolidCube(1);

		gl.glTranslated(0.25, -.5, -2.5);
		// gl.glScaled(.25, .25, .25);
		glut.glutSolidCube(1);
	}

	/**
	 * @return the myPos
	 */
	public double[] getMyPos() {
		return myPos;
	}

	/**
	 * @param myPos the myPos to set
	 */
	public void setMyPos(double[] myPos) {
		this.myPos = myPos;
	}
	
}
