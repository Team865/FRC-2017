package ca.warp7.robot.auto;

import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class GyroTest extends AutonomousBase {

	@Override
	public void periodic(Drive drive, GearMech gearMech, Climber climber, Shooter shooter) {
		switch(step){
		case 1:
			absTurn(90, drive);
			//if (travel(6, drive)) step++;
			break;
		case 2:
			Timer.delay(0.5);
			step++;
			break;
		case 3:
			if (relTurn(-90, drive)) step++;
			break;
		case 4:
			Timer.delay(0.5);
			step++;
			break;
		case 5:
			if(travel(6, drive)) step++;
			break;
		case 6:
			Timer.delay(0.5);
			step++;
			break;
		case 7:
			if(relTurn(-90, drive)) step++;
			break;
		case 8:
			Timer.delay(0.5);
			step++;
			break;
		case 9:
			if(travel(6, drive)) step++;
			break;
		case 10:
			Timer.delay(0.5);
			step++;
			break;
		case 11:
			if(relTurn(-90, drive))step++;
			break;
		case 12:
			Timer.delay(0.5);
			step++;
			break;
		case 13:
			if(travel(6, drive)) step++;
			break;
		case 14:
			Timer.delay(0.5);
			step++;
			break;
		case 15:
			if(relTurn(-90, drive))step++;
			break;
		case 16:
			Timer.delay(0.5);
			step = 1;
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
