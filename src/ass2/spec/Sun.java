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
		// shift along x = - 3*terrain.x
		gl.glTranslated(-20, 0, 0);
		// rotate aroung x = terrain.z / 2
		glut.glutSolidSphere(1.0, 20, 20);
		gl.glPopMatrix();

	}
	// set the rotate angle

}
