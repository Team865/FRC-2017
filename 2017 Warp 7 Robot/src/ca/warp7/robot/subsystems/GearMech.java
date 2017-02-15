package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.GEAR_PISTON_PORT;

import edu.wpi.first.wpilibj.Solenoid;

public class GearMech {

	public Solenoid pistons;
	
	
	public GearMech(){
		pistons = new Solenoid(GEAR_PISTON_PORT);
		pistons.set(false);
	}
	
	public void hold(){
		if(isHolding())
			pistons.set(false);
	}
	
	public void release(){
		if(!isHolding())
			pistons.set(true);
	}
	
	public boolean isHolding(){
		return pistons.get();
	}
}
