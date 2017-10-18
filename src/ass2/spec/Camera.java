package ass2.spec;

import java.awt.Dimension;

import com.jogamp.opengl.glu.GLU;

public class Camera {
	// the compass direction of the camera in degrees
	private double angle = 0;
	private double camerax;
	private double cameraz;
	private Avatar person;
	private double step = .1;
	private double dstAbovePerson = 3;
	private double dstAwayPerson = 4 ;
	private double angleAroundPerson = 10;

	private Dimension size;

	public Camera(Dimension size) {
		this.size = size;
		camerax = size.getHeight() / 2;
		cameraz = size.getWidth() + 5;
	}

	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * @return the camerax
	 */
	public double getCamerax() {
		return camerax;
	}

	/**
	 * @param camerax
	 *            the camerax to set
	 */
	public void setCamerax(double camerax) {
		this.camerax = camerax;
	}

	/**
	 * @return the cameraz
	 */
	public double getCameraz() {
		return cameraz;
	}

	/**
	 * @param cameraz
	 *            the cameraz to set
	 */
	public void setCameraz(double cameraz) {
		this.cameraz = cameraz;
	}

	public void setCamera(double[][][] verties) {
		GLU glu = new GLU();
		double[] eyes = { size.getHeight() / 2, size.getHeight() / 2, size.getWidth() + 2 };
		double[] centre = { size.getHeight() / 2, 0.0, size.getWidth() / 2 };

		eyes[0] = camerax;
		eyes[1] = 3; // Minimum height of camera.
		eyes[2] = cameraz;

		// Find max height of nearby terrain points.
		double radius = 2.;

		for (int i = 0; i < verties.length; i++) {
			for (int j = 0; j < 3; j++) {
				double[] vertex = verties[i][j];
				if (Math.abs(camerax - vertex[0]) < radius && Math.abs(cameraz - vertex[2]) < radius)
					eyes[1] = Math.max(eyes[1], vertex[1] + 1.5);
			}
		}
		// System.out.println("height = " + eyes[1]);

		// Compass direction.
		double[] dir = { 0, 0, 0 };

		if (person == null) {
			dir[0] = Math.sin(Math.toRadians(angle));
			dir[1] = -.5;
			dir[2] = -Math.cos(Math.toRadians(angle));

			centre[0] = eyes[0] + dir[0];
			centre[1] = eyes[1] + dir[1];
			centre[2] = eyes[2] + dir[2];
			System.out.println(eyes[0] + "xxx angle " + angle);
			System.out.println(dir[0] + " x " + dir[1] + " y " + dir[2] + " z " + angle + " angle");
			System.out.println(" x " + centre[0] + " y " + centre[1] + " z " + centre[2] + " angle " + angle);
		} else {
			double[] camera = person.getMyPos(); // look at person
			dir[0] = Math.sin(Math.toRadians(angleAroundPerson));
			dir[1] = -.5;
			dir[2] = -Math.cos(Math.toRadians(angleAroundPerson));

			// eyes = person.getMyPos(); // look at person
			eyes[1] = camera[1] + this.dstAbovePerson;
			eyes[2] = camera[2] + this.dstAwayPerson;
			centre[0] = camera[0] + dir[0];
			centre[1] = camera[1] + dir[1];
			centre[2] = camera[2] + dir[2];

			System.out.println("xxx" + eyes[0] + " Y " + eyes[1] + " Z " + eyes[2] + angleAroundPerson);
			System.out.println(dir[0] + " x " + dir[1] + " y " + dir[2] + " z " + angleAroundPerson + " angle");
			System.out.println(
					" ####x " + centre[0] + " y " + centre[1] + " z " + centre[2] + " angle " + angleAroundPerson);
		}
		// System.out.println(">>>" + angle);
		glu.gluLookAt(eyes[0], eyes[1], eyes[2], centre[0], centre[1], centre[2], 0.0, 1.0, 0.0);
	}

	public void up() {
		double dirx = Math.sin(Math.toRadians(angle));
		double dirz = -Math.cos(Math.toRadians(angle));
		camerax += dirx * step;
		cameraz += dirz * step;
	}

	public void down() {
		double dirx = Math.sin(Math.toRadians(angle));
		double dirz = -Math.cos(Math.toRadians(angle));

		camerax -= dirx * step;
		cameraz -= dirz * step;
	}

	public void right() {
		angle = (angle + 10) % 360;
	}

	public void left() {
		angle = (angle - 10) % 360;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(Avatar person) {
		this.person = person;
	}

	public void rightAngleAroundPerson() {
		angleAroundPerson = (angleAroundPerson + 10) % 360;
	}

	public void leftAngleAroundPerson() {
		angleAroundPerson = (angleAroundPerson - 10) % 360;

	}

}
