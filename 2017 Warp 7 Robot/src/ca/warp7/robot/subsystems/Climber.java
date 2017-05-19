package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.CLIMBER_MOTOR_PINS;

import ca.warp7.robot.misc.MotorGroup;
import edu.wpi.first.wpilibj.VictorSP;

public class Climber {

	private MotorGroup climbMotors;
	
	
	public Climber(){
		climbMotors = new MotorGroup(CLIMBER_MOTOR_PINS, VictorSP.class);
	}
	
	private double ramp = 0.0;
	public void setSpeed(double speed){
		// Ramp to prevent brown outs
		double rampSpeed = 6;
		ramp += (speed - ramp)/rampSpeed;
		climbMotors.set(ramp);
	}
	
}
