package ca.warp7.robot.auto;

public class BlueGeorgian extends AutonomousBase {
	//blue
	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4450, 6))
				nextStep();
			break;
		case 2:
			if(travel(4*12))
				nextStep();
			break;
		case 3:
			if(relTurn(67))
				nextStep();
			break;		
		case 4:
			if(travel(10*12))
				nextStep();
			break;
		default:
			reset();
			break;
		}
	}
}
