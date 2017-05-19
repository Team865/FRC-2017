package ca.warp7.robot.misc;

import edu.wpi.first.wpilibj.Timer;

public class MotionProfile {

	private final double p,i,d,v,a;
	private double scale;
	private int counter, doneCounter;
	private double errorSum, oldError, currentError, offset;
	private double oldPos, oldTime;
	private Equation A, V;
	private boolean flag;
	private double t;

	
	public MotionProfile(double v, double a, double p, double i, double d, double scale, Equation V, Equation A){
		this.p = p;
		this.i = i;
		this.d = d;
		this.v = v;
		this.a = a;
		this.scale = Math.abs(scale);
		this.A = A;
		this.V = V;
		counter = 0;
		oldPos = Double.NaN;
		oldTime = Double.NaN;
		doneCounter = 0;
		offset = 0;
		flag = false;
		errorSum = 0;
		oldError = 0;
		currentError = 0;
		t = Double.NaN;
	}
	
	public MotionProfile(double v, double a, double p, double i, double d, double scale){
		this(v,a,p,i,d,scale,null,null);
	}
	
	public MotionProfile(double v, double a, double p, double i, double d){
		this(v,a,p,i,d,1,null,null);
	}
	
	public void setProfile(double scale, Equation A, Equation V){
		this.scale = Math.abs(scale);
		this.A = A;
		this.V = V;
	}
	
	public void setProfile(Equation A, Equation V){
		this.A = A;
		this.V = V;
	}

	public double getOffset() {
		return offset;
	}
	
	public double getError(){
		return currentError;
	}
	
	public double getValue(double pos){
		if(t == Double.NaN)
			t = Timer.getFPGATimestamp();
		double time = Timer.getFPGATimestamp()-t;
		return getVTerm(time, pos)+getATerm(time)+getPTerm()+getITerm()+getDTerm();
	}
	
	public double getValue(double pos, double limit){
		return Util.limit(getValue(pos), limit);
	}
	
	public double getPTerm(){
		return p*(currentError/scale);
	}
	
	public double getITerm(){
		return i*(errorSum/scale);
	}

	public double getDTerm(){
		return d*((currentError-oldError)/scale);
	}
	
	public double getVTerm(double time, double pos){
		double vel = V.solve(time);
		calculateError(vel == Double.NaN ? 0 : vel, calculateVelocity(pos));
		return v*vel;
	}
	
	private double calculateVelocity(double pos){
		double time = Timer.getFPGATimestamp();
		if(oldTime == Double.NaN)
			oldTime = time;
		if(oldPos == Double.NaN)
			oldPos = pos;
		double vel = (pos-oldPos)/(time-oldTime);
		oldTime = time;
		oldPos = pos;
		return vel;
	}
	
	public double getATerm(double time){
		return a*A.solve(time);
	}
	
	public void setOffset(double offset){
		if(!flag){
			this.offset = offset;
			flag = true;
		}
	}
	
	public boolean finished(int finishedCodeRuns, double tolerance){
		countTolerance(tolerance);
		return doneCounter >= finishedCodeRuns;
	}
	
	public void countTolerance(double tolerance){	
		if(inTolerance(tolerance) && t != Double.NaN && V.solve(t) == Double.NaN)
			doneCounter++;
		else
			doneCounter = 0;
	}
	
	public boolean inTolerance(double tolerance){
		return Math.abs(currentError) <= Math.abs(tolerance);
	}
	
	public void setScale(double scale){
		this.scale = Math.abs(scale);
	}
	
	public void calculateError(double target, double current){
		current += offset;
		double error = target-current;
		setError(error);
	}
	
	public void setError(double error){
		counter++;
		errorSum += error;
		oldError = currentError;
		currentError = error;
		if(counter > 1000){
			counter = 0;
			errorSum = 0;
		}
	}
	
	public void reset(){
		errorSum = 0;
		oldError = 0;
		currentError = 0;
		counter = 0;		
		doneCounter = 0;
		offset = 0;
		flag = false;
		oldPos = Double.NaN;
		oldTime = Double.NaN;
		t = Double.NaN;
	}
}
