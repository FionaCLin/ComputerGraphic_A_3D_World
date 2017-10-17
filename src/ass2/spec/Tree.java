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


    public void drawTree(GL2 gl, GLUT glut) {


        drawCylinder2(gl, glut);
        drawSphere(gl,glut);


    }


    //copy form week 4 code
    public void drawCylinder(GL2 gl,int height, int slices,boolean cylinder) {

        gl.glColor3f(0,1,0);
        gl.glRotated(Math.PI/2, 1, 0, 0);


        double z1 = 0;
        double z2 = -height;


        gl.glPolygonMode(GL.GL_BACK,GL2.GL_LINE);

        //Front circle
        gl.glBegin(GL2.GL_TRIANGLE_FAN);{

            gl.glNormal3d(0,0,1);
            gl.glVertex3d(0,0,z1);
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                double a0 = i * angleStep;
                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

                gl.glVertex3d(x0,y0,z1);

            }
        }gl.glEnd();

        //Back circle
        gl.glBegin(GL2.GL_TRIANGLE_FAN);{

            gl.glNormal3d(0,0,-1);
            gl.glVertex3d(0,0,z2);
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){

                double a0 = 2*Math.PI - i * angleStep;

                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

                gl.glVertex3d(x0,y0,z2);
                //System.out.println("Back " + x0 + " " + y0);
            }


        }gl.glEnd();

        //Sides of the cylinder
        gl.glBegin(GL2.GL_QUADS);
        {
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                double a0 = i * angleStep;
                double a1 = ((i+1) % slices) * angleStep;

                //Calculate vertices for the quad
                double x0 = Math.cos(a0);
                double y0 = Math.sin(a0);

                double x1 = Math.cos(a1);
                double y1 = Math.sin(a1);

                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                if(cylinder){
                    gl.glNormal3d(x0, y0, 0);
                }else{
                    //Use the face normal for all 4 vertices in the quad.
                    gl.glNormal3d(-(z2-z1)*(y1-y0),(x1-x0)*(z2-z1),0);
                }

                gl.glVertex3d(x0, y0, z1);
                gl.glVertex3d(x0, y0, z2);

                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                if(cylinder)
                    gl.glNormal3d(x1, y1, 0);

                gl.glVertex3d(x1, y1, z2);
                gl.glVertex3d(x1, y1, z1);

            }

        }
        gl.glEnd();

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK,GL2.GL_FILL);
    }


    public void drawCylinder2(GL2 gl, GLUT glut){
        gl.glPushMatrix();
        gl.glTranslated(myPos[0], myPos[1], myPos[2]);
        gl.glRotated(-90,1,0,0);
        gl.glColor3f(102f / 255, 51f / 255, 0);
        //gl.glTranslated(0,0,-0.2);
        glut.glutSolidCylinder(0.06, 2, 20, 20);
        gl.glPopMatrix();
    }



    public void drawSphere(GL2 gl, GLUT glut){
        gl.glPushMatrix();
        gl.glTranslated(myPos[0], myPos[1], myPos[2]);
        gl.glRotated(-90,1,0,0);
        gl.glTranslated(0,0,2);
        gl.glColor3f(147f/255,247f/255,138f/255);
        glut.glutSolidSphere(0.6f, 10, 10);
        gl.glPopMatrix();

    }






}