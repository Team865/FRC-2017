package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.controls.Control.DOWN;
import static ca.warp7.robot.controls.Control.PRESSED;
import static ca.warp7.robot.controls.Control.UP;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kLeft;
import static edu.wpi.first.wpilibj.GenericHID.Hand.kRight;

import ca.warp7.robot.misc.DataPool;

public class DualRemote extends ControlsBase {

	@Override
	public void periodic() {
		if(driver.getTrigger(kLeft) == UP){ // are we doing gear auto stuff
			
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
				shooter.setRPM(4450);
			else if(operator.getTrigger(kLeft) == UP)
				shooter.setRPM(0);

			if(driver.getXButton() == PRESSED)
					gearMech.flippedyFlip();
			
			double hop = 0.7;
			double reverseHop = -0.6;
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
				if(shooter.withinRange(40) && shooter.getSetPoint() > 0.0){
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
			try{
				if(DataPool.getBooleanData("vision", "found")){
					drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
				}else{
					drive.cheesyDrive(-driver.getX(kRight), driver.getY(kLeft), driver.getBumper(kLeft) == DOWN, driver.getTrigger(kLeft) == DOWN, driver.getBumper(kRight) != DOWN);
				}
			}catch(Exception e){}
		}
	}
}
