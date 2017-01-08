package ca.warp7.robot;

public class Constants {
	// PWM Pins
	public static final int[] LEFT_DRIVE_MOTOR_PINS = { 0, 1, 2 };
	public static final int[] RIGHT_DRIVE_MOTOR_PINS = { 3, 4, 5 };
	public static final int HOOD_PIN = 6;
	public static final int INTAKE_MOTOR_PIN = 7;

	// CAN ID's
	public static final int SHOOTER_CAN_ID = 0;

	// DIG Pins
	public static final int INTAKE_PHOTOSENSOR_CLOSE = 0;
	public static final int INTAKE_PHOTOSENSOR_FAR = 1;
	public static final int LEFT_DRIVE_ENCODER_A = 6; // ding
	public static final int LEFT_DRIVE_ENCODER_B = 5; // ding
	public static final int RIGHT_DRIVE_ENCODER_A = 3; // ding
	public static final int RIGHT_DRIVE_ENCODER_B = 4; // ding
	public static final int SWITCH_A_PIN = 8;
	public static final int SWITCH_B_PIN = 7;
	
	// Relay
	public static final int SPIKE_PIN = 0;
	
	// Solenoids (manifold ports)
	public static final int GEAR_CHANGE = 0;
	public static final int PTO_SOLENOID = 1;
	public static final int INTAKE_PISTON_A = 3;
	public static final int INTAKE_PISTON_B = 2;

	// Compressor
	public static final int COMPRESSOR_PIN = 0;

	// General References
	public static final double HARDSTOP_RPM = 3600; //4500 max;
	public static final double DISTANCE_TO_DEFENCE_BASE = 0;
	public static final double INTAKE_SPEED = 0.9;
	public static final double SLOW_INTAKE_SPEED = 0.4;
	public static final double SUPER_SLOW_INTAKE_SPEED = 0.4;
	
	// Robot dimensions and stuff
	public static double WHEELBASE_WIDTH = 24; // inches
	public static double WHEEL_DIAMETER = 8; // inches
	public static double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
	public static int DRIVE_TICKS_PER_REV = 1024; // TODO ???!??
	public static double DRIVE_METERS_PER_TICK = WHEEL_CIRCUMFERENCE / DRIVE_TICKS_PER_REV;
	
	public static double MAX_VELOCITY = 12;// ft/s

    // reversed?
    public static boolean BATTERY = false;
    public static boolean INTAKE = true;

    public static String ATTR_EX_MODE = "CameraAttributes::Exposure::Mode";
    public static String ATTR_EX_VALUE = "CameraAttributes::Exposure::Value";

    public static String ATTR_BR_MODE = "CameraAttributes::Brightness::Mode";
    public static String ATTR_BR_VALUE = "CameraAttributes::Brightness::Value";
}
