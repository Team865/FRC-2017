package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class GearRight extends AutonomousBase{

	double rpm = 4706;
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
			if(relTurn(-45, 0.7))
				nextStep(0.2);
			break;
		case 3:
			drive.autoMove(-0.5, -0.5);
			try{
				if(gearGoalVisible())
					nextStep(0.0);
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			if(timePassed(5)){
				nextStep(0.0);
				System.err.println("no target");
				System.err.println("no target");
				System.err.println("no target");
				System.err.println("no target");
				System.err.println("no target");
				endAuto();
			}
			gearMech.hold();
			break;
		case 4:
			try{
				if(gearMove()){
					nextStep(0.2);
					step++;
				}
				
				if(timePassed(4)){
					gearMech.flippedyFlip();
					nextStep(0.0);
				}
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			break;
		case 5:
			drive.autoMove(-0.4, -0.4);
			if(timePassed(0.75))
				nextStep(0.2);
			break;
		case 6:
			gearMech.release();
			nextStep(0.5);
			break;
		case 7:
			if(travel(-(2*12), 0.75))
				nextStep(0.5);
			break;
		case 8:
			shooter.setRPM(rpm);
			if(relTurn(15, 0.7)){
				nextStep(0.5);
				step++;
			}
			break;
		case 9:
			shooter.setRPM(rpm);
			if(travel(-(1*12+2), 0.75))
				nextStep(0.5);
			break;
		case 10:
			shooter.setRPM(rpm);
			try{
				if(lineUpShooter(Direction.COUNTER_CLOCKWISE))
					nextStep(0.5);
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			break;
		case 11:
			try{
				if(autoShoot(6))
					nextStep(0.5);
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			break;
		default:
			drive.autoMove(0, 0);
			reset();
			break;
		}
	}

}
