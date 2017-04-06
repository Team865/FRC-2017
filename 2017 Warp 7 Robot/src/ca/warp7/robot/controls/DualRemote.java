package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import ca.warp7.robot.misc.DataPool;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class DualRemote extends ControlsBase {

	private double rpm = 4706;
	private double pixelOffset = -10;
	
	
	public DualRemote() {
		super();
		
		rpm = 4706;
	}
	
	@Override
	public void periodic() {
		if(driver.getTrigger(kLeft) == UP){ // are we doing shooter auto stuff
			
			if(driver.getStickButton(kRight) == PRESSED)
				drive.setDrivetrainReversed(!drive.driveReversed());
				
			if(driver.getTrigger(kRight) == DOWN)
				gearMech.release();
			else if(driver.getTrigger(kRight) == UP)
				gearMech.hold();
			
			if(operator.getBumper(kRight) == DOWN)
				climber.setSpeed(-1.0);
			else if(operator.getBumper(kRight) == UP)
				climber.setSpeed(0.0);
			
			if(operator.getBackButton() == PRESSED)
				compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
			
			if(operator.getTrigger(kLeft) == DOWN)
				shooter.setRPM(rpm);
			else if(operator.getTrigger(kLeft) == UP)
				shooter.setRPM(0);

			if(driver.getXButton() == PRESSED)
					gearMech.flippedyFlip();
			
			double hop = 1.0;
			double reverseHop = -1.0;
			double tower = 1.0;
			double slowTower = 0.4;
			double intake = 1.0;
			 if (operator.getDpad(90) == DOWN){
				shooter.setHopperSpeed(reverseHop);
				shooter.setIntakeSpeed(intake);
				shooter.setTowerSpeed(0.0);
			}else if(operator.getAButton() == DOWN){
				shooter.setHopperSpeed(hop);
				shooter.setIntakeSpeed(intake);
				if(shooter.withinRange(25) && shooter.getSetPoint() > 0.0){
					shooter.setTowerSpeed(tower);
				}else if(shooter.getSensor()){
					shooter.setTowerSpeed(slowTower);
				}else{
					shooter.setTowerSpeed(0.0);
				}
			}else if(operator.getAButton() == UP){
				shooter.setIntakeSpeed(0.0);
				shooter.setHopperSpeed(0.0);
				shooter.setTowerSpeed(0.0);
			}
			
			drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, false, driver.getBumper(kRight) != DOWN);
		}else{
			shooter.setIntakeSpeed(0.0);
			shooter.setHopperSpeed(0.0);
			shooter.setTowerSpeed(0.0);
			
			if(driver.getBButton() == DOWN)
				shooter.setRPM(rpm);
			else if(driver.getBButton() == UP)
				shooter.setRPM(0);
			
			try{
				boolean found = DataPool.getBooleanData("vision", "S_found");
				if(found){
					drive.autoMove(DataPool.getDoubleData("vision", "S_left"), DataPool.getDoubleData("vision", "S_right"));
					double pixelHeight = DataPool.getDoubleData("vision", "S_dist")+pixelOffset;
					if(pixelHeight > 534 && found){
						rpm = 4425;
						driver.setRumble(RumbleType.kLeftRumble, 1.0);
						driver.setRumble(RumbleType.kRightRumble, 1.0);
						operator.setRumble(RumbleType.kLeftRumble, 1.0);
						operator.setRumble(RumbleType.kRightRumble, 1.0);
					}else if(pixelHeight < 312 && found){
						rpm = 5350;
						driver.setRumble(RumbleType.kLeftRumble, 1.0);
						driver.setRumble(RumbleType.kRightRumble, 1.0);
						operator.setRumble(RumbleType.kLeftRumble, 1.0);
						operator.setRumble(RumbleType.kRightRumble, 1.0);
					}else if(found){
						rpm = 0.018*Math.pow(pixelHeight, 2)-19.579*pixelHeight+9675.03;
						driver.setRumble(RumbleType.kLeftRumble, 0.0);
						driver.setRumble(RumbleType.kRightRumble, 0.0);
						operator.setRumble(RumbleType.kLeftRumble, 0.0);
						operator.setRumble(RumbleType.kRightRumble, 0.0);
					}else{
						rpm = 4706;
						driver.setRumble(RumbleType.kLeftRumble, 0.0);
						driver.setRumble(RumbleType.kRightRumble, 0.0);
						operator.setRumble(RumbleType.kLeftRumble, 0.0);
						operator.setRumble(RumbleType.kRightRumble, 0.0);
					}
				}else{
					drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, true);
					rpm = 4706;
					driver.setRumble(RumbleType.kLeftRumble, 0.0);
					driver.setRumble(RumbleType.kRightRumble, 0.0);
					operator.setRumble(RumbleType.kLeftRumble, 0.0);
					operator.setRumble(RumbleType.kRightRumble, 0.0);
				}
			}catch(Exception e){
				System.err.println("WARNING JETSON FAILED");
				rpm = 4706;
				driver.setRumble(RumbleType.kLeftRumble, 0.0);
				driver.setRumble(RumbleType.kRightRumble, 0.0);
				operator.setRumble(RumbleType.kLeftRumble, 0.0);
				operator.setRumble(RumbleType.kRightRumble, 0.0);
			}
			
		}
	}
}
