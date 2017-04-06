package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class TestRemote extends ControlsBase{
	
	private double rpm = 4706;
	
	public TestRemote() {
		super();
		
		rpm = 4706;
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
			
			if(driver.getXButton() == PRESSED)
				gearMech.flippedyFlip();
			
			if(driver.getBButton() == DOWN)
				shooter.setRPM(rpm);
			else if(driver.getBButton() == UP)
				shooter.setRPM(0);
			
			double hop = 1.0;
			double reverseHop = -1.0;
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
				// old value == 40
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
			
			 if(driver.getDpad(0) == DOWN){
				rpm += 5;
				System.out.println("UP");
			 }else if (driver.getDpad(180) == DOWN){
				rpm -= 5;
				System.out.println("DOWN");
			}else if (driver.getDpad(270) == DOWN){
				rpm = 4706;
				System.out.println("RESET");
			 }
			 Shooter.shooterPool.logDouble("Setpoint", rpm);
			 
			//drive.tankDrive(driver.getY(kLeft), driver.getY(kRight));
			drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, driver.getBumper(kRight) != DOWN);
		}else{
			shooter.setIntakeSpeed(0.0);
			shooter.setHopperSpeed(0.0);
			shooter.setTowerSpeed(0.0);
			
			if(driver.getBButton() == DOWN)
				shooter.setRPM(rpm);
			else if(driver.getBButton() == UP)
				shooter.setRPM(0);
			
			/*
			try{
				boolean found = DataPool.getBooleanData("vision", "D_found");
				if(found){
					drive.autoMove(DataPool.getDoubleData("vision", "D_left"), DataPool.getDoubleData("vision", "D_right"));
				}else{
					drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, false);
				}
			}catch(Exception e){
				System.err.println("WARNING JETSON FAILED");
				drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, false);
			}
			*/
			
			try{
				boolean found = DataPool.getBooleanData("vision", "S_found");
				if(found){
					drive.autoMove(DataPool.getDoubleData("vision", "S_left"), DataPool.getDoubleData("vision", "S_right"));
					double pixelHeight = DataPool.getDoubleData("vision", "S_dist")-10;
					if(pixelHeight > 534 && found){
						rpm = 4425;
						driver.setRumble(RumbleType.kLeftRumble, 1.0);
						driver.setRumble(RumbleType.kRightRumble, 1.0);
					}else if(pixelHeight < 312 && found){
						rpm = 5350;
						driver.setRumble(RumbleType.kLeftRumble, 1.0);
						driver.setRumble(RumbleType.kRightRumble, 1.0);
					}else if(found){
						rpm = 0.018*Math.pow(pixelHeight, 2)-19.579*pixelHeight+9675.03;
						driver.setRumble(RumbleType.kLeftRumble, 0.0);
						driver.setRumble(RumbleType.kRightRumble, 0.0);
					}else{
						rpm = 4706;
						driver.setRumble(RumbleType.kLeftRumble, 0.0);
						driver.setRumble(RumbleType.kRightRumble, 0.0);
					}
				}else{
					drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, true);
					rpm = 4706;
					driver.setRumble(RumbleType.kLeftRumble, 0.0);
					driver.setRumble(RumbleType.kRightRumble, 0.0);
				}
			}catch(Exception e){
				System.err.println("WARNING JETSON FAILED");
				rpm = 4706;
				driver.setRumble(RumbleType.kLeftRumble, 0.0);
				driver.setRumble(RumbleType.kRightRumble, 0.0);
			}
			
			
		}
	}

}
