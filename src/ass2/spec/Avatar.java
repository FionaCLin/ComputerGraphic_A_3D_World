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
//	private double step =.1;
	private double angle = 0;
	public Avatar(double x, double y, double z, Texture face, Texture body) {
		myPos = new double[3];
		myPos[0] = x;
		myPos[1] = y;
		myPos[2] = z;
		myTextures[0] = face;
		myTextures[1] = body;
	}

	public void drawAvatar(GL2 gl, GLUT glut) {
		
		
		gl.glPushMatrix();
		// Turn on OpenGL texturing.
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			gl.glScaled(.25, .25, .25);
			gl.glTranslated(0, 2.5, 0);
			gl.glColor3f(102f / 255, 0, 51f / 255);

			GLU glu = new GLU();
			gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[1].getTextureId());
			GLUquadric quadric = glu.gluNewQuadric();
            
			
			// draw body
			gl.glPushMatrix();
				gl.glTranslated(0, -.5, 0);
				gl.glRotated(90, 1, 0, 0);
		
				gl.glColor3f(102f / 255, 51f / 255, 0);
				glu.gluQuadricTexture(quadric, true);
	            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
				glu.gluCylinder(quadric,.5,.5,2, 20, 20);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glRotated(-90, 1, 0, 0);
				gl.glRotated(angle, 0, 0, 1);
				//draw face
				gl.glPushMatrix();
	
				// bind the texture
				gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[0].getTextureId());
	            
				
				glu.gluQuadricTexture(quadric, true);
	            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
	            glu.gluSphere(quadric, 1, 10, 10);
	        	// bind the texture
	
				gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[1].getTextureId());
				gl.glPopMatrix();				
				
				// draw ears
	        	gl.glPushMatrix();
	        	
					gl.glTranslated(-.75, .85, 0);
					gl.glRotated(90, 1, 0, 0);
					gl.glColor3f(102f / 255, 51f / 255, 0);
					glu.gluQuadricTexture(quadric, true);
		            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
					glu.gluSphere(quadric,.5, 10, 10);
				
				gl.glPopMatrix();				
				gl.glPushMatrix();
	
					gl.glTranslated(.75, .85, 0);
					gl.glRotated(90, 1, 0, 0);
					gl.glColor3f(102f / 255, 51f / 255, 0);
					glu.gluQuadricTexture(quadric, true);
		            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
		            glu.gluSphere(quadric,.5, 10, 10);
					
				gl.glPopMatrix();
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
		System.out.println(myPos[0] + " x " + myPos[1]+" y "+myPos[2]+" z");
	}

//	public void up(double height) {
//		double dirx = Math.sin(Math.toRadians(angle));
//		double dirz = -Math.cos(Math.toRadians(angle));
//		myPos[0] -= dirx * step;
//		myPos[1] = height;
//		myPos[2] -= dirx * step;
//		System.out.println(" angle "+angle + myPos[0] + " x " + myPos[1]+" y "+myPos[2]+" z");
//	}
//
//	public void down(double height) {
//		double dirx = Math.sin(Math.toRadians(angle));
//		double dirz = -Math.cos(Math.toRadians(angle));
//		myPos[0] += dirx * step;
//		myPos[1] = height;
//		myPos[2] += dirx * step;
//		System.out.println(" angle "+angle +myPos[0] + " x " + myPos[1]+" y "+myPos[2]+" z");
//	}
//
	//head rotation
	public void left() {
		if( angle > -45)
			angle  = (angle - 10) % 360;
		System.out.println(angle + " angle " + myPos[0] + " x "+myPos[1]+" y "+myPos[2]+" z");
	}

	public void right() {
		if (angle < 45)
			angle = (angle + 10) % 360;
		System.out.println(angle + " angle " + myPos[0] + " x "+myPos[1]+" y "+myPos[2]+" z");
	}
}
