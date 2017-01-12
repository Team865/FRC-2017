package ca.warp7.robot.controls;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;

import static ca.warp7.robot.Warp7Robot.drive;

public class DefaultControls extends ControlsBase{

	private XboxController driver;
	private XboxController operator;
	private boolean rightStick = false;
	
	
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
		if(driver.getStickButton(Hand.kRight)){
			if(!rightStick){
				drive.setDrivetrainReversed(!drive.isDrivetrainReversed());
				rightStick = true;
			}
		}else
			if(rightStick)
				rightStick = false;
		
		drive.cheesyDrive(-driver.getX(Hand.kRight), driver.getY(Hand.kLeft), driver.getBumper(Hand.kLeft), driver.getBumper(Hand.kRight));
	}

	@Override
	public void disabled() {
		driver.setRumble(RumbleType.kLeftRumble, 0);
        driver.setRumble(RumbleType.kRightRumble, 0);
        operator.setRumble(RumbleType.kLeftRumble, 0);
        operator.setRumble(RumbleType.kRightRumble, 0);
	}

}
