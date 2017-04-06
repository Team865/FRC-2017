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
	}
	
	public abstract void periodic();

	public void reset(){
		drive.autoMove(0, 0);
		stopShooter();
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
	protected boolean absTurn(double degrees, double limit) {
		degrees %= 360;
		double error = degrees - drive.getRotation()%360;
		
		return relTurn(error, limit);
	}
	
	private boolean resetT = true;
	private double offset = 0.0;
	private double errorSum = 0.0;
	private int counterR = 0;
	private int done = 0;
	/**
	 * negative values turn counter clockwise, positive clockwise
	 * 
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	protected boolean relTurn(double degrees, double limit) {
		limit = Math.abs(limit);
		if(resetT){
			errorSum = 0.0;
			offset = drive.getRotation();
			counterR = 0;
			done = 0;
		}

		//degrees -= 10;
		//8,16,0
		
		double angle = drive.getRotation()-offset;
		double kp = 8.0;
		double kd = 20.0;
		double ki = 0.00075;
		
		double error = degrees-angle;
		double speed = (kp*error/180) + ((error-errorOld)*kd/180) + (errorSum*ki/180);
		if(Math.abs(error) < 1)speed = 0;
		
		autoPool.logDouble("gyro error", error);
		autoPool.logBoolean("Turn in Tolerance", Math.abs(error) < 3);
				
		speed = Math.max(-limit, Math.min(limit, speed));
		drive.autoMove(speed, -speed);
		errorOld = error;
		
		errorSum += error;
		
		if(counterR >= 1000)
			errorSum = 0.0;
		
		counterR++;

		if (Math.abs(error) < 3){
			if(Math.abs(error) < 2)
				drive.autoMove(0, 0);
			if(Math.abs(degrees-(drive.getRotation()-offset)) < 3)
				done++;
			else
				done = 0;
			
			if(done >= 20){
				done = 0;
				resetT = true;
				return true;
			}
			
			resetT = false;
			return false;
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
	private int doneT = 0;
	/**
	 * call recursively
	 * 
	 * @param toTravel in inches
	 * @return if it is done
	 */
	protected boolean travel(double toTravel, double maxSpeed){
		maxSpeed = Math.abs(maxSpeed);
		if(resetD){
			lStart = drive.getLeftDistance();
			rStart = drive.getRightDistance();
			distance = toTravel;
			sumL = 0;
			sumR = 0;
			doneT = 0;
		}
		
		/*
		double kp = 2;
		double kd = 0.1;
		double ki = 0.0001;
		*/
		
		double kp = 64;
		double leftPAdd = 0.0;
		double kd = 0.0;
		double ki = 0.0;
		
		double errorL = toTravel - (drive.getLeftDistance()-lStart);
		double errorR = toTravel - (drive.getRightDistance()-rStart);
		sumL += errorL;
		sumR += errorR;
		autoPool.logDouble("errorL", errorR);
		autoPool.logDouble("errorR", errorL);
		double speedL = (kp*errorL)/Math.abs(lStart+distance) + ((errorL-oldErrorL)*kd/Math.abs(lStart+distance))+ki*sumL;
		double speedR = ((leftPAdd + kp)*errorR)/Math.abs(rStart+distance) + ((errorR-oldErrorR)*kd/Math.abs(rStart+distance))+ki*sumR;
		if(Math.abs(errorL) < 0.3)speedL = 0;
		if(Math.abs(errorR) < 0.3)speedR = 0;
		
		autoPool.logBoolean("L Encoder in Tolerance", Math.abs(errorR) < 0.3);
		autoPool.logBoolean("R Encoder in Tolerance", Math.abs(errorL) < 0.3);
		
		speedL = Math.max(-maxSpeed,  Math.min(maxSpeed, speedL));
		speedR = Math.max(-maxSpeed, Math.min(maxSpeed, speedR));
		
		drive.autoMove(-speedL, -speedR);
		oldErrorL = errorL;
		oldErrorR = errorR;
		
		if(counter >= 1000){
			sumL = 0.0;
			sumR = 0.0;
		}
		
		if(Math.abs(errorL) < 0.3 && Math.abs(errorR) < 0.3){	
			if(Math.abs(toTravel - (drive.getLeftDistance()-lStart)) < 0.3 && Math.abs(toTravel - (drive.getRightDistance()-rStart)) < 0.3)
				doneT++;
			else
				doneT = 0;
			
			if(doneT >= 20){
				doneT = 0;
				resetD = true;
				return true;
			}
			resetD = false;
			return false;
		}else{
			resetD = false;
			return false;
		}
	}
	
	private int sCounter = 0;
	protected boolean lineUpShooter(Direction dir) throws NullPointerException{
		if(visionTurn(dir)){
			sCounter++;
			if(sCounter >= 20){
				sCounter = 0;
				return true;
			}
		}else{
			sCounter = 0;
		}
		
		return false;
	}
	
	private boolean temp = true;
	protected boolean visionTurn(Direction dir) throws NullPointerException{
		if(DataPool.getBooleanData("vision", "S_found"))
			if(temp){
				drive.autoMove(DataPool.getDoubleData("vision", "S_left"), DataPool.getDoubleData("vision", "S_right"));
				Timer.delay(0.01);
				temp = false;
			}else{
				drive.autoMove(0, 0);
				Timer.delay(0.01);
				temp = true;
			}
				
		else
			if(dir == Direction.CLOCKWISE)
				drive.autoMove(-0.5, 0.5);
			else
				drive.autoMove(0.5, -0.5);
		
		return Math.abs(DataPool.getDoubleData("vision", "S_right")) < 0.23;
	}
	
	protected boolean visionMove() throws NullPointerException{
		drive.autoMove(DataPool.getDoubleData("vision", "D_left"), DataPool.getDoubleData("vision", "D_right"));
		return !DataPool.getBooleanData("vision", "D_found");
	}
	
	protected boolean gearMove() throws NullPointerException{
		if(visionMove()){
			drive.autoMove(-0.3, -0.3);
			Timer.delay(0.8);
			if(!DataPool.getBooleanData("vision", "D_found") || Math.abs(DataPool.getDoubleData("vision", "D_right")) < 0.15){ // if we don't see the target... finish
				drive.autoMove(0.0, 0.0);
				return true;
			}
		}
		return false;
	}
	
	protected void shoot(double shooterRPM){
		shooter.setRPM(shooterRPM);
		shooter.setHopperSpeed(1.0);
		shooter.setIntakeSpeed(1.0);
		if(shooter.withinRange(30) && shooter.getSetPoint() > 0.0){
			shooter.setTowerSpeed(1.0);
		}else if(shooter.getSensor()){
			shooter.setTowerSpeed(0.0);
		}else{
			shooter.setTowerSpeed(0.0);
		}
	}
	
	protected boolean shoot(double shooterRPM, double seconds){
		shoot(shooterRPM);
		if(timePassed(seconds)){
			stopShooter();
			return true;
		}
		return false;
	}
	
	private double rpm = 0.0;
	protected boolean autoShoot(double seconds) throws NullPointerException{
		if(rpm <= 0.0){
			double pixelHeight = DataPool.getDoubleData("vision", "S_dist");
			rpm = 0.018*Math.pow(pixelHeight, 2)-19.579*pixelHeight+9675.03;
		}
		
		if(shoot(rpm, seconds)){
			rpm = 0.0;
			stopShooter();
			return true;
		}else{
			return false;
		}
	}
	
	protected void stopShooter(){
		shooter.setHopperSpeed(0.0);
		shooter.setIntakeSpeed(0.0);
		shooter.setRPM(0.0);
		shooter.setTowerSpeed(0.0);
	}
	
	private double timer = -1;
	protected boolean timePassed(double seconds) {
		if(timer <= 0)
			timer = Timer.getFPGATimestamp();
		
		if(Timer.getFPGATimestamp() - timer >= seconds){
			timer = -1;
			return true;
		}else{
			return false;
		}
	}
	
	protected void nextStep(double delaySeconds){
		step++;
		resetValues();
		drive.autoMove(0, 0);
		Timer.delay(delaySeconds);
	}
	
	private void resetValues(){
		resetD = true;
		resetT = true;
		timer = -1;
		sumL = 0.0;
		sumR = 0.0;
		counter = 0;
		counterR = 0;
		done = 0;
		doneT = 0;
		errorSum = 0.0;
		lStart = 0.0;
		rStart = 0.0;
		oldErrorL = 0.0;
		oldErrorR = 0.0;
		offset = 0.0;
		temp = true;
		errorOld = 0.0;
		sCounter = 0;
		rpm = 0.0;
	}
	
	protected enum Direction{
		CLOCKWISE, COUNTER_CLOCKWISE;
	}
}
