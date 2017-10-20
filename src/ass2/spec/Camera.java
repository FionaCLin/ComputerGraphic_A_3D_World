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
	private double dstFromPerson = 4;
	private double angleAroundPerson = 0;
	private boolean isfollow;

	private Dimension size;
	private double cameray;

	public Camera() {
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

	public void setCamera() {
		GLU glu = new GLU();
		double[] eyes = person.getMyPos();
		double[] centre = person.getMyPos();

		// Compass direction.
		double[] dir = { 0, 0, 0 };

		if (!isfollow) {
			dir[0] = Math.sin(Math.toRadians(angle));
			dir[1] = .5;
			dir[2] = -Math.cos(Math.toRadians(angle));

			centre[0] = eyes[0] + dir[0];
			centre[1] = eyes[1] + dir[1];
			centre[2] = eyes[2] + dir[2];
			System.out.println(dir[0] + " x " + dir[1] + " y " + dir[2] + " z " + angle + " angle");
			System.out.println(" x " + centre[0] + " y " + centre[1] + " z " + centre[2] + " angle " + angle);
		} else {
			centre = person.getMyPos(); // look at person
			dir[0] = Math.sin(Math.toRadians(angleAroundPerson));
			dir[2] = Math.cos(Math.toRadians(angleAroundPerson));

			eyes[0] = person.getMyPos()[0] + dstFromPerson * dir[0]; // look at
																		// person
			eyes[1] = person.getMyPos()[1] + dstAbovePerson;
			eyes[2] = person.getMyPos()[2] + dstFromPerson * dir[2];

			System.out.println("xxx" + eyes[0] + " Y " + eyes[1] + " Z " + eyes[2] + angleAroundPerson);
			// System.out.println(dir[0] + " x " + dir[1] + " y " + dir[2] + " z
			// " + angleAroundPerson + " angle");
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
		// set min height 
		camerax = person.getMyPos()[0] + 1;
		cameray = person.getMyPos()[1] + 3 ; 		// set min height 
		cameraz = person.getMyPos()[2] + 1;

	}

	public void rightAngleAroundPerson() {
		angleAroundPerson = (angleAroundPerson + 10) % 360;
	}

	public void leftAngleAroundPerson() {
		angleAroundPerson = (angleAroundPerson - 10) % 360;

	}

	public void setFollow() {
		isfollow = !isfollow;
	}

	public boolean isFollow() {
		return isfollow;
	}

}
