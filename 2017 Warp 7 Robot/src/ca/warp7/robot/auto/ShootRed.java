package ca.warp7.robot.auto;

public class ShootRed extends AutonomousBase{

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4450, 6))
				nextStep(0.0);
			break;
		case 2:
			if(travel(4*12))
				nextStep(0.0);
			break;
		case 3:
			if(relTurn(-90, 10, 15))
				nextStep(0.0);
			break;
		case 4:
			if(travel(-10*12))
				nextStep(0.0);
			break;
		default:
			reset();
			break;
		}
	}

}
