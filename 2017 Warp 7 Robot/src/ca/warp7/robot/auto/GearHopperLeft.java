package ca.warp7.robot.auto;

public class GearHopperLeft extends AutonomousBase{

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(travel(6*12))
				nextStep(0.5);
			break;
		case 2:
			if(relTurn(-60))
				nextStep(0.5);
			break;
		case 3:
			if(travel(2*12))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}
	}

}
