package ca.warp7.robot.auto;

import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class ShootGearLeft extends AutonomousBase {

	public ShootGearLeft(Drive drive) {
		drive.autoGear(false);
	}
	
	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
		switch(step){
		case 1:
			if(relTurn(-70, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 2:
			if(travel(6*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 3:
			if(relTurn(30, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 4:
			try{
				if(visionMove(drive)){
					Timer.delay(0.5);
					step++;
				}
			}catch(Exception e){step++;}
			break;
		case 5:
			drive.autoMove(0, 0);
			gearMech.release();
			Timer.delay(0.5);
			step++;
			break;
		case 6:
			if(travel(3*12, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 7:
			if(relTurn(-30, drive)){
				Timer.delay(0.5);
				step++;
			}
			break;
		case 8:
			if(travel(1*12, drive)){
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
	}

}
