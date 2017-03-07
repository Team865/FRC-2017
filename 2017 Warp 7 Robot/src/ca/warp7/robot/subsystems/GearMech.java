package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.GEAR_PISTON_PORT;
import static ca.warp7.robot.Constants.FLIP_PORT;

import edu.wpi.first.wpilibj.Solenoid;

public class GearMech {

	private Solenoid holdPiston;
	private Solenoid flipPiston;
	
	
	public GearMech(){
		holdPiston = new Solenoid(GEAR_PISTON_PORT);
		flipPiston = new Solenoid(FLIP_PORT);
	}
	
	public void hold(){
		if(isHolding())
			holdPiston.set(false);
	}
	
	public void release(){
		if(!isHolding())
			holdPiston.set(true);
	}
	
	public boolean isHolding(){
		return holdPiston.get();
	}
	
	public void flip(boolean b){
		if(flipPiston.get() != b)
			flipPiston.set(b);
	}
	
	public void flippedyFlip(){
		flipPiston.set(!flipPiston.get());
	}
}
