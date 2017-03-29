package ca.warp7.robot.auto;

public class CentreGear extends AutonomousBase {
	
	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(travel(4.5*12))
				nextStep(0.5);
			break;
		case 2:
			try{
				if(gearMove())
					nextStep(0.5);
			}catch(NullPointerException npe){
				System.err.println("Nathan plug in the Jetson >:(");
				step++;
			}
			break;
		case 5:
			if(travel(-2.5))
				nextStep(0.5);
			break;
		case 6:
			gearMech.release();
			nextStep(0.5);
			break;
		case 7:
			if(travel(-(2*12-2.5)))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
			
		}
	}
}