package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class CentreGear extends AutonomousBase {
	
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
			if(gearMove()){
				nextStep(0.2);
				step++;
			}
			
			if(timePassed(4)){
				gearMech.flippedyFlip();
				nextStep(0.0);
			}
			break;
		case 2:
			drive.autoMove(-0.4, -0.4);
			if(timePassed(0.75))
				nextStep(0.2);
			break;
		case 3:
			gearMech.release();
			nextStep(0.5);
			break;
		case 4:
			if(travel(-(2*12), 0.75))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
			
		}
	}
}
