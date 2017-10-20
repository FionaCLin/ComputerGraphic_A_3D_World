package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import ass2.spec.Game.Model;

public class Sun {
	private double[] myPos;
	private double angle;

	public Sun() {

	}

	public void drawSun(GL2 gl, GLUT glut) {
		gl.glPushMatrix();
		glut.glutSolidSphere(1.0, 20, 20);
		gl.glPopMatrix();

	}

	/**
	 * @return the myPos
	 */
	public double[] getMyPos() {
		return myPos;
	}

	/**
	 * @param myPos
	 *            the myPos to set
	 */
	public void setMyPos(double[] myPos) {
		this.myPos = myPos;
		System.out.println(myPos[0] + " x " + myPos[1] + " y " + myPos[2] + " z");
	}

	public void up(double height) {
		myPos[1] = height;
		myPos[2] -= step;
		System.out.println(myPos[0] + " x " + myPos[1] + " y " + myPos[2] + " z");
	}

	public void down(double height) {
		myPos[1] = height;
		myPos[2] += step;
		System.out.println(myPos[0] + " x " + myPos[1] + " y " + myPos[2] + " z");
	}

	public void left(double height) {
		myPos[1] = height;
		myPos[0] -= step;
		System.out.println(myPos[0] + " x " + myPos[1] + " y " + myPos[2] + " z");
	}

	public void right(double height) {
		myPos[1] = height;
		myPos[0] += step;
		System.out.println(myPos[0] + " x " + myPos[1] + " y " + myPos[2] + " z");
	}

}
