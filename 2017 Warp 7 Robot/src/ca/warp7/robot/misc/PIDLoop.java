package ca.warp7.robot.misc;

public class PIDLoop {

	private final double p,i,d;
	private double scale;
	private int counter, doneCounter;
	private double errorSum, oldError, currentError, offset;
	private boolean flag;
	
	
	public PIDLoop(double p, double i, double d, double scale){
		errorSum = 0;
		oldError = 0;
		currentError = 0;
		counter = 0;
		doneCounter = 0;
		offset = 0;
		flag = false;
		this.p = p;
		this.i = i;
		this.d = d;
		scale = Math.abs(scale);
		this.scale = scale;
	}
	
	public PIDLoop(double p, double i, double d){
		this(p, i, d, 1);
	}
	
	public PIDLoop(PIDLoop settings){
		this(settings.getPID()[0], settings.getPID()[1], settings.getPID()[2], settings.getPID()[3]);
	}
	
	public double[] getPID(){
		return new double[]{p, i, d, scale};
	}
	
	public double getOffset() {
		return offset;
	}
	
	public void setScale(double scale){
		scale = Math.abs(scale);
		this.scale = scale;
	}
	
	public double getError(){
		return currentError;
	}
	
	public double getValue(){
		return getPTerm()+getITerm()+getDTerm();
	}
	
	public boolean finished(int finishedCodeRuns, double tolerance){
		countTolerance(tolerance);
		return doneCounter >= finishedCodeRuns;
	}
	
	public void countTolerance(double tolerance){	
		if(inTolerance(tolerance))
			doneCounter++;
		else
			doneCounter = 0;
	}
	
	public boolean inTolerance(double tolerance){
		tolerance = Math.abs(tolerance);
		return Math.abs(currentError) <= tolerance;
	}
	
	public double getValue(double limit){
		return Util.limit(getValue(), limit);
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
	
	public void setOffset(double offset){
		if(!flag){
			this.offset = offset;
			flag = true;
		}
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
	}
}
