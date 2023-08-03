
package org.openhab.binding.exec.internal;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.exec.internal.handler.ExecHandler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.Set;
import static org.openhab.binding.exec.internal.ExecBindingConstants.THING_COMMAND;
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.exec")
public class ExecHandlerFactory extends BaseThingHandlerFactory {
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_COMMAND);
    private final Logger logger = LoggerFactory.getLogger(ExecHandlerFactory.class);
    private final ExecWhitelistWatchService execWhitelistWatchService;
    @Activate
    public ExecHandlerFactory(@Reference ExecWhitelistWatchService execWhitelistWatchService) {
        this.execWhitelistWatchService = execWhitelistWatchService;
    }
    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }
    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(THING_COMMAND)) {
            return new ExecHandler(thing, execWhitelistWatchService);
        }
        return null;
    }
}
