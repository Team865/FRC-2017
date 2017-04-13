package ca.warp7.robot.auto;

public class Nothing extends AutonomousBase {

	@Override
	public void periodic() {
		/*
		 * If a robit climbs in a match and no ref is there to see it. Does it really climb?
		 * 
		 */
		if(step == 1)
			if(travel(100, 0.9))
				nextStep(0.0);
		
	}

}
