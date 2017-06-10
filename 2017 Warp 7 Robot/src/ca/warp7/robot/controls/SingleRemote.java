package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.*;

import ca.warp7.robot.misc.DataPool;

public class SingleRemote extends ControlsBase{
	
	private double rpm = 4450;
	
	
	public SingleRemote() {
		super();
		
		rpm = 4450;
	}

	@Override
	public void periodic() {
		if(driver.getTrigger(kLeft) == UP || true){ // are we doing gear auto stuff
			
			if(driver.getStickButton(kRight) == PRESSED)
				drive.setDrivetrainReversed(!drive.driveReversed());
			
			if(driver.getTrigger(kRight) == DOWN){
				gearMech.release();
				if(timePassed(0.125))
					gearMech.flip(true);
			}else if(driver.getTrigger(kRight) == UP){
				gearMech.hold();
				gearMech.flip(false);
				timer = -1;
			}
			
			if(driver.getYButton() == DOWN)
				climber.setSpeed(-1.0);
			else if(driver.getXButton() == DOWN)
				climber.setSpeed(-0.4);
			else
				climber.setSpeed(0.0);
			
			if(driver.getBackButton() == PRESSED)
				compressor.setClosedLoopControl(!compressor.getClosedLoopControl());	
			
			if(driver.getBButton() == DOWN)
				shooter.setRPM(4425);
			else if(driver.getDpad(270) == DOWN)
				shooter.setRPM(rpm);
			else if(driver.getBButton() == UP)
				shooter.setRPM(0);
			
			double hop = 1.0;
			double reverseHop = -1.0;
			double tower = 1.0;
			double slowTower = 0.5;
			double intake = 1.0;
			 if (driver.getDpad(90) == DOWN){
				shooter.setHopperSpeed(reverseHop);
				shooter.setIntakeSpeed(intake);
				shooter.setTowerSpeed(0.0);
			}else if(driver.getAButton() == DOWN){
				shooter.setHopperSpeed(hop);
				shooter.setIntakeSpeed(intake);
				if(shooter.withinRange(25) && shooter.getSetPoint() > 0.0){
					shooter.setTowerSpeed(tower);
				}else if(shooter.getSensor()){
					shooter.setTowerSpeed(slowTower);
				}else{
					shooter.setTowerSpeed(0.0);
				}
			}else if(driver.getAButton() == UP){
				shooter.setIntakeSpeed(0.0);
				shooter.setHopperSpeed(0.0);
				shooter.setTowerSpeed(0.0);
			}
			 
			// vvvv for testing rpm's only don't use this during an actually comp
			/*
			 if(driver.getDpad(0) == DOWN)
				rpm += 5;
			else if (driver.getDpad(180) == DOWN)
				rpm -= 5;
			else if (driver.getDpad(270) == DOWN)
				rpm = 4425;
			 */
			 
			//drive.tankDrive(driver.getY(kLeft), driver.getY(kRight));
			drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, driver.getBumper(kRight) != DOWN);
		}else{
			try{
				if(DataPool.getBooleanData("vision", "found")){
					drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
				}else{
					drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, driver.getBumper(kRight) != DOWN);
				}
			}catch(Exception e){
				System.out.println("me no work no moar");
			}
		}
	}

}
