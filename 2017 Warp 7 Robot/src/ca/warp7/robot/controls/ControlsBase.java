package ca.warp7.robot.controls;

import static ca.warp7.robot.Constants.DRIVER_ID;
import static ca.warp7.robot.Constants.OPERATOR_ID;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;

public abstract class ControlsBase {

	public static DataPool controlPool = new DataPool("controls");
	
	protected XboxControllerPlus driver;
	protected XboxControllerPlus operator;
	protected Climber climber;
	protected Drive drive;
	protected GearMech gearMech;
	protected Shooter shooter;
	
	
	public ControlsBase(){
		driver = new XboxControllerPlus(DRIVER_ID);
		operator = new XboxControllerPlus(OPERATOR_ID);
		
		climber = Warp7Robot.climber;
		drive = Warp7Robot.drive;
		gearMech = Warp7Robot.gearMech;
		shooter = Warp7Robot.shooter;
	}
	
	abstract public void periodic();
    
}