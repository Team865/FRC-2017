package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class RedGeorgian extends AutonomousBase {

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4450, 6))
				nextStep();
			break;
		case 2:
			if(travel(2*12))
				nextStep();
			break;
		case 3:
			if(relTurn(-50))
				nextStep();
			break;
		case 4:
			if(travel(-10*12))
				nextStep();
			break;
		default:
			reset();
			break;
		}
	}
}
