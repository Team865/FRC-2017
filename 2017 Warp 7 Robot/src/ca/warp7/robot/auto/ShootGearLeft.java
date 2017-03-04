package ca.warp7.robot.auto;

import static ca.warp7.robot.Warp7Robot.shooter;

import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class ShootGearLeft extends AutonomousBase {

	public ShootGearLeft(Drive drive, GearMech gearMech) {
		drive.autoGear(false);
		reset();
		gearMech.hold();
	}
	
	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
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
			Timer.delay(0.5);
			step++;
			break;
		case 2:
			if(relTurn(18, drive)){
				Timer.delay(0.5);
				step++;
				reset();
			}
			break;
		case 3:
			if(travel(-3*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 4:
			if(relTurn(-81, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 5:
			if(travel(4.25*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 6:
			if(relTurn(35, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 7:
			if(travel(3*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 8:	
			try{
				if(visionMove(drive)){
					Timer.delay(2);
					step++;
				}
			}catch(NullPointerException npe){System.out.println("why");step++;}
			break;
		case 9:
			if(DataPool.getBooleanData("vision", "found")){
				step--;
				return;
			}
			drive.autoMove(0, 0);
			gearMech.release();
			Timer.delay(0.5);
			step++;
			break;
		case 10:
			if(travel(-3.75*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 11:
			//-60 would be square with driver stations
			if(relTurn(30, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 12:
			if(travel(-2*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		default:
			reset(drive, gearMech, climber, shooter);
			break;
		}
	}

	@Override
	public void reset(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
		drive.autoMove(0, 0);
		gearMech.hold();
	}

}
