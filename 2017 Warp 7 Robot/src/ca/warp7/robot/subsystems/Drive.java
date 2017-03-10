package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.DRIVE_INCHES_PER_TICK;
import static ca.warp7.robot.Constants.DRIVE_SHIFTER_PORT;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.LEFT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.misc.Util.limit;

import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.misc.MotorGroup;
import ca.warp7.robot.misc.Util;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Drive{
	
	public static DataPool drivePool;
	
	private MotorGroup rightDrive;
	private MotorGroup leftDrive;
	private Solenoid shifter;
	private ADXRS450_Gyro gyro;
	private Encoder leftEncoder;
	private Encoder rightEncoder;
	
	private double quickstop_accumulator = 0;
	private double old_wheel = 0;
    private boolean driveReversed = false;
    
    
    public Drive() {
		drivePool = new DataPool("Drive");

		// setup drive train motors
		rightDrive = new MotorGroup(RIGHT_DRIVE_MOTOR_PINS, VictorSP.class);
		rightDrive.setInverted(true);
		leftDrive = new MotorGroup(LEFT_DRIVE_MOTOR_PINS, VictorSP.class);

		// setup drive train gear shifter
        shifter = new Solenoid(DRIVE_SHIFTER_PORT);
		shifter.set(false);
		
		// setup drive train encoders
		leftEncoder =  new Encoder(LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		rightEncoder = new Encoder(RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		leftEncoder.setDistancePerPulse(DRIVE_INCHES_PER_TICK);
		leftEncoder.setReverseDirection(true);
		rightEncoder.setReverseDirection(false);
		rightEncoder.setDistancePerPulse(DRIVE_INCHES_PER_TICK);
		
		// setup gyro
		gyro = new ADXRS450_Gyro();
	}

	public void setGear(boolean gear) {
		if(shifter.get() != gear)
			shifter.set(gear);
	}

	public void tankDrive(double left, double right) {
		autoMove(left, right);
	}

	public void cheesyDrive(double wheel, double throttle, boolean quickturn, boolean altQuickturn, boolean shift) {
		/*
		 * Poofs! :param wheel: The speed that the robot should turn in the X
		 * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
		 * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
		 * :param quickturn: If the robot should drive arcade-drive style
		 */
		throttle = Util.deadband(throttle);
		wheel = Util.deadband(wheel);
		if(driveReversed)
			wheel*=-1;
		double right_pwm;
		double left_pwm;
		double neg_inertia_scalar;
		double neg_inertia = wheel - old_wheel;
		old_wheel = wheel;
		wheel = Util.sinScale(wheel, 0.9f, 1, 0.9f);

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
		if(altQuickturn){
			if(Math.abs(throttle) < 0.2){
				double alpha = .1f;
				quickstop_accumulator = (1-alpha)*quickstop_accumulator+alpha*limit(wheel, 1.0)*5;
			}
			over_power = - wheel * .75;
			angular_power = -wheel * 1;
		}else if (quickturn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				quickstop_accumulator = (1 - alpha) * quickstop_accumulator + alpha * limit(wheel, 1.0) * 5;
			}
			over_power = 1;
			angular_power = -wheel * 1;
		} else {
			over_power = 0;
            double sensitivity = .9;
            angular_power = throttle * wheel * sensitivity - quickstop_accumulator;
			quickstop_accumulator = Util.wrap_accumulator(quickstop_accumulator);
		}
		
		
		if(shift){
			if(!shifter.get())
				shifter.set(true);
		}else{
			if(shifter.get())
				shifter.set(false);
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
		
        if(driveReversed) {
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

	private double leftRamp = 0.0;
	private double rightRamp = 0.0;
	public void moveRamped(double desiredLeft, double desiredRight) {
		double ramp_speed = 6;
		leftRamp += (desiredLeft - leftRamp) / ramp_speed;
		rightRamp += (desiredRight - rightRamp) / ramp_speed;
		leftDrive.set(leftRamp);
		rightDrive.set(rightRamp);
	}

	public void autoMove(double left, double right) {
		leftDrive.set(limit(left, 1));
		rightDrive.set(limit(right, 1));
	}

	public void autoShift(boolean b) {
		if(shifter.get() != b)
			shifter.set(b);
	}
	
	public void slowPeriodic() {
		drivePool.logDouble("gyro_angle", getRotation());
		drivePool.logDouble("left_enc", rightEncoder.getDistance());
		drivePool.logDouble("right_enc", leftEncoder.getDistance());
	}

	public void setDrivetrainReversed(boolean reversed) {
        driveReversed = reversed;
    }
	
	public boolean driveReversed() {
        return driveReversed;
    }
	
	public double getRotation() {
		return gyro.getAngle();
	}
	
	public double getLeftDistance(){
		return leftEncoder.getDistance();
	}
	
	public double getRightDistance(){
		return rightEncoder.getDistance();
	}
}