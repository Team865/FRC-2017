package ca.warp7.robot.auto;

public class BaseLine extends AutonomousBase{
	
	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(travel(10 * 12))
				step++;
			break;
		default:
			reset();
			break;
		}
	}
}
