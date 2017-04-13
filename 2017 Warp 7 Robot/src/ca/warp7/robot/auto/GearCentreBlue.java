package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;
import edu.wpi.first.wpilibj.Timer;

public class GearCentreBlue extends AutonomousBase {

	public GearCentreBlue(){
		super();
		
		found = false;
	}
	
	private boolean found = false;
	
	@Override
	public void periodic() {
		Warp7Robot.compressor.setClosedLoopControl(false);
		drive.autoShift(false);
		
		switch(step){
		case 1:
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
		case 2:
			try{
				if(timePassed(4)){
					if(!gearMove()){
						gearMech.flippedyFlip();
						drive.autoMove(-1, -1);
						Timer.delay(0.75);
						drive.autoMove(0, 0);
					}
					nextStep(0.5);
				}else{
					if(!gearMove()){
						found = true;
					}
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
			if(found)
				gearMech.release();
			nextStep(0.2);
			break;
		case 4:
			if(travel(-1*12, 0.35))
				nextStep(0.2);
			break;
		case 5:
			if(travel(-4*12, 0.8))
				nextStep(0.2);
			break;
		case 6:
			shooter.setRPM(5000);
			if(relTurn(65, 0.6))
				nextStep(0.0);
			break;
		case 7:
			shooter.setRPM(5000);
			if(travel(-2*12, 0.85))
				nextStep(0.2);
			break;
		case 8:
			try{
				shooter.setRPM(5000);
				if(lineUpShooter(Direction.CLOCKWISE))
					nextStep(0.0);
			}catch(Exception e){
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				System.err.println("NO JETSON!!!!!!");
				endAuto();
			}
			break;
		case 9:
			try{
				if(autoShoot(10))
					nextStep(0.0);
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
