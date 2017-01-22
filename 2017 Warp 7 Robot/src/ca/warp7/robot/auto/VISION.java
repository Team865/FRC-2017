package ca.warp7.robot.auto;

import static ca.warp7.robot.Warp7Robot.piCommand;

import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Shooter;

public class VISION extends AutonomousBase {

	private int step = 1;
	
	
	public VISION(){
		step = 1;
	}
	
	@Override
	public void periodic(Drive drive, Shooter shooter) {
		switch(step){
		case 1:
			piCommand = "forward";
			drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
			break;
		}
	}

	@Override
	public void reset(Drive drive, Shooter shooter) {
		drive.autoMove(0, 0);
	}

}
