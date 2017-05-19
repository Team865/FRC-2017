package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class GearCentreRed extends AutonomousBase {
	
	double rpm = 4706;
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
			drive.autoMove(-0.5, -0.5);
			try{
				if(gearGoalVisible()){
					nextStep();
					System.err.println("Found Jetson");
				}
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
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
		case 2:
			try{
				if(gearMove())
					nextStep(0.2, 2);
				
				if(timePassed(4)){
					//gearMech.flippedyFlip();
					nextStep();
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
		case 3:
			drive.autoMove(-0.8, -0.8);
			if(timePassed(0.75))
				nextStep(0.2);
			break;
		case 4:
			gearMech.release();
			nextStep(0.5);
			break;
		case 5:
			if(travel(-1.5*12, 0.4))
				nextStep(0.2);
			break;
		case 6:
			if(travel(-3.5*12, 0.8))
				nextStep(0.2);
			break;
		case 7:
			shooter.setRPM(rpm);
			if(relTurn(-65))
				nextStep();
			break;
		case 8:
			shooter.setRPM(rpm);
			if(travel(-2*12, 0.85))
				nextStep(0.2);
			break;
		case 9:
			shooter.setRPM(rpm);
			try{
				if(lineUpShooter(Direction.COUNTER_CLOCKWISE))
					nextStep(0.25);
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			break;
		case 10:
			try{
				if(visionShoot(10))
					nextStep();
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
			reset();
			break;
		}
	}

}
