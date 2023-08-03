
package net.sf.robocode.host.security;
import net.sf.robocode.host.IHostedThread;
import net.sf.robocode.host.IThreadManager;
import net.sf.robocode.io.RobocodeProperties;
import java.net.SocketPermission;
import java.security.AccessControlException;
import java.security.Permission;
public class RobocodeSecurityManager extends SecurityManager {
	private final IThreadManager threadManager;
	public RobocodeSecurityManager(IThreadManager threadManager) {
		super();
		this.threadManager = threadManager;
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		while (tg != null) {
			threadManager.addSafeThreadGroup(tg);
			tg = tg.getParent();
		}
		isSafeThread(Thread.currentThread());
		if (RobocodeProperties.isSecurityOn()) {
			System.setSecurityManager(this);
		}
	}
	@Override
	public void checkAccess(Thread t) {
		if (RobocodeProperties.isSecurityOff()) {
			return;
		}
		Thread c = Thread.currentThread();
		if (isSafeThread(c)) {
			return;
		}
		super.checkAccess(t);
		boolean found = false;
		ThreadGroup cg = c.getThreadGroup();
		ThreadGroup tg = t.getThreadGroup();
		while (tg != null) {
			if (tg == cg) {
				found = true;
				break;
			}
			try {
				tg = tg.getParent();
			} catch (AccessControlException e) {
				break;
			}
		}
		if (!found) {
			String message = "Preventing " + c.getName() + " from access to " + t.getName();
			IHostedThread robotProxy = threadManager.getLoadedOrLoadingRobotProxy(c);
			if (robotProxy != null) {
				robotProxy.punishSecurityViolation(message);
			}
			throw new SecurityException(message);
		}
	}
	@Override
	public void checkAccess(ThreadGroup g) {
		if (RobocodeProperties.isSecurityOff()) {
			return;
		}
		Thread c = Thread.currentThread();
		if (isSafeThread(c)) {
			return;
		}
		super.checkAccess(g);
		final ThreadGroup cg = c.getThreadGroup();
		if (cg == null) {
			return;
		}
		if ("SeedGenerator Thread".equals(c.getName()) && "SeedGenerator ThreadGroup".equals(cg.getName())) {
			return; 
		}
		IHostedThread robotProxy = threadManager.getLoadedOrLoadingRobotProxy(c);
		if (robotProxy == null) {
			throw new AccessControlException("Preventing " + c.getName() + " from access to " + g.getName());			
		}
		if (cg.activeCount() > 5) {
			String message = "Robots are only allowed to create up to 5 threads!";
			robotProxy.punishSecurityViolation(message);
			throw new SecurityException(message);
		}
	}
    public void checkPermission(Permission perm) {
		if (RobocodeProperties.isSecurityOff()) {
			return;
		}
		Thread c = Thread.currentThread();
		if (isSafeThread(c)) {
			return;
		}
        super.checkPermission(perm);
        if (perm instanceof SocketPermission) {
    		IHostedThread robotProxy = threadManager.getLoadedOrLoadingRobotProxy(c);
        	String message = "Using socket is not allowed";
        	robotProxy.punishSecurityViolation(message);
            throw new SecurityException(message);
        }
    }
	private boolean isSafeThread(Thread c) {
		return threadManager.isSafeThread(c);
	}
}
