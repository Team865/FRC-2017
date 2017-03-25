package ca.warp7.robot.auto;

public class ShootGearRight extends AutonomousBase{

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(relTurn(90))
				nextStep(0.5);
			break;
		case 2:
			step--;
			break;
		}
	}

}
