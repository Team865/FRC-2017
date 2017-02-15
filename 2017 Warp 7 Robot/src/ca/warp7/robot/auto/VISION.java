package ca.warp7.robot.auto;

import static ca.warp7.robot.Warp7Robot.jetsonCommand;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;

public class VISION extends AutonomousBase {

	private int step = 1;
	
	
	public VISION(){
		step = 1;
	}
	
	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber) {
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
			System.out.println("go go go");
			try {Thread.sleep(3000);}catch (InterruptedException e) {}
			step++;
			break;
		case 4:
			drive.autoMove(0.25, 0.25);
			try {Thread.sleep(2000);}catch (InterruptedException e) {}
			step++;
			break;
		case 5:
			drive.autoMove(0,  0);
			step++;
			break;
		case 6:
			if(relativeTurn(0, drive)) step++;
		}
	}

	@Override
	public void reset(Drive drive, GearMech gearMech, Climber climber) {
		drive.autoMove(0, 0);
	}

}
