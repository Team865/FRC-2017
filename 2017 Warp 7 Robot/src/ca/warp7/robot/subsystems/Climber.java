package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.CLIMBER_MOTOR_PINS;

import ca.warp7.robot.misc.MotorGroup;
import edu.wpi.first.wpilibj.VictorSP;

public class Climber {

	private MotorGroup climbMotors;
	
	
	public Climber(){
		climbMotors = new MotorGroup(CLIMBER_MOTOR_PINS, VictorSP.class);
	}
	
	public void setSpeed(double speed){
		double delta = speed-climbMotors.get();
		
		// Make it increase over time
		climbMotors.set(climbMotors.get() + delta * 0.2);
	}
}
