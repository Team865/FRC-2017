package ca.warp7.robot.auto.test;

import static ca.warp7.robot.Warp7Robot.jetsonCommand;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.auto.AutonomousBase;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class VISION extends AutonomousBase {

	private int step = 1;
	
	
	public VISION(){
		step = 1;
	}
	
	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
		switch(step){
		case 1:
			jetsonCommand = "forward";
			Warp7Robot.compressor.setClosedLoopControl(false);
			step++;
			break;
		case 2:
			drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
			if(!DataPool.getBooleanData("vision", "found")) step++;
			break;
		case 3:
			jetsonCommand = "none";
			drive.autoMove(0, 0);
			gearMech.release();
			Timer.delay(0.75);
			step++;
			break;
		case 4:
			drive.autoMove(0.25, 0.25);
			Timer.delay(2);
			step++;
			break;
		case 5:
			drive.autoMove(0,  0);
			gearMech.hold();
			step++;
			break;
		case 6:
			if(absTurn(90, drive)) step++;
			break;
		default:
			drive.autoMove(0, 0);
			break;
		}
	}

	@Override
	public void reset(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
		drive.autoMove(0, 0);
	}

}
