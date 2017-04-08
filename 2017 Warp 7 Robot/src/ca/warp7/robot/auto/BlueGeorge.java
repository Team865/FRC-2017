package ca.warp7.robot.auto;

public class BlueGeorge extends AutonomousBase {
	//blue
	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4425, 6))
				nextStep(0.0);
			break;
		case 2:
			if(fakeCurve(-1, 0, -95))
				nextStep(0.5);
			break;
		case 3:
			if(travel(10*12, 0.7))
				nextStep(0.0);
			break;
		default:
			reset();
			break;
		}
	}
}
