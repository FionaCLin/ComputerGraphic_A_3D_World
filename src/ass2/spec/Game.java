package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

	private String textureNames[] = { "grass.bmp", "tree.jpg", "leaves.jpg", "road.png", "bearfur.jpg",
			"bearface.jpg" };
	private String textureExtensions[] = { "bmp", "jpg", "jpg", "png", "jpg", "jpg" };
	private int curTex;
	private boolean myModulate;
	private boolean mySpecularSep;
	private boolean mySoomth = true;

	public enum Model {
		Terrain, Tree, Leaves, Road, AvatarFur, AvatarFace
	}

	private Texture myTextures[];

	public Game(Terrain terrain) {
		super("Assignment 2");
		myTerrain = terrain;
		camera = new Camera(myTerrain.size());

	}

	/**
	 * Run the game.
	 *
	 */
	public void run() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLJPanel panel = new GLJPanel();

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
		Terrain terrain = LevelIO.load(new File("src/test4.json"));
		Game game = new Game(terrain);
		game.run();
	}

	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		setUpLighting(gl);

		gl.glLoadIdentity();
		camera.setCamera(this.myTerrain.vertex_mesh());
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

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glDisable(GL.GL_CULL_FACE);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		// material parameter set for metallic gold or brass

		float ambient[] = { 0.33f, 0.22f, 0.03f, 1.0f };
		float diffuse[] = { 0.78f, 0.57f, 0.11f, 1.0f };
		float specular[] = { 0.99f, 0.91f, 0.81f, 1.0f };
		float shininess = 27.8f;

		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);

		// Turn on OpenGL texturing.
		gl.glEnable(GL2.GL_TEXTURE_2D);

		// gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_DONT_CARE);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		// Load textures

		myTextures = new Texture[this.getNumTextures()];
		for (int i = 0; i < this.getNumTextures(); i++) {
			myTextures[i] = new Texture(this, gl, "src/texture/" + this.getTexName(i), this.getTexExtension(i), true);
		}
		Texture face = myTextures[Model.AvatarFace.ordinal()];
		Texture fur = myTextures[Model.AvatarFur.ordinal()];

		double camerax = camera.getCamerax();
		double cameraz = camera.getCameraz();

		person = new Avatar(camerax, myTerrain.altitude(camerax, cameraz - 6.5), cameraz - 6.5, face, fur);

	}

	public void setUpLighting(GL2 gl) {

		gl.glEnable(GL2.GL_LIGHTING);
		// When you enable lighting you must still actually
		// turn on a light such as this default light.

		// material parameter set for metallic gold or brass

		float ambient[] = { 0.3f, 0.3f, 0.3f, 1.0f };
		float diffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float specular[] = { 0.99f, 0.91f, 0.81f, 1.0f };
		float shininess = 27.8f;

		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);

		gl.glShadeModel(this.isSmooth() ? GL2.GL_SMOOTH : GL2.GL_FLAT);

		if (this.isSpecular()) {

			gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
		} else {
			gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SINGLE_COLOR);
		}
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
		myModel = Model.Terrain;
		// Turn on OpenGL texturing.
		gl.glEnable(GL2.GL_TEXTURE_2D);
		// bind the texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, myTextures[getTexId()].getTextureId());

		double[][][] verties = this.myTerrain.vertex_mesh();
		gl.glBegin(GL2.GL_TRIANGLES);
		{
			for (int i = 0; i < verties.length; i++) {
				for (int j = 0; j < verties[i].length; j++) {
					if (j != 3) {
						double[] vertex = verties[i][j];
						// myTextures[getTexId()].draw(gl, j);

						double[] textCoord = { 0, 0, 1, 0, 1, 1 };

						gl.glTexCoord2d(textCoord[j * 2], textCoord[j * 2 + 1]);
						gl.glVertex3d(vertex[0], vertex[1], vertex[2]);
						// gl.glVertex3dv(vertex, 0);
					} else {
						double[] normal = verties[i][j];
						gl.glNormal3dv(normal, 0);
					}
				}
			}
		}
		gl.glEnd();

		// draw trees
		List<Tree> trees = myTerrain.trees();
		for (Tree t : trees) {

			t.setTextures(myTextures[Model.Leaves.ordinal()], myTextures[Model.Tree.ordinal()]);
			t.drawTree(gl);
		}

		List<Road> roads = myTerrain.roads();
		for(Road r : roads){
			r.setTextures(myTextures[Model.Road.ordinal()]);
			r.drawRoad(gl);
		}

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
		case KeyEvent.VK_Q:
			if (isfollowing) {
				camera.leftAngleAroundPerson();
			}

			break;
		case KeyEvent.VK_E:
			if (isfollowing) {
				camera.rightAngleAroundPerson();
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