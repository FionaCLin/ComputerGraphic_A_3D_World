package ass2.spec;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Texture {

	private boolean mipMapEnabled = true;

	private int[] textureID = new int[1];

	private Game myModel;
	private Texture myTextures[];

	// Create a texture from a file. Make sure the file has a width and height
	// that is a power of 2
	public Texture(Game model, GL2 gl, String fileName, String extension, boolean mipmaps) {
		myModel = model;
		mipMapEnabled = mipmaps;
		TextureData data = null;
		try {
			System.out.println(fileName + "<<<<<<<<");
			File file = new File(fileName);
			BufferedImage img = ImageIO.read(file); // read file into
													// BufferedImage
			ImageUtil.flipImageVertically(img);

			// This library will result in different formats being upside down.
			// data = TextureIO.newTextureData(GLProfile.getDefault(), file,
			// false,extension);

			// This library call flips all images the same way
			data = AWTTextureIO.newTextureData(GLProfile.getDefault(), img, false);
		
		} catch (IOException exc) {
			System.err.println(fileName);
			exc.printStackTrace();
			System.exit(1);
		}

		gl.glGenTextures(1, textureID, 0);
		//The first time bind is called with the given id,
		//an openGL texture object is created and bound to the id
		//It also makes it the current texture.
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);

		 // Build texture initialised with image data.
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0,
        				data.getInternalFormat(),
        				data.getWidth(),
        				data.getHeight(),
        				0,
        				data.getPixelFormat(),
        				data.getPixelType(),
        				data.getBuffer());
		
        setFilters(gl);

	}

	private void setFilters(GL2 gl) {
		// Build the texture from data.
		if (mipMapEnabled) {
			// Set texture parameters to enable automatic mipmap generation and
			// bilinear/trilinear filtering
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);

			float fLargest[] = new float[1];
			gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fLargest, 0);
			System.out.println(fLargest[0]);
			gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, fLargest[0]);
			gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
		} else {
			// Set texture parameters to enable bilinear filtering.
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		}

	}

	public Texture(GL2 gl, ByteBuffer buffer, int size, boolean mipmaps) {
		mipMapEnabled = mipmaps;
		gl.glGenTextures(1, textureID, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);

		// Specify image data for currently active texture object.
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, size, size, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);

		setFilters(gl);

	}

	public int getTextureId() {
		return textureID[0];
	}

	public void release(GL2 gl) {
		if (textureID[0] > 0) {
			gl.glDeleteTextures(1, textureID, 0);
		}
	}

	public void draw(GL2 gl,int j) {
		

		switch (myModel.getModel()) {

		case Terrain:

			double[] textCoord = { 0, 0, 1, 0, 1, 1 };

			gl.glTexCoord2d(textCoord[j * 2], textCoord[j * 2 + 1]);

			break;

		// case Tree:
		// GLU glu = new GLU();
		// // the built in glut sphere does not have texture coordinates set
		// //glut.glutSolidSphere(1, 20, 20);
		// GLUquadric quadric = glu.gluNewQuadric();
		// glu.gluQuadricTexture(quadric, true);
		// glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);
		// glu.gluSphere(quadric, 1, 64, 64);
		//
		// break;

		// case TORUS:
		// // the built in glut torus does not have texture coordinates set
		// glut.glutSolidTorus(0.5, 1.5, 20, 20);
		//
		// break;
		//
		// case Avatar:
		//
		// gl.glPushMatrix();
		//
		// gl.glPopMatrix();
		}

	}

	private void setLighting(GL2 gl) {
		gl.glShadeModel(myModel.isSmooth() ? GL2.GL_SMOOTH : GL2.GL_FLAT);
		//
		// // rotate the light
		// gl.glMatrixMode(GL2.GL_MODELVIEW);
		// gl.glPushMatrix();
		// gl.glRotated(myRotateLightX, 1, 0, 0);
		// gl.glRotated(myRotateLightY, 0, 1, 0);
		//
		// float[] pos = new float[] { 0.0f, 0.0f, 4.0f, 1.0f };
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
		// gl.glPopMatrix();
		//
		// // set the intensities
		//
		// float ambient = myModel.getAmbient();
		// float diffuse = myModel.getDiffuse();
		// float specular = myModel.getSpecular();
		//
		// float[] a = new float[4];
		// a[0] = a[1] = a[2] = ambient;
		// a[3] = 1.0f;
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, a, 0);
		//
		// float[] d = new float[4];
		// d[0] = d[1] = d[2] = diffuse;
		// d[3] = 1.0f;
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, d, 0);
		//
		// float[] s = new float[4];
		// s[0] = s[1] = s[2] = specular;
		// s[3] = 1.0f;
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, s, 0);

		if (myModel.isSpecular()) {

			gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
		} else {
			gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SINGLE_COLOR);
		}
	}
}
