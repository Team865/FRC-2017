package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class GearLeft extends AutonomousBase{

	private double rpm = 4706;
	
	
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
			gearMech.hold();
			if(travel(6*12+6, 0.75))
				nextStep(0.2);
			break;
		case 2:
			if(relTurn(45, 0.7))
				nextStep(0.2);
			break;
		case 3:
			if(gearMove()){
				nextStep(0.2);
				step++;
			}
			
			if(timePassed(4)){
				gearMech.flippedyFlip();
				nextStep(0.0);
			}
			break;
		case 4:
			drive.autoMove(-0.4, -0.4);
			if(timePassed(0.75))
				nextStep(0.2);
			break;
		case 5:
			gearMech.release();
			nextStep(0.5);
			break;
		case 6:
			if(travel(-(2*12), 0.75))
				nextStep(0.5);
			break;
		case 7:
			//shooter.setRPM(rpm);
			if(relTurn(-15, 0.7)){
				nextStep(0.5);
			}
			break;
		case 8:
			//shooter.setRPM(rpm);
			if(travel(-(1*12+2), 0.75)){
				nextStep(0.5);
				//used to disable shooting
				step+=20;
				System.out.println(step);
			}
			break;
		case 9:
			shooter.setRPM(rpm);
			if(lineUpShooter(Direction.CLOCKWISE))
				nextStep(0.5);
			break;
		case 10:
			if(autoShoot(6))
				nextStep(0.5);
			break;	
		default:
			reset();
			break;
		}
	}


}
