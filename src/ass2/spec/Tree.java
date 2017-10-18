package ass2.spec;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */


import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;


public class Tree {
	private Texture myTextures[] = new Texture[2];
    private double[] myPos;

    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }

    public void setTextures(Texture leaves, Texture trunk){
    	myTextures[0] = leaves;
		myTextures[1] = trunk;
    }
    public double[] getPosition() {
        return myPos;
    }

	public void drawTree(GL2 gl) {
		gl.glPushMatrix();
		// Turn on OpenGL texturing.
			gl.glTranslated(myPos[0], myPos[1], myPos[2]);
			gl.glScaled(.5, .5, .5);
			gl.glTranslated(0, 2.5, 0);
			gl.glColor3f(102f / 255, 0, 51f / 255);
			// bind the texture
			gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[0].getTextureId());

			GLU glu = new GLU();
            GLUquadric quadric = glu.gluNewQuadric();
            glu.gluQuadricTexture(quadric, true);
            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
            glu.gluSphere(quadric, 1, 10, 10);

			
			gl.glPushMatrix();
				gl.glTranslated(0, -.5, 0);
				gl.glRotated(90, 1, 0, 0);
				// bind the texture
				gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[1].getTextureId());

				gl.glColor3f(102f / 255, 51f / 255, 0);
				glu.gluQuadricTexture(quadric, true);
	            glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
				glu.gluCylinder(quadric,.5,.5,2, 20, 20);
			gl.glPopMatrix();
		gl.glPopMatrix();

	}
//
//    public void drawTree(GL2 gl) {
//        /*//Set Trunk Material Properties from week5 slide
//        float[] amb = {0.2f, 0.15f, 0.2f, 1.0f};
//        float[] diff = {0.2f, 0.1f, 0.0f, 1.0f};
//        float[] spe = {0.5f, 0.5f, 0.5f, 1.0f};
//        float phong = 50f;
//        GLU glu = new GLU();
//
//        gl.glPushMatrix();
//        {   //draw trunk
//            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);
//            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diff, 0);
//            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spe, 0);
//            gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, phong);
//
//            //make trunck using cylinder
//            //GLU glu = new GLU();
//            gl.glTranslated(myPos[0], myPos[1], myPos[2]);
//            gl.glRotated(-90.0, 1, 0, 0);
//            GLUquadric gluQuadratic = glu.gluNewQuadric();
//            glu.gluQuadricTexture(gluQuadratic, true);
//            glu.gluQuadricNormals(gluQuadratic, GLU.GLU_SMOOTH);
//            glu.gluCylinder(gluQuadratic, 0.07f, 0.07f, 0.7f, 60, 60);
//        }
//        gl.glPopMatrix();*/
//        drawCylinder(gl,15,40,false);
//    }
//
//
//    //copy form week 4 code
//    public void drawCylinder(GL2 gl,int height, int slices,boolean cylinder) {
//
//        /*int height = 10;
//        int slices = 32;
//        boolean cylinder =true;*/
//
//
//        gl.glColor3f(1,0,0);
//        gl.glTranslated(myPos[0], myPos[1], myPos[2]);
//        gl.glRotated(0, 1, 0, 0);
//
//
//        double z1 = 0;
//        double z2 = -height;
//
//
//        gl.glPolygonMode(GL.GL_BACK,GL2.GL_LINE);
//
//        //Front circle
//        gl.glBegin(GL2.GL_TRIANGLE_FAN);{
//
//            gl.glNormal3d(0,0,1);
//            gl.glVertex3d(0,0,z1);
//            double angleStep = 2*Math.PI/slices;
//            for (int i = 0; i <= slices ; i++){
//                double a0 = i * angleStep;
//                double x0 = Math.cos(a0);
//                double y0 = Math.sin(a0);
//
//                gl.glVertex3d(x0,y0,z1);
//
//            }
//        }gl.glEnd();
//
//        //Back circle
//        gl.glBegin(GL2.GL_TRIANGLE_FAN);{
//
//            gl.glNormal3d(0,0,-1);
//            gl.glVertex3d(0,0,z2);
//            double angleStep = 2*Math.PI/slices;
//            for (int i = 0; i <= slices ; i++){
//
//                double a0 = 2*Math.PI - i * angleStep;
//
//                double x0 = Math.cos(a0);
//                double y0 = Math.sin(a0);
//
//                gl.glVertex3d(x0,y0,z2);
//                //System.out.println("Back " + x0 + " " + y0);
//            }
//
//
//        }gl.glEnd();
//
//        //Sides of the cylinder
//        gl.glBegin(GL2.GL_QUADS);
//        {
//            double angleStep = 2*Math.PI/slices;
//            for (int i = 0; i <= slices ; i++){
//                double a0 = i * angleStep;
//                double a1 = ((i+1) % slices) * angleStep;
//
//                //Calculate vertices for the quad
//                double x0 = Math.cos(a0);
//                double y0 = Math.sin(a0);
//
//                double x1 = Math.cos(a1);
//                double y1 = Math.sin(a1);
//
//                //If we want it to be smooth like a cylinder
//                //use different normals for each different x and y
//                if(cylinder){
//                    gl.glNormal3d(x0, y0, 0);
//                }else{
//                    //Use the face normal for all 4 vertices in the quad.
//                    gl.glNormal3d(-(z2-z1)*(y1-y0),(x1-x0)*(z2-z1),0);
//                }
//
//                gl.glVertex3d(x0, y0, z1);
//                gl.glVertex3d(x0, y0, z2);
//
//                //If we want it to be smooth like a cylinder
//                //use different normals for each different x and y
//                if(cylinder)
//                    gl.glNormal3d(x1, y1, 0);
//
//                gl.glVertex3d(x1, y1, z2);
//                gl.glVertex3d(x1, y1, z1);
//
//            }
//
//        }
//        gl.glEnd();
//
//        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
//    }



}