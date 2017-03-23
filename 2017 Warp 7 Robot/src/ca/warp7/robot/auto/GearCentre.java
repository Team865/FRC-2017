package ca.warp7.robot.auto;

public class GearCentre extends AutonomousBase {

	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(travel(3*12))
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
		case 3:
			if(travel(-4))
				nextStep(0.5);			
		case 4:
			gearMech.release();
			nextStep(0.5);
			break;
		case 5:
			if(travel(-4*12))
				nextStep(0.5);
			break;
		case 6:
			if(relTurn(60))
				nextStep(0.5);
			break;
		case 7:
			if(shoot(5500, 6.5))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}
	}

}
