package ca.warp7.robot.auto;

import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.GearMech;

public abstract class AutonomousBase {

	public abstract void periodic(Drive drive, GearMech gearMech, Climber climber);

	public abstract void reset(Drive drive, GearMech gearMech, Climber climber);
}
