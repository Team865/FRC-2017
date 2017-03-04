package ca.warp7.robot;

import static ca.warp7.robot.Constants.COMPRESSOR_PIN;

import ca.warp7.robot.auto.AutonomousBase;
import ca.warp7.robot.auto.GearCentre;
import ca.warp7.robot.auto.GearHopperLeft;
import ca.warp7.robot.auto.GearHopperRight;
import ca.warp7.robot.auto.Nothing;
import ca.warp7.robot.auto.ShootGearLeft;
import ca.warp7.robot.auto.ShootGearRight;
import ca.warp7.robot.controls.ControlsBase;
import ca.warp7.robot.controls.SingleRemote;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Warp7Robot extends SampleRobot{
	
	public static Drive drive;
	public static Shooter shooter;
	public static Climber climber;
	public static GearMech gearMech;
	private static AutonomousBase auto;
	private ControlsBase controls;
	private DataPool vision;
	private DataPool generalPool;
	public static DriverStation driverStation;
	public static String jetsonCommand;
	public static Compressor compressor;
	public static DigitalInput s4;
	public static DigitalInput s5;
	public static DigitalInput s6;
	public static DigitalInput s7;
	public static DigitalInput s8;
	
	
	// 45678
	
	
	public void robotInit(){
		System.out.println("hello i am robit");
		
		drive = new Drive();
		shooter = new Shooter();
		climber = new Climber();
		gearMech = new GearMech();
		
		driverStation = DriverStation.getInstance();
		compressor = new Compressor(COMPRESSOR_PIN);
		
		s4 = new DigitalInput(4);
		s5 = new DigitalInput(5);
		s6 = new DigitalInput(6);
		s7 = new DigitalInput(7);
		s8 = new DigitalInput(8);
		
		XboxController driver = new XboxController(0);
        XboxController operator = new XboxController(1);
        controls = new SingleRemote(driver, operator);
        vision = new DataPool("vision");
        vision.logData("command", "");
        generalPool = new DataPool("General");
        jetsonCommand = "";
	}
	
	public void operatorControl(){
		controls.reset();
		if(driverStation.isFMSAttached()) {
            compressor.setClosedLoopControl(false);
        } else {
            compressor.setClosedLoopControl(true);
        }
		 while (isOperatorControl() && isEnabled()) {
			 controls.periodic();
			 
			 slowPeriodic();
	         Timer.delay(0.005);
		 }
	}
	
	public void autonomous(){
		
		if(!s4.get())
			auto = new ShootGearLeft(drive);
		else if(!s5.get())
			auto = new ShootGearRight();
		else if(!s6.get())
			auto = new GearHopperLeft();
		else if(!s7.get())
			auto = new GearHopperRight();
		else if(!s8.get())
			auto = new GearCentre();
		else
			auto = new Nothing();
		//auto = new GyroTest();
		//auto = new VISION();
		//auto = new RapidFire();
		
		while (isAutonomous() && isEnabled()) {
			auto.periodic(drive, gearMech, climber, shooter);
			
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
		//shooter.slowPeriodic();
		vision.logData("command", jetsonCommand);
		
		if(!s4.get())
			generalPool.logInt("Switch Key", 4);
		else if(!s5.get())
			generalPool.logInt("Switch Key", 5);			
		else if(!s6.get())
			generalPool.logInt("Switch Key", 6);
		else if(!s7.get())
			generalPool.logInt("Switch Key", 7);
		else if(!s8.get())
			generalPool.logInt("Switch Key", 8);
		else
			generalPool.logInt("Switch Key", -1);
		
		DataPool.collectAllData();
	}
}
