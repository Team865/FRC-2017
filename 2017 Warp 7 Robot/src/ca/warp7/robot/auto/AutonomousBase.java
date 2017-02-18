package ca.warp7.robot.auto;

import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;

public abstract class AutonomousBase {

	public int step = 1;
	public static DataPool autoPool = new DataPool("auto");
	
	public abstract void periodic(Drive drive, GearMech gearMech, Climber climber, Shooter shooter);

	public abstract void reset(Drive drive, GearMech gearMech, Climber climber, Shooter shooter);
	
	/**
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	private static double errorOld = 0.0;
	public static boolean absTurn(double degrees, Drive drive) {
		degrees %= 360;
		if(degrees < 0) degrees += 360;
		double angle = drive.gyro.getAngle()%360;
		double kp = 7.0;
		double kd = 3;
		
		double error = degrees-angle;
		double speed = (kp*error/360) + ((error-errorOld)*kd/360);
		if(Math.abs(error) < 3)speed = 0;
		
		
		speed = Math.max(-1, Math.min(1, speed));
		drive.autoMove(speed, -speed);
		errorOld = error;
		return Math.abs(error) < 3;
	}
	
	/**
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	private static boolean reset = true;
	private static double offset = 0.0;
	public static boolean relTurn(double degrees, Drive drive) {
		if(reset)offset = drive.gyro.getAngle();
		
		double angle = drive.gyro.getAngle()-offset;
		double kp = 7.0;
		double kd = 3.0;
		
		double error = degrees-angle;
		double speed = (kp*error/360) + ((error-errorOld)*kd/360);
		if(Math.abs(error) < 3)speed = 0;
		
		
		speed = Math.max(-1, Math.min(1, speed));
		drive.autoMove(speed, -speed);
		errorOld = error;
		
		if (Math.abs(error) < 3){
			reset = true;
			return true;
		}else{
			reset = false;
			return false;
		}
	}
	
	private static double distance = 0.0;
	private static double lStart = 0.0;
	private static double rStart = 0.0;
	private static double oldErrorL = 0.0;
	private static double oldErrorR = 0.0;
	public static boolean travel(double toTravel, Drive drive){
		if(reset){
			lStart = drive.leftEncoder.getDistance();
			rStart = drive.rightEncoder.getDistance();
			distance = toTravel;
		}
		
		double kp = 0.1;
		double kd = 0.0;
		
		double errorL = (distance + lStart) - drive.leftEncoder.getDistance();
		double errorR = (distance + rStart) - drive.rightEncoder.getDistance();
		autoPool.logDouble("errorL", errorR);
		autoPool.logDouble("errorR", errorL);
		double speedL = (kp*errorL) + ((errorL-oldErrorL)*kd);
		double speedR = (kp*errorR) + ((errorR-oldErrorR)*kd);
		if(Math.abs(errorL) < 0.25)speedL = 0;
		if(Math.abs(errorR) < 0.25)speedR = 0;
		
		speedL = Math.max(-1,  Math.min(1, speedL));
		speedR = Math.max(-1, Math.min(1, speedR));
		drive.autoMove(-speedL, -speedR);
		oldErrorL = errorL;
		oldErrorR = errorR;
		
		if(Math.abs(errorL) < 0.25 && Math.abs(errorR) < 0.25){
			reset = true;
			return true;
		}else{
			reset = false;
			return false;
		}
	}
}
