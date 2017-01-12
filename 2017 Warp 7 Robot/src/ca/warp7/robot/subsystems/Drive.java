package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.DRIVE_METERS_PER_TICK;
import static ca.warp7.robot.Constants.GEAR_SHIFTER_PORT;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.LEFT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.Constants.DROP_DOWN_MOTOR_PINS;

import ca.warp7.robot.MotorGroup;
import ca.warp7.robot.Util;
import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Drive{
	
	private MotorGroup rightDrive;
	private MotorGroup leftDrive;
	private MotorGroup dropDrive;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	private Solenoid shifter;
	private ADXRS450_Gyro gyro;
	private DataPool pool;
	private double leftRamp = 0.0;
	private double rightRamp = 0.0;
	private double quickstop_accumulator = 0;
	private double old_wheel = 0;
    private boolean isDrivetrainReversed = false;
    
    
    public Drive() {
		pool = new DataPool("Drive");

		// setup drive train motors
		rightDrive = new MotorGroup(RIGHT_DRIVE_MOTOR_PINS, VictorSP.class);
		rightDrive.setInverted(true);
		leftDrive = new MotorGroup(LEFT_DRIVE_MOTOR_PINS, VictorSP.class);
		dropDrive = new MotorGroup(DROP_DOWN_MOTOR_PINS, VictorSP.class);

		// setup drive train gear shifter
        shifter = new Solenoid(GEAR_SHIFTER_PORT);
		shifter.set(false);
		
		// setup drive train encoders
		leftEncoder =  new Encoder(LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		rightEncoder = new Encoder(RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(DRIVE_METERS_PER_TICK);
		rightEncoder.setDistancePerPulse(DRIVE_METERS_PER_TICK);
		
		// setup gyro
		gyro = new ADXRS450_Gyro();
	}

	public void setGear(boolean gear) {
		shifter.set(gear);
	}

	public void tankDrive(double left, double right) {
		pool.logDouble("desiredLeft", left);
		pool.logDouble("desiredRight", right);
		moveRamped(left, right);
	}

	public void cheesyDrive(double wheel, double throttle, boolean quickturn, boolean dropDown) {
		/*
		 * Poofs! :param wheel: The speed that the robot should turn in the X
		 * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
		 * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
		 * :param quickturn: If the robot should drive arcade-drive style
		 */
		throttle = Util.deadband(throttle);
		wheel = Util.deadband(wheel);
		if(isDrivetrainReversed)
			wheel*=-1;
		double right_pwm;
		double left_pwm;
		double neg_inertia_scalar;
		double neg_inertia = wheel - old_wheel;
		old_wheel = wheel;
		wheel = Util.sinScale(wheel, 0.8f, 3, 0.6);

		if (wheel * neg_inertia > 0) {
			neg_inertia_scalar = 2.5f;
		} else {
			if (Math.abs(wheel) > .65) {
				neg_inertia_scalar = 6;
			} else {
				neg_inertia_scalar = 4;
			}
		}

		double neg_inertia_accumulator = neg_inertia * neg_inertia_scalar;

		wheel += neg_inertia_accumulator;

		double over_power, angular_power;
		if (quickturn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				quickstop_accumulator = (1 - alpha) * quickstop_accumulator + alpha * Util.limit(wheel, 1.0) * 5;
			}
			over_power = 1;
			angular_power = -wheel * .85;
		} else {
			over_power = 0;
            double sensitivity = .9;
            angular_power = throttle * wheel * sensitivity - quickstop_accumulator;
			quickstop_accumulator = Util.wrap_accumulator(quickstop_accumulator);
		}
		
		if(dropDown && !quickturn){
			dropDrive.set(Util.limit(wheel, 1.0));
		}else{
			dropDrive.set(0.0);
		}
		
		right_pwm = left_pwm = throttle;

		left_pwm += angular_power;
		right_pwm -= angular_power;

		if (left_pwm > 1) {
			right_pwm -= over_power * (left_pwm - 1);
			left_pwm = 1;
		} else if (right_pwm > 1) {
			left_pwm -= over_power * (right_pwm - 1);
			right_pwm = 1;
		} else if (left_pwm < -1) {
			right_pwm += over_power * (-1 - left_pwm);
			left_pwm = -1;
		} else if (right_pwm < -1) {
			left_pwm += over_power * (-1 - right_pwm);
			right_pwm = -1;
		}
        if(isDrivetrainReversed) {
            left_pwm *= -1;
            right_pwm *= -1;
        }
		if(shifter.get()) { // if low gear
			leftDrive.set(left_pwm);
			rightDrive.set(right_pwm);
		} else {
			moveRamped(left_pwm, right_pwm);
		}
	}

	public void moveRamped(double desiredLeft, double desiredRight) {
		double ramp_speed = 6;
		leftRamp += (desiredLeft - leftRamp) / ramp_speed;
		rightRamp += (desiredRight - rightRamp) / ramp_speed;
		leftDrive.set(leftRamp);
		rightDrive.set(rightRamp);
	}

	public void stop() {
		rightDrive.set(0);
		leftDrive.set(0);
	}

	public void autoMove(double left, double right) {
		leftDrive.set(left);
		rightDrive.set(right);
	}

	public double getRotation() {
		return gyro.getAngle();
	}

	public void slowPeriodic() {
		pool.logDouble("gyro_angle", getRotation());
		pool.logDouble("left_enc", leftEncoder.getDistance());
		pool.logDouble("right_enc", rightEncoder.getDistance());
	}

    public void setDrivetrainReversed(boolean reversed) {
        isDrivetrainReversed = reversed;
    }

    public boolean isDrivetrainReversed() {
        return isDrivetrainReversed;
    }
}
