package ca.warp7.robot.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import ca.warp7.robot.networking.DataPool;

import static ca.warp7.robot.Constants.SHOOTER_CAN_ID;

public class Shooter {

	private CANTalon _talon;
	private DataPool _pool;
	
	public Shooter(){
		_talon = new CANTalon(SHOOTER_CAN_ID);
		_talon.configEncoderCodesPerRev(12*86);
		_talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		_talon.configNominalOutputVoltage(+0.0f, -0.0f);
		_talon.configPeakOutputVoltage(+12.0f, -12.0f);
		_talon.changeControlMode(TalonControlMode.Speed);
		_talon.setProfile(0);
		_talon.setF(0);
		_talon.setP(1);
		_talon.setI(0.00011);
		_talon.setD(30);
		//P 1
		//I 0.0001
		//D 15
		
		//P 1.125
		//I 0.0000165
		//D 1
		
		//1
		//0.0001105
		//30
		_talon.reverseOutput(true);
		_talon.reverseSensor(true);
		_talon.enableBrakeMode(false);
		/*
		 * practice bot _talon.setF(1.345); _talon.setP(30); _talon.setI(0);
		 * _talon.setD(0);
		 */
		// _talon.setVoltageRampRate(10);

		_pool = new DataPool("Shooter");
	}
	
	public void spinUp(double targetSpeed) {
		_talon.enable();
		_talon.setSetpoint(targetSpeed);
	}

	public void slowPeriodic() {
		if(_talon != null){
	        _pool.logDouble ("setpoint", _talon.getSetpoint());
			_pool.logDouble ("speed", _talon.getSpeed());
			_pool.logDouble ("offset", _talon.getSetpoint()-_talon.getSpeed());
			_pool.logBoolean("readyToFire", atTargetRPM());
			_pool.logDouble("Hot stuff", _talon.getTemperature());
		}
	}

	public double getSpeed() {
		return _talon.getSpeed();
	}

	public boolean atTargetRPM() {
		double speed = _talon.getSpeed();
		double setpoint = _talon.getSetpoint();
		return Math.abs(speed - setpoint) < 35 ;
	}

	public void coast() {
		_talon.disable();
		_talon.set(0);
	}
}
