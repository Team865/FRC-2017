package ca.warp7.robot.auto;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Shooter;

public abstract class AutonomousBase {

	public abstract void periodic(Drive drive, Shooter shooter);

	public abstract void reset(Drive drive, Shooter shooter);
}
