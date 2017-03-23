package ca.warp7.robot.auto;

public class ShootGearLeft extends AutonomousBase {
	
	@Override
	public void periodic() {
		switch(step){
		case 1:
			if(shoot(4450, 6))
				nextStep(0.5);
			break;
		case 2:
			if(relTurn(18))
				nextStep(0.5);
			break;
		case 3:
			if(travel(-3*12))
				nextStep(0.5);
			break;
		case 4:
			if(relTurn(-81))
				nextStep(0.5);
			break;
		case 5:
			if(travel(4.25*12))
				nextStep(0.5);
			break;
		case 6:
			if(relTurn(35))
				nextStep(0.5);
			break;
		case 7:
			if(travel(3*12))
				nextStep(0.5);
			break;
		case 8:	
			try{
				if(gearMove())
					nextStep(0.5);
			}catch(NullPointerException npe){
				System.err.println("Nathan plug in the Jetson >:(");
				step++;
			}
			break;
		case 9:
			if(travel(-4))
				nextStep(0.5);			
		case 10:
			gearMech.release();
			nextStep(0.5);
			break;
		case 11:
			if(travel(-3.75*12))
				nextStep(0.5);
			break;
		case 12:
			//-60 would be square with driver stations
			if(relTurn(30))
				nextStep(0.5);
			break;
		case 13:
			if(travel(-2*12))
				nextStep(0.5);
			break;
		default:
			reset();
			break;
		}
	}
}
