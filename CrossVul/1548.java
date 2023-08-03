
package net.sf.robocode.test.robots;
import net.sf.robocode.test.helpers.RobocodeTestBed;
import org.junit.Assert;
import robocode.control.events.TurnEndedEvent;
public class TestHttpAttack extends RobocodeTestBed {
	private boolean messagedAccessDenied;
	@Override
	public String getRobotNames() {
		return "tested.robots.HttpAttack,sample.Target";
	}
	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);
		final String out = event.getTurnSnapshot().getRobots()[0].getOutputStreamSnapshot();
		if (out.contains("access denied (java.net.SocketPermission")
				|| out.contains("access denied (\"java.net.SocketPermission\"")) {
			messagedAccessDenied = true;	
		}	
	}
	@Override
	protected void runTeardown() {
		Assert.assertTrue("HTTP connection is not allowed", messagedAccessDenied);
	}
	@Override
	protected int getExpectedErrors() {
		return hasJavaNetURLPermission ? 2 : 1; 
	}
}
