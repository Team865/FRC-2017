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
	 * negative values move to the right, positive to the left
	 * 
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	private static boolean resetT = true;
	private static double offset = 0.0;
	public static boolean relTurn(double degrees, Drive drive) {
		if(resetT)offset = drive.gyro.getAngle();
		
		double angle = drive.gyro.getAngle()-offset;
		double kp = 8.0;
		double kd = 3.0;
		
		double error = degrees-angle;
		double speed = (kp*error/360) + ((error-errorOld)*kd/360);
		if(Math.abs(error) < 3)speed = 0;
		
		
		speed = Math.max(-1, Math.min(1, speed));
		drive.autoMove(speed, -speed);
		errorOld = error;
		
		if (Math.abs(error) < 3){
			resetT = true;
			return true;
		}else{
			resetT = false;
			return false;
		}
	}
	
	private static boolean resetD = true;
	private static double distance = 0.0;
	private static double sumL = 0.0;
	private static double sumR = 0.0;
	private static int counter = 0;
	private static double lStart = 0.0;
	private static double rStart = 0.0;
	private static double oldErrorL = 0.0;
	private static double oldErrorR = 0.0;
	public static boolean travel(double toTravel, Drive drive){
		if(resetD){
			lStart = drive.leftEncoder.getDistance();
			rStart = drive.rightEncoder.getDistance();
			distance = toTravel;
		}
		
		double kp = 2;
		double kd = 0.1;
		double ki = 0.0001;
		
		double errorL = toTravel - (drive.leftEncoder.getDistance()-lStart);
		double errorR = toTravel - (drive.rightEncoder.getDistance()-rStart);
		sumL += errorL;
		sumR += errorR;
		autoPool.logDouble("errorL", errorR);
		autoPool.logDouble("errorR", errorL);
		double speedL = (kp*errorL)/Math.abs(lStart+distance) + ((errorL-oldErrorL)*kd/Math.abs(lStart+distance))+ki*sumL;
		double speedR = (kp*errorR)/Math.abs(rStart+distance) + ((errorR-oldErrorR)*kd/Math.abs(rStart+distance))+ki*sumR;
		//double speedL = (kp*errorL) + ((errorL-oldErrorL)*kd);
		//double speedR = (kp*errorR)/(rStart+distance) + ((errorR-oldErrorR)*kd/(rStart+distance));
		if(Math.abs(errorL) < 0.25)speedL = 0;
		if(Math.abs(errorR) < 0.25)speedR = 0;
		
		speedL = Math.max(-1,  Math.min(1, speedL));
		speedR = Math.max(-1, Math.min(1, speedR));
		//vvvvv remove this after fixing the encoders
		speedR = speedL;
		drive.autoMove(-speedL, -speedR);
		oldErrorL = errorL;
		oldErrorR = errorR;
		
		if(counter >= 1000){
			sumL = 0.0;
			sumR = 0.0;
		}
		
		if(Math.abs(errorL) < 0.25){
		// use this after the encoder is fixed if(Math.abs(errorL) < 1 && Math.abs(errorR) < 1){
			resetD = true;
			return true;
		}else{
			resetD = false;
			return false;
		}
	}
	
	public static boolean visionMove(Drive drive) throws NullPointerException{
		drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
		return !DataPool.getBooleanData("vision", "found");
	}
	
	public static void reset(){
		resetD = true;
		resetT = true;
		sumL = 0.0;
		sumR = 0.0;
		counter = 0;
		lStart = 0.0;
		rStart = 0.0;
		oldErrorL = 0.0;
		oldErrorR = 0.0;
	}
}
