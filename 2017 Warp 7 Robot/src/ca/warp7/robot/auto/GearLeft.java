package ca.warp7.robot.auto;

public class GearLeft extends AutonomousBase{

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(travel(6*12, 0.9))
				nextStep(0.5);
			break;
		case 2:
			if(relTurn(60))
				nextStep(0.5);
			break;
		case 3:
			if(travel(3*12, 0.9))
				nextStep(0.5);
			break;
		case 4:
			try{
				if(gearMove())
					nextStep(0.5);
			}catch(NullPointerException npe){
				System.err.println("Nathan plug in the Jetson >:(");
				step++;
			}
			break;
		case 5:
			if(travel(-2.5, 0.9))
				nextStep(0.5);
			break;
		case 6:
			gearMech.release();
			nextStep(0.5);
			break;
		case 7:
			if(travel(-(5*12-2.5), 0.9))
				nextStep(0.5);
			break;
		case 8:
			if(relTurn(-60))
				nextStep(0.5);
			break;
		case 9:
			if(travel(3*12, 0.9))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}

	}

}
