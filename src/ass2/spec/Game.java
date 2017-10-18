package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Game
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, MouseMotionListener, KeyListener {

	private Terrain myTerrain;
	private Avatar person;
	private Camera camera;

	private boolean isfollowing = false;

	// texture files
	private Model myModel = Model.Terrain;
	private boolean mySmooth = false;

	private String textureNames[] = { "grass.bmp", "tree.jpg", "road.png", "avatar_face.png" };
	private String textureExtensions[] = { "bmp", "jpg", "png", "png" };
	private int curTex;
	private boolean myModulate;
	private boolean mySpecularSep;
	private boolean mySoomth;

	public enum Model {
		Terrain, Tree, Road, Avatar
	}

	private Texture myTextures[];

	public Game(Terrain terrain) {
		super("Assignment 2");
		myTerrain = terrain;
		camera = new Camera(myTerrain.size());
		double camerax = camera.getCamerax();
		double cameraz = camera.getCameraz();
		person = new Avatar(camerax, myTerrain.altitude(camerax, cameraz - 6.5), cameraz - 6.5);

	}

	/**
	 * Run the game.
	 *
	 */
	public void run() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLJPanel panel = new GLJPanel();
		
		
		TextureView view1 = new TextureView(this);
		panel.addGLEventListener(view1);

		// add a GL Event listener to handle rendering
		panel.addGLEventListener(this);

		// NEW: add a key listener to respond to keypresses
		panel.addKeyListener(this);
		// the panel needs to be focusable to get key events
		panel.setFocusable(true);

		// Add an animator to call 'display' at 60fps
		FPSAnimator animator = new FPSAnimator(60);
		animator.add(panel);
		animator.start();

		getContentPane().add(panel);
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Load a level file and display it.
	 * 
	 * @param args
	 *            - The first argument is a level file in JSON format
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Terrain terrain = LevelIO.load(new File(args[0]));
		Game game = new Game(terrain);
		game.run();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		// clear the window and depth buffer
		setUpLighting(gl);
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		camera.setCamera(this.myTerrain.vertex_mesh());

		// bind the texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[getTexId()].getTextureId());
		// use the texture to modulate diffuse and ambient lighting
		if (getModulate()) {
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		} else {
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
		}

		gl.glColor3f(0, 0.5f, 0);
		draw(gl);

		GLUT glut = new GLUT();

		glut.glutSolidSphere(1.0, 20, 20);
		person.drawAvatar(gl, glut);

		// gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		// By enabling lighting, color is worked out differently.
		// normalise normals (!)
		// this is necessary to make lighting work properly
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glDisable(GL.GL_CULL_FACE);

		gl.glEnable(GL2.GL_LIGHTING);
		// When you enable lighting you must still actually
		// turn on a light such as this default light.

		// Light property vectors.
		float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float lightPos[] = myTerrain.getSunlight();
		float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };

		// Light properties.
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

		gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb, 0); // Global
																	// ambient
																	// light.
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable
																	// two-sided
																	// lighting.
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable
																		// local
																		// viewpoint.

		// Material property vectors.
		float matAmbAndDif2[] = { 0.9f, 0.0f, 0.0f, 1.0f };
		float matAmbAndDif1[] = { 0.0f, 0.9f, 0.0f, 1.0f };
		float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float matShine[] = { 50.0f };

		// Material properties.
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1, 0);
		gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine, 0);

		// Enable color material mode:
		// The ambient and diffuse color of the front faces will track the color
		// set by glColor().
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		// enable texturing
		gl.glEnable(GL.GL_TEXTURE_2D);
		// Load textures
		myTextures = new Texture[this.getNumTextures()];
		for (int i = 0; i < this.getNumTextures(); i++) {
			myTextures[i] = new Texture(gl, "src/texture/" + this.getTexName(i), this.getTexExtension(i), true);
		}

	}

	public void setUpLighting(GL2 gl) {
		// Light property vectors.
		float lightPos[] = { 0.0f, 1.5f, 3.0f, 1.0f };

		float lightAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float lightDifAndSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float globAmb[] = { 0.2f, 0.2f, 0.2f, 1.0f };

		gl.glEnable(GL2.GL_LIGHT0); // Enable particular light source.

		// Set light properties.
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDifAndSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightDifAndSpec, 0);

		// This position gets multiplied by current transformation

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb, 0); // Global
																	// ambient
																	// light.
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE); // Enable
																	// two-sided
																	// lighting.
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		double w = this.myTerrain.size().getWidth();
		double h = this.myTerrain.size().getHeight();

		GLU glu = new GLU();
		glu.gluPerspective(60.0, (float) w / (float) h, 1.0, 200.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	private void draw(GL2 gl) {
		// draw the Terrian
		double[][][] verties = this.myTerrain.vertex_mesh();
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			for (int i = 0; i < verties.length; i++) {
				for (int j = 0; j < verties[i].length; j++) {
					if (j != 3) {
						double[] vertex = verties[i][j];
						gl.glVertex3dv(vertex, 0);
					} else {
						double[] normal = verties[i][j];
						gl.glNormal3dv(normal, 0);
					}
				}
			}
		}
		gl.glEnd();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		double[] pos = person.getMyPos();
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			if (pos[2] > 1) {
				pos[2] -= .5;
				pos[1] = myTerrain.altitude(pos[0], pos[2]);
				person.setMyPos(pos);
			}
			break;
		case KeyEvent.VK_S:
			if (pos[2] < myTerrain.size().getWidth() - 1) {
				pos[2] += .5;
				pos[1] = myTerrain.altitude(pos[0], pos[2]);
				person.setMyPos(pos);
			}
			break;
		case KeyEvent.VK_A:
			if (pos[0] > 1) {
				pos[0] -= .5;
				pos[1] = myTerrain.altitude(pos[0], pos[2]);
				person.setMyPos(pos);
			}
			break;
		case KeyEvent.VK_D:
			if (pos[0] < myTerrain.size().getHeight() - 1) {
				pos[0] += .5;
				pos[1] = myTerrain.altitude(pos[0], pos[2]);
				person.setMyPos(pos);
			}
			break;

		case KeyEvent.VK_SPACE:
			isfollowing = (!isfollowing);
			if (isfollowing)
				camera.setPerson(person);
			else
				camera.setPerson(null);
			break;
		case KeyEvent.VK_UP:
			camera.up();
			break;
		case KeyEvent.VK_DOWN:

			camera.down();
			break;
		case KeyEvent.VK_RIGHT:
			camera.right();

			break;
		case KeyEvent.VK_LEFT:
			camera.left();
			break;
		default:
			break;
		}
		System.out.println(camera.getAngle());

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean getModulate() {
		return myModulate;
	}

	public int getNumTextures() {
		return textureNames.length;
	}

	public int getTexId() {
		return curTex;
	}

	public String getTexName(int i) {
		return textureNames[i];

	}

	public String getTexExtension(int i) {
		return textureExtensions[i];
	}

	public Model getModel() {
		return myModel;
	}

	public boolean isSmooth() {
		return mySoomth;
	}

	public boolean isSpecular() {
		return mySpecularSep;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}