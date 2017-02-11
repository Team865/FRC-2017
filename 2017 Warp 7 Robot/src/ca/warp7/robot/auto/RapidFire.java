package ca.warp7.robot.auto;

import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;

public class RapidFire extends AutonomousBase {

	private int step = 1;
	
	
	public RapidFire(){
		step = 1;
	}
	
	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber) {
		switch(step){
		case 1:
			//shooter.spinUp(3500);
			break;
		}
	}

	@Override
	public void reset(Drive drive, GearMech gearMech, Climber climber) {
		//shooter.spinUp(0);
	}

}
