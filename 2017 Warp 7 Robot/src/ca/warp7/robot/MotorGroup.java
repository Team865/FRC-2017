package ca.warp7.robot;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.SpeedController;

public class MotorGroup implements SpeedController {
	private SpeedController[] motors;
	private boolean isInverted;

	public MotorGroup(int[] pins, Class<?> type) {
		assert type.isAssignableFrom(SpeedController.class); // if this fails u borked
		motors = new SpeedController[pins.length];
		for (int i = 0; i < pins.length; i++) {
			try {
				motors[i] = (SpeedController) type.getConstructor(Integer.TYPE).newInstance(pins[i]);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				// pls no
				e.printStackTrace();
			}
		}
	}

	@Override
	public void set(double speed) {
		for (SpeedController motor : motors) {
			motor.set(speed);
		}
	}

	@Override
	public void pidWrite(double output) {
		for (SpeedController motor : motors) {
			motor.pidWrite(output);
		}
	}

	@Override
	public double get() {
		return motors[0].get(); // Really, they should all be the same.
	}

	@Override
	public void setInverted(boolean isInverted) {
		this.isInverted = isInverted;
		for (SpeedController motor : motors) {
			motor.setInverted(isInverted);
		}
	}
	
	// THIS IS DANGEROUS.
	/*
	 * public void setInverted(int index, boolean isInverted) {
	 * motors[index].setInverted(isInverted); }
	 */

	@Override
	public boolean getInverted() {
		return isInverted;
	}

	@Override
	public void disable() {
		for (SpeedController motor : motors) {
			motor.disable();
		}
	}

	@Override
	public void stopMotor() {
		for (SpeedController motor : motors) {
			motor.stopMotor();
		}
	}
}