
package org.openhab.binding.exec.internal;
import static org.openhab.binding.exec.internal.ExecBindingConstants.THING_COMMAND;
import java.util.Collections;
import java.util.Set;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.exec.internal.handler.ExecHandler;
import org.osgi.service.component.annotations.Component;
@NonNullByDefault
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.exec")
public class ExecHandlerFactory extends BaseThingHandlerFactory {
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_COMMAND);
    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }
    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(THING_COMMAND)) {
            return new ExecHandler(thing);
        }
        return null;
    }
}
