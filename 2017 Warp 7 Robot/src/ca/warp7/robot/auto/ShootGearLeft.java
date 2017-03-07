package ca.warp7.robot.auto;

import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.Timer;

public class ShootGearLeft extends AutonomousBase {
	
	@Override
	public void periodic() {
		switch(step){
		case 1:
			shooter.spinUp(5500);
			Timer.delay(1.5);
			shooter.setHopperSpin(0.8);
			shooter.setTowerSpin(0.6);
			Timer.delay(5);
			shooter.spinUp(0);
			shooter.setHopperSpin(0.0);
			shooter.setTowerSpin(0.0);
			nextStep(0.5);
			break;
		case 2:
			if(relTurn(18))
				nextStep(0.5);
			break;
		case 3:
			if(travel(-3*12))
				nextStep(0.5);
			break;
		case 4:
			if(relTurn(-81))
				nextStep(0.5);
			break;
		case 5:
			if(travel(4.25*12))
				nextStep(0.5);
			break;
		case 6:
			if(relTurn(35))
				nextStep(0.5);
			break;
		case 7:
			if(travel(3*12))
				nextStep(0.5);
			break;
		case 8:	
			try{
				if(visionMove()){
					Timer.delay(2);
					nextStep(0.0);
				}
			}catch(NullPointerException npe){
				System.err.println("RIP no jetson");
				step++;
			}
			break;
		case 9:
			try{
				if(DataPool.getBooleanData("vision", "found")){
					step--;
					return;
				}
			}catch(NullPointerException npe){}
			drive.autoMove(0, 0);
			gearMech.release();
			nextStep(0.5);
			break;
		case 10:
			if(travel(-3.75*12))
				nextStep(0.5);
			break;
		case 11:
			//-60 would be square with driver stations
			if(relTurn(30))
				nextStep(0.5);
			break;
		case 12:
			if(travel(-2*12))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}
	}

}
