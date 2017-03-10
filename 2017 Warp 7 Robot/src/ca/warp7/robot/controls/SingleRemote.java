package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.climber;
import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.Warp7Robot.drive;
import static ca.warp7.robot.Warp7Robot.gearMech;
import static ca.warp7.robot.Warp7Robot.shooter;

import ca.warp7.robot.misc.DataPool;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class SingleRemote extends ControlsBase{

	private XboxController driver;
	private XboxController operator;
	private boolean rightStick = false;
	private boolean back = false;
	private boolean x = false;
	private double rpm = 4450;
	private double pov = -1;
	
	
	public SingleRemote(XboxController driver, XboxController operator) {
		this.driver = driver;
		this.operator = operator;
		
		rightStick = false;
		rpm = 4450;
		pov = -1;
		driver.setRumble(RumbleType.kLeftRumble, 0);
        driver.setRumble(RumbleType.kRightRumble, 0);
        operator.setRumble(RumbleType.kLeftRumble, 0);
        operator.setRumble(RumbleType.kRightRumble, 0);
	}

	@Override
	public void periodic() {
		if(driver.getTriggerAxis(Hand.kLeft) < 0.5){
			if(driver.getStickButton(Hand.kRight)){
				if(!rightStick){
					drive.setDrivetrainReversed(!drive.driveReversed());
					rightStick = true;
				}
			}else
				if(rightStick)
					rightStick = false;
			
			if(Math.abs(driver.getTriggerAxis(Hand.kRight)) >= 0.5){
				gearMech.release();
			}else{
				gearMech.hold();
			}
			
			if(driver.getYButton()){
				climber.setSpeed(-1.0);
			}else{
				climber.setSpeed(0.0);
			}
			
			if(driver.getBackButton()){
				if(!back){
					compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
					back = true;
				}
			}else{
				back = false;
			}
			
			if(driver.getBButton()){
				shooter.setRPM(rpm);
			}else{
				shooter.setRPM(0);
			}
			
			if(driver.getXButton()){
				if(!x){
					gearMech.flippedyFlip();
					x = true;
				}
			}else{
				x = false;
			}
			
			double hop = 0.7;
			double reverseHop = -0.6;
			double tower = 1.0;
			double slowTower = 0.4;
			double intake = 1.0;
			 if (driver.getPOV(0) == 90){
				shooter.setHopperSpeed(reverseHop);
				shooter.setIntakeSpeed(intake);
				shooter.setTowerSpeed(0.0);
			}else if(driver.getAButton()){
				shooter.setHopperSpeed(hop);
				shooter.setIntakeSpeed(intake);
				if(shooter.withinRange(40) && shooter.getSetPoint() > 0.0){
					shooter.setTowerSpeed(tower);
				}else if(shooter.getSensor()){
					shooter.setTowerSpeed(slowTower);
				}else{
					shooter.setTowerSpeed(0.0);
				}
			}else{
				shooter.setIntakeSpeed(0.0);
				shooter.setHopperSpeed(0.0);
				shooter.setTowerSpeed(0.0);
			}
			 
			if(driver.getPOV(0) == 0 && pov != 0){
				pov = 0;
				rpm += 5;
			}else if (driver.getPOV(0) == 180 && pov != 180){
				pov = 180; 
				rpm -= 5;
			}else if (driver.getPOV(0) == 270 && pov != 270){
				pov = 270;
				rpm = 4450;
			}else{
				pov = -1;
			}
			 
			//drive.tankDrive(driver.getY(Hand.kLeft), driver.getY(Hand.kRight));
			drive.cheesyDrive(-driver.getX(Hand.kRight), driver.getY(Hand.kLeft), driver.getBumper(Hand.kLeft), driver.getTriggerAxis(Hand.kLeft) >= 0.5, driver.getBumper(Hand.kRight));
		}else{
			try{
				if(DataPool.getBooleanData("vision", "found")){
					drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
				}else{
					drive.cheesyDrive(-driver.getX(Hand.kRight), driver.getY(Hand.kLeft), driver.getBumper(Hand.kLeft), driver.getTriggerAxis(Hand.kLeft) >= 0.5, driver.getBumper(Hand.kRight));
				}
			}catch(Exception e){
				System.out.println("i no work no more");
			}
		}
	}

	@Override
	public void disabled() {
		driver.setRumble(RumbleType.kLeftRumble, 0);
        driver.setRumble(RumbleType.kRightRumble, 0);
        operator.setRumble(RumbleType.kLeftRumble, 0);
        operator.setRumble(RumbleType.kRightRumble, 0);
	}

}
