package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class GearLeftRed extends AutonomousBase{

	double rpm = 4706;
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
			gearMech.hold();
			if(travel(6*12+1, 0.75))
				nextStep(0.2);
			break;
		case 2:
			if(relTurn(55, 0.7))
				nextStep(0.2);
			break;
		case 3:
			if(travel(1*12, 0.75))
				nextStep(0.1);
			break;
		case 4:
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
		case 5:
			try{
				if(gearMove()){
					nextStep(0.2);
					step++;
					//endAuto();
				}
				
				if(timePassed(4)){
					//gearMech.flippedyFlip();
					nextStep(0.0);
					//endAuto();
					
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
		case 6:
			drive.autoMove(-0.4, -0.4);
			if(timePassed(0.75))
				nextStep(0.2);
			break;
		case 7:
			gearMech.release();
			nextStep(0.5);
			break;
		case 8:
			if(travel(-(2*12), 0.4))
				nextStep(0.5);
			break;
		default:
			drive.autoMove(0, 0);
			reset();
			break;
		}
	}

}
