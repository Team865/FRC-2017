package ca.warp7.robot;

import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Warp7Robot extends SampleRobot{

	
	public static Drive drive;
	private DefaultControls controls;
	
	
	public void robotInit(){
		System.out.println("hello i am robit");
		
		drive = new Drive();
		
		XboxController driver = new XboxController(0);
        XboxController operator = new XboxController(1);
        controls = new DefaultControls(driver, operator);
	}
	
	public void operatorControl(){
		controls.reset();
		 while (isOperatorControl() && isEnabled()) {
			 controls.periodic();
			 
			 slowPeriodic();
	         Timer.delay(0.005);
		 }
	}
	
	public void autonomous(){
		
		while (isAutonomous() && isEnabled()) {
			
			
			 slowPeriodic();
			 Timer.delay(0.005);
		}
	}
	
	public void disabled(){
		
		while (!isOperatorControl() && !isAutonomous() && !isEnabled()) {
			 
			
			slowPeriodic();
			Timer.delay(0.005);
		}
	}
	
	public void slowPeriodic(){
		drive.slowPeriodic();
		DataPool.collectAllData();
	}
}
