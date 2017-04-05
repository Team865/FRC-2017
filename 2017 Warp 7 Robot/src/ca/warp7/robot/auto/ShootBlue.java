package ca.warp7.robot.auto;

public class ShootBlue extends AutonomousBase {

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4450, 6))
				nextStep(0.0);
			break;
		case 2:
			if(travel(4*12, 0.9))
				nextStep(0.0);
			break;
		case 3:
			//if(relTurn(-75, 10, 10))
				nextStep(0.0);
			break;
		case 4:
			if(travel(10*12, 0.9))
				nextStep(0.0);
			break;
		default:
			reset();
			break;
		}
	}

}
