package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.HOPPER_SPIN_PINS;

import ca.warp7.robot.MotorGroup;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {

	private MotorGroup hopperSpin;
	
	
	public Shooter(){
		hopperSpin = new MotorGroup(HOPPER_SPIN_PINS, VictorSP.class);
	}
	
	public void setHopperSpin(double speed){
		if(hopperSpin.get() != -speed)
			hopperSpin.set(-speed);
	}
}
