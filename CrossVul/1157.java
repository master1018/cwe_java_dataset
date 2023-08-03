
package org.openhab.binding.exec.internal;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
@NonNullByDefault
public class ExecBindingConstants {
    public static final String BINDING_ID = "exec";
    public static final ThingTypeUID THING_COMMAND = new ThingTypeUID(BINDING_ID, "command");
    public static final String OUTPUT = "output";
    public static final String INPUT = "input";
    public static final String EXIT = "exit";
    public static final String RUN = "run";
    public static final String LAST_EXECUTION = "lastexecution";
}
