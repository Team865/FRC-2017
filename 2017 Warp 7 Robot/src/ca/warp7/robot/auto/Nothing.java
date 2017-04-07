package ca.warp7.robot.auto;

public class Nothing extends AutonomousBase {

	@Override
	public void periodic() {
		/* 
		   Robit is bored so is trying to solve riddle...
		   If a tree falls in a forest and no one is there to hear it... did it make a sound?
		  
		   ...
		   
		   I guess not?
		*/
		if(step == 1){
			if(travel(7*12, 0.9))
				nextStep(0.5);
		}else{
			reset();
		}
	}
}
