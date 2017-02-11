package ca.warp7.robot.subsystems;

import ca.warp7.robot.MotorGroup;
import edu.wpi.first.wpilibj.VictorSP;

import static ca.warp7.robot.Constants.CLIMBER_MOTOR_PINS;

public class Climber {

	public MotorGroup climbMotors;
	
	
	public Climber(){
		climbMotors = new MotorGroup(CLIMBER_MOTOR_PINS, VictorSP.class);
	}
	
	public void requestSpeed(double speed){
		double delta = speed-climbMotors.get();
		
		climbMotors.set(climbMotors.get() + delta * 0.2);
	}
}
