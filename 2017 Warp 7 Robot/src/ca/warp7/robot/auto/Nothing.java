package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;

public class Nothing extends AutonomousBase {

	@Override
	public void periodic() {
		/* 
		   Robit is bored so is trying to solve riddle...
		   If a tree falls in a forest and no one is there to hear it... did it make a sound?
		  
		   ...
		   
		   I guess not?
		*/
		Warp7Robot.compressor.setClosedLoopControl(false);
		doRight();
		drive.autoShift(false);
	}
	public double rpm = 4706;

	public void doRight(){
		switch(step){
		case 1:
			gearMech.hold();
			if(travel(6*12, 0.75))
				nextStep(0.2);
			break;
		case 2:
			if(relTurn(-55))
				nextStep(0.2);
			break;
		case 3:
			if(travel(3*12+44, 0.75))
				nextStep(0.2);
			break;
		case 4:
			gearMech.release();
			nextStep(0.5);
			break;
		case 5:
			if(travel(-(2*12+4), 0.75))
				nextStep(0.5);
			break;
		case 6:
			shooter.setRPM(rpm);
			if(relTurn(15))
				nextStep(0.5);
			break;
		case 7:
			shooter.setRPM(rpm);
			if(travel(-(1*12+2), 0.45))
				nextStep(0.5);
			break;
		case 8:
			if(shoot(rpm, 6))
				nextStep(0.5);
			break;	
		default:
			reset();
			break;
		}
	}
	
	
	
	
	public void doCentre(){
		switch(step){
		case 1:
			if(travel(83, 0.5))
				nextStep(0.5);
			break;
		case 2:
			gearMech.release();
			nextStep(0.5);
			break;
		case 3:
			if(travel(-2*12, 0.5))
				nextStep(0.5);
			break;
		case 4:
			gearMech.hold();
			nextStep(0.0);
			break;
		default:
			reset();
			break;
		}
	}
	
	public void doHopper1(){
		switch(step){
		case 1:
			if(travel(-88, 0.95))
				nextStep(0.25);
			break;
		case 2:
			if(relTurn(90))
				nextStep(0.15);
			break;
		case 3:
			shooter.setIntakeSpeed(1.0);
			if(travel(-38, 0.85))
				nextStep(0.0);
			break;
		case 4:
			//shooter.setIntakeSpeed(1.0);
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
			if(relTurn(68))
				nextStep(0.0);
			break;
		case 7:
			shooter.setRPM(rpm);
			if(travel(1*12+10, 0.85))
				nextStep(0.1);
			break;
		case 8:
			if(shoot(rpm, 10))
				nextStep(0.0);
			break;
		default:
			reset();
			break;
		}
	}
	
	public void doDaTurn(){
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
	
	public void doDaMove(){
		switch(step){
		case 1:
			if(travel(3*12, 0.9))
				nextStep(0.5);
			break;
		case 2:
			if(travel(-3*12, 0.9))
				nextStep(0.5);
			break;
		case 3:
			step = 1;
			break;
		default:
			reset();
			break;
		}
	}
	
	public void doStuff(){
		switch(step){
		case 1:
			if(travel(3*12, 0.9))
				nextStep(0.5);
			break;
		case 2:
			if(relTurn(90))
				nextStep(0.5);
			break;
		case 3:
			if(travel(2*12, 0.9))
				nextStep(0.5);
			break;
		case 4:
			if(relTurn(90))
				nextStep(0.5);
			break;
		case 5:
			if(travel(3*12, 0.9))
				nextStep(0.5);
			break;
		case 6:
			if(relTurn(90))
				nextStep(0.5);
			break;
		case 7:
			if(travel(2*12, 0.9))
				nextStep(0.5);
			break;
		case 8:
			if(relTurn(90))
				nextStep(0.5);
			break;
		case 9:
			step = 1;
			break;
		default:
			reset();
			break;
		}
	}
}
