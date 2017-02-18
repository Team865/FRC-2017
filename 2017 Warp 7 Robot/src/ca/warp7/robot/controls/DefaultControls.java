package ca.warp7.robot.controls;

import static ca.warp7.robot.Warp7Robot.climber;
import static ca.warp7.robot.Warp7Robot.compressor;
import static ca.warp7.robot.Warp7Robot.drive;
import static ca.warp7.robot.Warp7Robot.gearMech;
import static ca.warp7.robot.Warp7Robot.shooter;

import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

public class DefaultControls extends ControlsBase{

	private XboxController driver;
	private XboxController operator;
	private boolean rightStick = false;
	private boolean back_op = false;
	private boolean x = false;
	
	
	public DefaultControls(XboxController driver, XboxController operator) {
		this.driver = driver;
		this.operator = operator;
	}

	@Override
	public void reset() {
		rightStick = false;
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
					drive.setDrivetrainReversed(!drive.isDrivetrainReversed());
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
				climber.requestSpeed(-1.0);
			}else{
				climber.requestSpeed(0.0);
			}
			
			if(operator.getBackButton()){
				if(!back_op){
					compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
					back_op = true;
				}
			}else{
				back_op = false;
			}
			
			if(driver.getXButton() || operator.getXButton()){
				if(!x){
					gearMech.flippedyFlip();
					x = true;
				}
			}else{
				x = false;
			}
			
			if(driver.getAButton()){
				shooter.setHopperSpin(1.0);
			}else{
				shooter.setHopperSpin(0.0);
			}
			
			drive.cheesyDrive(-driver.getX(Hand.kRight), driver.getY(Hand.kLeft), driver.getBumper(Hand.kLeft), driver.getTriggerAxis(Hand.kLeft) >= 0.5, driver.getBumper(Hand.kRight));
		}else{
			if(DataPool.getBooleanData("vision", "found")){
				drive.autoMove(DataPool.getDoubleData("vision", "left"), DataPool.getDoubleData("vision", "right"));
			}else{
				drive.cheesyDrive(-driver.getX(Hand.kRight), driver.getY(Hand.kLeft), driver.getBumper(Hand.kLeft), driver.getTriggerAxis(Hand.kLeft) >= 0.5, driver.getBumper(Hand.kRight));
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
