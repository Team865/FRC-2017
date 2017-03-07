package ca.warp7.robot.auto;

import ca.warp7.robot.Warp7Robot;
import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public abstract class AutonomousBase {

	public int step;
	public static DataPool autoPool = new DataPool("auto");
	protected Climber climber;
	protected Drive drive;
	protected GearMech gearMech;
	protected Shooter shooter;
	
	
	public AutonomousBase(){
		climber = Warp7Robot.climber;
		drive = Warp7Robot.drive;
		gearMech = Warp7Robot.gearMech;
		shooter = Warp7Robot.shooter;
		step = 1;
		reset();
		resetValues();
	}
	
	public abstract void periodic();

	public void reset(){
		drive.autoMove(0, 0);
		gearMech.hold();
		drive.autoShift(false);
		resetValues();
	}
	
	/**
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	private double errorOld = 0.0;
	protected boolean absTurn(double degrees) {
		degrees %= 360;
		if(degrees < 0) degrees += 360;
		double angle = drive.getRotation()%360;
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
	private boolean resetT = true;
	private double offset = 0.0;
	protected boolean relTurn(double degrees) {
		if(resetT)offset = drive.getRotation();
		
		double angle = drive.getRotation()-offset;
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
	
	private boolean resetD = true;
	private double distance = 0.0;
	private double sumL = 0.0;
	private double sumR = 0.0;
	private int counter = 0;
	private double lStart = 0.0;
	private double rStart = 0.0;
	private double oldErrorL = 0.0;
	private double oldErrorR = 0.0;
	protected boolean travel(double toTravel){
		if(resetD){
			lStart = drive.getLeftDistance();
			rStart = drive.getRightDistance();
			distance = toTravel;
		}
		
		double kp = 2;
		double kd = 0.1;
		double ki = 0.0001;
		
		double errorL = toTravel - (drive.getLeftDistance()-lStart);
		double errorR = toTravel - (drive.getRightDistance()-rStart);
		sumL += errorL;
		sumR += errorR;
		autoPool.logDouble("errorL", errorR);
		autoPool.logDouble("errorR", errorL);
		double speedL = (kp*errorL)/Math.abs(lStart+distance) + ((errorL-oldErrorL)*kd/Math.abs(lStart+distance))+ki*sumL;
		double speedR = (kp*errorR)/Math.abs(rStart+distance) + ((errorR-oldErrorR)*kd/Math.abs(rStart+distance))+ki*sumR;
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
	
	protected boolean visionMove() throws NullPointerException{
		drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
		return !DataPool.getBooleanData("vision", "found");
	}
	
	protected void nextStep(double delaySeconds){
		step++;
		resetValues();
		Timer.delay(delaySeconds);
	}
	
	private void resetValues(){
		resetD = true;
		resetT = true;
		sumL = 0.0;
		sumR = 0.0;
		counter = 0;
		lStart = 0.0;
		rStart = 0.0;
		oldErrorL = 0.0;
		oldErrorR = 0.0;
		offset = 0.0;
		errorOld = 0.0;
	}
}
