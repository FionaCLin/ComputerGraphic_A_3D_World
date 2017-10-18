package ass2.spec;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import ass2.spec.Game.Model;

public class Avatar {
	private double[] myPos;
	private Texture myTextures[] = new Texture[2];

	public Avatar(double x, double y, double z, Texture face, Texture body) {
		myPos = new double[3];
		myPos[0] = x;
		myPos[1] = y;
		myPos[2] = z;
		myTextures[0] = face;
		myTextures[1] = body;
	}

	public double[] getPosition() {
		return myPos;
	}

	public void drawAvatar(GL2 gl, GLUT glut) {
		gl.glPushMatrix();
		// Turn on OpenGL texturing.
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			gl.glScaled(.25, .25, .25);
			gl.glTranslated(0, 2.5, 0);
			gl.glColor3f(102f / 255, 0, 51f / 255);
			// bind the texture
			gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[1].getTextureId());

			GLU glu = new GLU();
            // the built in glut sphere does not have texture coordinates set
            //glut.glutSolidSphere(1, 20, 20);
            GLUquadric quadric = glu.gluNewQuadric();
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
            glu.gluSphere(quadric, 1, 10, 10);

			
//			glut.glutSolidSphere(1.0, 10, 10);
			gl.glPushMatrix();
				gl.glTranslated(0, -.5, 0);
				gl.glRotated(90, 1, 0, 0);
		
				gl.glColor3f(102f / 255, 51f / 255, 0);
				glu.gluQuadricTexture(quadric, true);
	            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
//				glut.glutSolidCylinder(0.5, 2, 20, 20);
				glu.gluCylinder(quadric,.5,.5,2, 20, 20);
			gl.glPopMatrix();
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
		System.out.println(myPos[0] + " x "+myPos[1]+" y "+myPos[2]+" z");
	}

}
