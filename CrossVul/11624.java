
package org.platformlambda.services;
import org.platformlambda.core.exception.AppException;
import org.platformlambda.core.models.LambdaFunction;
import org.platformlambda.core.system.Platform;
import org.platformlambda.models.SamplePoJo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
public class HelloPoJo implements LambdaFunction {
    private static final Logger log = LoggerFactory.getLogger(HelloPoJo.class);
    @Override
    public Object handleEvent(Map<String, String> headers, Object body, int instance) throws AppException, IOException {
        String id = headers.get("id");
        if (id == null) {
            throw new IllegalArgumentException("Missing parameter 'id'");
        }
        if (id.equals("1")) {
            SamplePoJo mock = new SamplePoJo(1, "Simple PoJo class", "100 World Blvd, Planet Earth");
            mock.setDate(new Date());
            mock.setInstance(instance);
            mock.setOrigin(Platform.getInstance().getOrigin());
            log.info("Pojo delivered by instance #{}", instance);
            return mock;
        } else {
            throw new AppException(404, "Not found. Try id = 1");
        }
    }
}
