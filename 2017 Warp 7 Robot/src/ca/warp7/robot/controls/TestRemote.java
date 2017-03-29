package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import ca.warp7.robot.misc.DataPool;

public class TestRemote extends ControlsBase{
	
	private double rpm = 5300;
	
	
	public TestRemote() {
		super();
		
		rpm = 5300;
	}

	@Override
	public void periodic() {
		if(driver.getTrigger(kLeft) == UP){ // are we doing gear auto stuff
			
			if(driver.getStickButton(kRight) == PRESSED)
				drive.setDrivetrainReversed(!drive.driveReversed());
			
			if(driver.getTrigger(kRight) == DOWN)
				gearMech.release();
			else if(driver.getTrigger(kRight) == UP)
				gearMech.hold();
			
			if(driver.getYButton() == DOWN)
				climber.setSpeed(-1.0);
			else if(driver.getYButton() == UP)
				climber.setSpeed(0.0);
			
			if(driver.getBackButton() == PRESSED)
				compressor.setClosedLoopControl(!compressor.getClosedLoopControl());	
			
			if(driver.getBButton() == DOWN)
				shooter.setRPM(rpm);
			else if(driver.getBButton() == UP)
				shooter.setRPM(0);
			
			if(driver.getXButton() == PRESSED)
				gearMech.flippedyFlip();	
			
			double hop = 0.9;
			double reverseHop = -0.9;
			double tower = 1.0;
			double slowTower = 0.4;
			double intake = 1.0;
			 if (driver.getDpad(90) == DOWN){
				shooter.setHopperSpeed(reverseHop);
				shooter.setIntakeSpeed(intake);
				shooter.setTowerSpeed(0.0);
			}else if(driver.getAButton() == DOWN){
				shooter.setHopperSpeed(hop);
				shooter.setIntakeSpeed(intake);
				shooter.setTowerSpeed(tower);
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
				rpm = 4450;
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
