package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.HOPPER_SPIN_PINS;
import static ca.warp7.robot.Constants.SHOOTER_MASTER_ID;
import static ca.warp7.robot.Constants.SHOOTER_SLAVE_ID;
import static ca.warp7.robot.Constants.TOWER_SPIN_PINS;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import ca.warp7.robot.misc.DataPool;
import ca.warp7.robot.misc.MotorGroup;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {

	public static DataPool shooterPool;
	
	private MotorGroup hopperSpin;
	private MotorGroup towerSpin;
	private CANTalon masterTalon;
	private CANTalon slaveTalon;
	
	
	public Shooter(){
		hopperSpin = new MotorGroup(HOPPER_SPIN_PINS, VictorSP.class);
		towerSpin = new MotorGroup(TOWER_SPIN_PINS, VictorSP.class);
		
		slaveTalon = new CANTalon(SHOOTER_SLAVE_ID);
		masterTalon = new CANTalon(SHOOTER_MASTER_ID);
		slaveTalon.reverseOutput(true);
		slaveTalon.enableBrakeMode(false);
		slaveTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		slaveTalon.set(masterTalon.getDeviceID());
		masterTalon.configEncoderCodesPerRev(12*86);
		masterTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		masterTalon.configNominalOutputVoltage(+0.0f,  -0.0f);
		masterTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		masterTalon.changeControlMode(TalonControlMode.Speed);
		masterTalon.reverseOutput(true);
		masterTalon.reverseSensor(true);
		masterTalon.setProfile(0);
		masterTalon.setF(0);
		masterTalon.setP(0.08);
		masterTalon.setI(0.00015);
		masterTalon.setD(2);
		//p = 0.08, I = 0.000125, D = 2, 12.2 v idle, 0.6 tower w/ velcrow // 50 in 4.5 in rot on the back, left motor only, no fly wheel
		//p = 0.08, I = 0.00015, D = 2, 12.2 v idle, 0.6 tower w/ velcrow // 50 in 4.5 in rot on the back, both motors, no fly wheel
		
		shooterPool = new DataPool("Shooter");
	}
	
	public void setRPM(double targetSpeed){
		if(targetSpeed > 0){
			masterTalon.enable();
			slaveTalon.enable();
			masterTalon.setSetpoint(targetSpeed);
			slaveTalon.set(masterTalon.getDeviceID());
			shooterPool.logDouble("rpm", masterTalon.getSpeed());
			shooterPool.logDouble("Setpoint", masterTalon.getSetpoint());
		}else{
			stop();
		}
	}
	
	public double getRPM(){
		return masterTalon.getSpeed();
	}
	
	public boolean withinRange(double allowableError){
		double speed = masterTalon.getSpeed();
		double setpoint = masterTalon.getSetpoint();
		return Math.abs(speed-setpoint) < allowableError;
	}
	
	private void stop(){
		masterTalon.disable();
		slaveTalon.disable();
		masterTalon.set(0);
		slaveTalon.set(masterTalon.getDeviceID());
		shooterPool.logDouble("rpm", masterTalon.getSpeed());
		shooterPool.logDouble("Setpoint", masterTalon.getSetpoint());
	}
	
	public void setHopperSpeed(double speed){
		if(hopperSpin.get() != -speed)
			hopperSpin.set(-speed);
	}
	
	public void setTowerSpeed(double speed){
		if(towerSpin.get() != speed)
			towerSpin.set(speed);
	}
}
