package ca.warp7.robot;

import static ca.warp7.robot.Constants.COMPRESSOR_PIN;
import static ca.warp7.robot.auto.AutonomousBase.autoPool;

import ca.warp7.robot.auto.AutonomousBase;
import ca.warp7.robot.auto.CentreGear;
import ca.warp7.robot.auto.GearLeft;
import ca.warp7.robot.auto.GearRight;
import ca.warp7.robot.auto.Nothing;
import ca.warp7.robot.auto.ShootBlue;
import ca.warp7.robot.auto.ShootRed;
import ca.warp7.robot.controls.ControlsBase;
import ca.warp7.robot.controls.TestRemote;
import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Warp7Robot extends SampleRobot{
	
	public static Drive drive;
	public static Shooter shooter;
	public static Climber climber;
	public static GearMech gearMech;
	public static Compressor compressor;
	
	private static AutonomousBase auto;
	private static ControlsBase controls;
	
	private DriverStation driverStation;
	private DigitalInput s4;
	private DigitalInput s5;
	private DigitalInput s6;
	private DigitalInput s7;
	private DigitalInput s8;
	
	
	public void robotInit(){
		System.out.println("Hello me is robit");
		
		drive = new Drive();
		shooter = new Shooter();
		climber = new Climber();
		gearMech = new GearMech();
		compressor = new Compressor(COMPRESSOR_PIN);
		
		driverStation = DriverStation.getInstance();
		s4 = new DigitalInput(4);
		s5 = new DigitalInput(5);
		s6 = new DigitalInput(6);
		s7 = new DigitalInput(7);
		s8 = new DigitalInput(8);
	}
	
	public void operatorControl(){
        controls = new TestRemote();

		if(driverStation.isFMSAttached())
			compressor.setClosedLoopControl(false);
        else 
        	compressor.setClosedLoopControl(true);
        
		 while (isOperatorControl() && isEnabled()) {
			 controls.periodic();
			 periodic();
			 
	         Timer.delay(0.005);
		 }
	}
	
	public void autonomous(){
		
		if(!s4.get())
			auto = new CentreGear();
		else if(!s5.get())
			auto = new GearRight();
		else if(!s6.get())
			auto = new GearLeft();
		else if(!s7.get())
			auto = new ShootRed();
		else if(!s8.get())
			auto = new ShootBlue();
		else
			auto = new Nothing();
		
		while (isAutonomous() && isEnabled()) {
			auto.periodic();
			periodic();
			Timer.delay(0.005);
		}
	}
	
	public void disabled(){
		while (!isEnabled()) {
			periodic();
			Timer.delay(0.005);
		}
	}
	
	public void periodic(){
		drive.periodic();
		
		if(!s4.get())
			autoPool.logInt("Switch Key", 4);
		else if(!s5.get())
			autoPool.logInt("Switch Key", 5);			
		else if(!s6.get())
			autoPool.logInt("Switch Key", 6);
		else if(!s7.get())
			autoPool.logInt("Switch Key", 7);
		else if(!s8.get())
			autoPool.logInt("Switch Key", 8);
		else
			autoPool.logInt("Switch Key", -1);
		
		DataPool.collectAllData();
	}
}
