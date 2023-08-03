
package hudson.slaves;
import hudson.Functions;
import hudson.model.Computer;
import hudson.model.User;
import org.jvnet.localizer.Localizable;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.export.Exported;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Date;
@ExportedBean
public abstract class OfflineCause {
    protected final long timestamp = System.currentTimeMillis();
    @Exported
    public long getTimestamp() {
        return timestamp;
    }
    public final @Nonnull Date getTime() {
        return new Date(timestamp);
    }
    public static class SimpleOfflineCause extends OfflineCause {
        public final Localizable description;
        protected SimpleOfflineCause(Localizable description) {
            this.description = description;
        }
        @Exported(name="description") @Override
        public String toString() {
            return description.toString();
        }
    }
    public static OfflineCause create(Localizable d) {
        if (d==null)    return null;
        return new SimpleOfflineCause(d);
    }
    public static class ChannelTermination extends OfflineCause {
        @Exported
        public final Exception cause;
        public ChannelTermination(Exception cause) {
            this.cause = cause;
        }
        public String getShortDescription() {
            return cause.toString();
        }
        @Override public String toString() {
            return Messages.OfflineCause_connection_was_broken_(Functions.printThrowable(cause));
        }
    }
    public static class LaunchFailed extends OfflineCause {
        @Override
        public String toString() {
            return Messages.OfflineCause_LaunchFailed();
        }
    }
    public static class UserCause extends SimpleOfflineCause {
        @Deprecated
        private transient User user;
        private  @CheckForNull String userId;
        public UserCause(@CheckForNull User user, @CheckForNull String message) {
            this(
                    user != null ? user.getId() : null,
                    message != null ? " : " + message : ""
            );
        }
        private UserCause(String userId, String message) {
            super(hudson.slaves.Messages._SlaveComputer_DisconnectedBy(userId, message));
            this.userId = userId;
        }
        public User getUser() {
            return userId == null
                    ? User.getUnknown()
                    : User.getById(userId, true)
            ;
        }
        @SuppressWarnings("deprecation")
        private Object readResolve() throws ObjectStreamException {
            if (user != null) {
                String id = user.getId();
                if (id != null) {
                    userId = id;
                } else {
                    User user = User.get(this.user.getFullName(), true, Collections.emptyMap());
                    userId = user.getId();
                }
                this.user = null;
            }
            return this;
        }
    }
    public static class ByCLI extends UserCause {
        @Exported
        public final String message;
        public ByCLI(String message) {
            super(User.current(), message);
            this.message = message;
        }
    }
}
