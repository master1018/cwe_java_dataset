
package net.sf.robocode.test.robots;
import net.sf.robocode.test.helpers.RobocodeTestBed;
import org.junit.Assert;
import robocode.control.events.TurnEndedEvent;
public class TestConstructorHttpAttack extends RobocodeTestBed {
	private boolean messagedInitialization;
	private boolean messagedAccessDenied;
	@Override
	public String getRobotNames() {
		return "tested.robots.ConstructorHttpAttack,sample.Target";
	}
	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);
		final String out = event.getTurnSnapshot().getRobots()[0].getOutputStreamSnapshot();
		if (out.contains("An error occurred during initialization")) {
			messagedInitialization = true;	
		}	
		if (out.contains("access denied (java.net.SocketPermission")
				|| out.contains("access denied (\"java.net.SocketPermission\"")) {
			messagedAccessDenied = true;	
		}	
	}
	@Override
	protected void runTeardown() {
		Assert.assertTrue("Error during initialization", messagedInitialization);
		Assert.assertTrue("HTTP connection is not allowed", messagedAccessDenied);
	}
	@Override
	protected int getExpectedErrors() {
		return hasJavaNetURLPermission ? 3 : 2; 
	}
}
