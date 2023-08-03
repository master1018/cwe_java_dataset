
package org.platformlambda.example;
import org.platformlambda.core.annotations.MainApplication;
import org.platformlambda.core.models.EntryPoint;
import org.platformlambda.core.models.LambdaFunction;
import org.platformlambda.core.system.AppStarter;
import org.platformlambda.core.system.Platform;
import org.platformlambda.services.HelloGeneric;
import org.platformlambda.services.HelloPoJo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
@MainApplication
public class MainApp implements EntryPoint {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    public static void main(String[] args) {
        AppStarter.main(args);
    }
    @Override
    public void start(String[] args) throws Exception {
        Platform platform = Platform.getInstance();
        LambdaFunction echo = (headers, body, instance) -> {
            log.info("echo @"+instance+" received - "+headers+", "+body);
            Map<String, Object> result = new HashMap<>();
            result.put("headers", headers);
            result.put("body", body);
            result.put("instance", instance);
            result.put("origin", platform.getOrigin());
            return result;
        };
        platform.register("hello.world", echo, 10);
        platform.register("hello.pojo", new HelloPoJo(), 5);
        platform.register("hello.generic", new HelloGeneric(), 5);
        platform.connectToCloud();
    }
}
