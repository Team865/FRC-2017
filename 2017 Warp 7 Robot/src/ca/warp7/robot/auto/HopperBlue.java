package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class HopperBlue extends AutonomousBase {

	private double rpm = 4706;
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
			if(travel(-90, 0.95))
				nextStep(0.25);
			break;
		case 2:
			if(relTurn(-90, 0.7))
				nextStep(0.15);
			break;
		case 3:
			shooter.setIntakeSpeed(1.0);
			if(travel(-38, 0.85))
				nextStep(0.0);
			break;
		case 4:
			drive.autoMove(0.5, 0.5);
			if(timePassed(2.5)){
				shooter.setIntakeSpeed(0.0);
				nextStep(0.0);
			}
			break;
		case 5:
			shooter.setRPM(rpm);
			if(travel(12, 0.9))
				nextStep(0.1);
			break;
		case 6:
			shooter.setRPM(rpm);
			if(relTurn(-68, 0.7))
				nextStep(0.0);
			break;
		case 7:
			shooter.setRPM(rpm);
			if(lineUpShooter(Direction.CLOCKWISE))
				nextStep(0.15);
			break;
		case 8:
			if(autoShoot(6))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}
	}

}
