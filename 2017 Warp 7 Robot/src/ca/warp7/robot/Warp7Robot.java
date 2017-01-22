package ca.warp7.robot;

import ca.warp7.robot.auto.AutonomousBase;
import ca.warp7.robot.auto.RapidFire;
import ca.warp7.robot.controls.DefaultControls;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Warp7Robot extends SampleRobot{
	
	public static Drive drive;
	public static Shooter shooter;
	private static AutonomousBase auto;
	private DefaultControls controls;
	private DataPool vision;
	public static String piCommand;
	
	
	public void robotInit(){
		System.out.println("hello i am robit");
		
		drive = new Drive();
		shooter = new Shooter();
		
		XboxController driver = new XboxController(0);
        XboxController operator = new XboxController(1);
        controls = new DefaultControls(driver, operator);
        vision = new DataPool("vision");
        vision.logData("command", "");
        piCommand = "";
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
		
		//auto = new VISION();
		auto = new RapidFire();
		
		while (isAutonomous() && isEnabled()) {
			auto.periodic(drive, shooter);
			
			 slowPeriodic();
			 Timer.delay(0.005);
		}
	}
	
	public void disabled(){
		//auto.reset(drive, shooter);
		while (!isEnabled()) {
			slowPeriodic();
			Timer.delay(0.005);
		}
	}
	
	public void slowPeriodic(){
		drive.slowPeriodic();
		shooter.slowPeriodic();
		vision.logData("command", piCommand);
		DataPool.collectAllData();
	}
}
