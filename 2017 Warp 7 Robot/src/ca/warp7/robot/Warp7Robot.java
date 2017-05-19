package ca.warp7.robot;

import static ca.warp7.robot.Constants.COMPRESSOR_PIN;
import static ca.warp7.robot.Constants.LIGHT_PORT;
import static ca.warp7.robot.auto.AutonomousBase.autoPool;

import ca.warp7.robot.auto.AutonomousBase;
import ca.warp7.robot.auto.GearCentreBlue;
import ca.warp7.robot.auto.GearCentreRed;
import ca.warp7.robot.auto.GearLeftBlue;
import ca.warp7.robot.auto.GearLeftRed;
import ca.warp7.robot.auto.GearRightBlue;
import ca.warp7.robot.auto.GearRightRed;
import ca.warp7.robot.controls.ControlsBase;
import ca.warp7.robot.controls.DualRemote;
import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.misc.VisionCommand;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Warp7Robot extends SampleRobot{
	
	public static Drive drive;
	public static Shooter shooter;
	public static Climber climber;
	public static GearMech gearMech;
	public static Compressor compressor;
	public static VisionCommand vision;
	
	private static AutonomousBase auto;
	private static ControlsBase controls;
	
	private DriverStation driverStation;
	public static Solenoid light;
	
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
		
		vision = VisionCommand.OFF;
		
		driverStation = DriverStation.getInstance();
		light = new Solenoid(LIGHT_PORT);
		s4 = new DigitalInput(4);
		s5 = new DigitalInput(5);
		s6 = new DigitalInput(6);
		s7 = new DigitalInput(7);
		s8 = new DigitalInput(8);
	}
	
	public void operatorControl(){
        controls = new DualRemote();

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
		// top
		if(!s4.get())
			auto = new GearLeftBlue();//shoot
		else if(!s5.get())
			auto = new GearRightBlue();//non-shoot
		else if(!s6.get())
			auto = new GearCentreBlue();//shoot
		else if(!s7.get())
			auto = new GearCentreRed();//shoot
		else if(!s8.get())
			auto = new GearLeftRed();//non-shoot
		else
			auto = new GearRightRed();//shoot
		// bottom
		
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
		
		try{
			light.set(true);
		}catch(Exception e){
			light.set(false);
		}
		
		try{
			autoPool.logInt("Step", auto.step);
		}catch(Exception e){
			autoPool.logInt("Step", 0);
		}
		
		autoPool.logData("I AM HERE", "true");
		
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
