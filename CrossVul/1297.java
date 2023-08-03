
package com.vmware.xenon.common;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.junit.Before;
import org.junit.Test;
import com.vmware.xenon.common.Service.ServiceOption;
import com.vmware.xenon.common.ServiceStats.ServiceStat;
import com.vmware.xenon.common.ServiceStats.TimeSeriesStats;
import com.vmware.xenon.common.ServiceStats.TimeSeriesStats.AggregationType;
import com.vmware.xenon.common.ServiceStats.TimeSeriesStats.TimeBin;
import com.vmware.xenon.common.test.TestContext;
import com.vmware.xenon.services.common.ExampleService;
import com.vmware.xenon.services.common.ExampleService.ExampleServiceState;
import com.vmware.xenon.services.common.MinimalTestService;
public class TestUtilityService extends BasicReusableHostTestCase {
    private List<Service> createServices(int count) throws Throwable {
        List<Service> services = this.host.doThroughputServiceStart(
                count, MinimalTestService.class,
                this.host.buildMinimalTestState(),
                null, null);
        return services;
    }
    @Before
    public void setUp() {
        this.host.setSingleton(true);
    }
    @Test
    public void patchConfiguration() throws Throwable {
        int count = 10;
        host.waitForServiceAvailable(ExampleService.FACTORY_LINK);
        ServiceConfigUpdateRequest updateBody = ServiceConfigUpdateRequest.create();
        updateBody.removeOptions = EnumSet.of(ServiceOption.IDEMPOTENT_POST);
        TestContext ctx = this.testCreate(1);
        URI configUri = UriUtils.buildConfigUri(host, ExampleService.FACTORY_LINK);
        this.host.send(Operation.createPatch(configUri).setBody(updateBody)
                .setCompletion(ctx.getCompletion()));
        this.testWait(ctx);
        TestContext ctx2 = this.testCreate(1);
        this.host.send(Operation.createGet(configUri).setCompletion((o, e) -> {
            if (e != null) {
                ctx2.failIteration(e);
                return;
            }
            ServiceConfiguration cfg = o.getBody(ServiceConfiguration.class);
            if (!cfg.options.contains(ServiceOption.IDEMPOTENT_POST)) {
                ctx2.completeIteration();
            } else {
                ctx2.failIteration(new IllegalStateException(Utils.toJsonHtml(cfg)));
            }
        }));
        this.testWait(ctx2);
        List<Service> services = createServices(count);
        for (Service s : services) {
            Map<String, ServiceStat> stats = this.host.getServiceStats(s.getUri());
            assertTrue(stats != null);
            assertTrue(stats.isEmpty());
        }
        updateBody = ServiceConfigUpdateRequest.create();
        updateBody.addOptions = EnumSet.of(ServiceOption.INSTRUMENTATION);
        ctx = this.testCreate(services.size());
        for (Service s : services) {
            configUri = UriUtils.buildConfigUri(s.getUri());
            this.host.send(Operation.createPatch(configUri).setBody(updateBody)
                    .setCompletion(ctx.getCompletion()));
        }
        this.testWait(ctx);
        TestContext ctx3 = testCreate(services.size());
        for (Service s : services) {
            URI u = UriUtils.buildConfigUri(s.getUri());
            host.send(Operation.createGet(u).setCompletion((o, e) -> {
                if (e != null) {
                    ctx3.failIteration(e);
                    return;
                }
                ServiceConfiguration cfg = o.getBody(ServiceConfiguration.class);
                if (cfg.options.contains(ServiceOption.INSTRUMENTATION)) {
                    ctx3.completeIteration();
                } else {
                    ctx3.failIteration(new IllegalStateException(Utils.toJsonHtml(cfg)));
                }
            }));
        }
        testWait(ctx3);
        ctx = testCreate(services.size());
        for (Service s : services) {
            this.host.send(Operation.createPatch(s.getUri())
                    .setBody(this.host.buildMinimalTestState())
                    .setCompletion(ctx.getCompletion()));
        }
        testWait(ctx);
        for (Service s : services) {
            Map<String, ServiceStat> stats = this.host.getServiceStats(s.getUri());
            assertTrue(stats != null);
            assertTrue(!stats.isEmpty());
        }
    }
    @Test
    public void redirectToUiServiceIndex() throws Throwable {
        ExampleServiceState s = new ExampleServiceState();
        s.name = UUID.randomUUID().toString();
        s.documentSelfLink = s.name;
        Operation post = Operation
                .createPost(UriUtils.buildFactoryUri(this.host, ExampleService.class))
                .setBody(s);
        this.host.sendAndWaitExpectSuccess(post);
        for (int i = 0; i < 2; i++) {
            Operation htmlResponse = this.host.sendUIHttpRequest(
                    UriUtils.buildUri(
                            this.host,
                            UriUtils.buildUriPath(ExampleService.FACTORY_LINK,
                                    ServiceHost.SERVICE_URI_SUFFIX_UI))
                            .toString(), null, 1);
            validateServiceUiHtmlResponse(htmlResponse);
            htmlResponse = this.host.sendUIHttpRequest(
                    UriUtils.buildUri(
                            this.host,
                            UriUtils.buildUriPath(ExampleService.FACTORY_LINK, s.name,
                                    ServiceHost.SERVICE_URI_SUFFIX_UI))
                            .toString(), null, 1);
            validateServiceUiHtmlResponse(htmlResponse);
        }
    }
    @Test
    public void testUtilityStats() throws Throwable {
        String name = UUID.randomUUID().toString();
        ExampleServiceState s = new ExampleServiceState();
        s.name = name;
        Consumer<Operation> bodySetter = (o) -> {
            o.setBody(s);
        };
        URI factoryURI = UriUtils.buildFactoryUri(this.host, ExampleService.class);
        long c = 2;
        Map<URI, ExampleServiceState> states = this.host.doFactoryChildServiceStart(null, c,
                ExampleServiceState.class, bodySetter, factoryURI);
        ExampleServiceState exampleServiceState = states.values().iterator().next();
        ServiceStats.ServiceStat stat = new ServiceStat();
        stat.name = "key1";
        stat.latestValue = 100;
        stat.unit = "unit";
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        ServiceStats allStats = this.host.getServiceState(null, ServiceStats.class,
                UriUtils.buildStatsUri(
                        this.host, exampleServiceState.documentSelfLink));
        ServiceStat retStatEntry = allStats.entries.get("key1");
        assertTrue(retStatEntry.accumulatedValue == 100);
        assertTrue(retStatEntry.latestValue == 100);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.unit.equals("unit"));
        assertTrue(retStatEntry.sourceTimeMicrosUtc == null);
        stat.latestValue = 50;
        stat.unit = "unit1";
        Long updatedMicrosUtc1 = Utils.getNowMicrosUtc();
        stat.sourceTimeMicrosUtc = updatedMicrosUtc1;
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        allStats = this.host.getServiceState(null, ServiceStats.class, UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink));
        retStatEntry = allStats.entries.get("key1");
        assertTrue(retStatEntry.accumulatedValue == 150);
        assertTrue(retStatEntry.latestValue == 50);
        assertTrue(retStatEntry.version == 2);
        assertTrue(retStatEntry.unit.equals("unit1"));
        assertTrue(retStatEntry.sourceTimeMicrosUtc == updatedMicrosUtc1);
        stat.name = "key2";
        stat.latestValue = 50;
        stat.unit = "unit2";
        Long updatedMicrosUtc2 = Utils.getNowMicrosUtc();
        stat.sourceTimeMicrosUtc = updatedMicrosUtc2;
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        allStats = this.host.getServiceState(null, ServiceStats.class, UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink));
        retStatEntry = allStats.entries.get("key1");
        assertTrue(retStatEntry.accumulatedValue == 150);
        assertTrue(retStatEntry.latestValue == 50);
        assertTrue(retStatEntry.version == 2);
        assertTrue(retStatEntry.unit.equals("unit1"));
        assertTrue(retStatEntry.sourceTimeMicrosUtc == updatedMicrosUtc1);
        retStatEntry = allStats.entries.get("key2");
        assertTrue(retStatEntry.accumulatedValue == 50);
        assertTrue(retStatEntry.latestValue == 50);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.unit.equals("unit2"));
        assertTrue(retStatEntry.sourceTimeMicrosUtc == updatedMicrosUtc2);
        stat.name = "key1";
        stat.latestValue = 75;
        stat.unit = "replaceUnit";
        stat.sourceTimeMicrosUtc = null;
        this.host.sendAndWaitExpectSuccess(Operation.createPut(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        allStats = this.host.getServiceState(null, ServiceStats.class, UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink));
        retStatEntry = allStats.entries.get("key1");
        assertTrue(retStatEntry.accumulatedValue == 75);
        assertTrue(retStatEntry.latestValue == 75);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.unit.equals("replaceUnit"));
        assertTrue(retStatEntry.sourceTimeMicrosUtc == null);
        ServiceStats stats = new ServiceStats();
        stat.name = "key3";
        stat.latestValue = 200;
        stat.unit = "unit3";
        stats.entries.put("key3", stat);
        this.host.sendAndWaitExpectSuccess(Operation.createPut(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stats));
        allStats = this.host.getServiceState(null, ServiceStats.class, UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink));
        if (allStats.entries.size() != 1) {
            ServiceStat nodeGroupStat = allStats.entries.get(
                    Service.STAT_NAME_NODE_GROUP_CHANGE_MAINTENANCE_COUNT);
            if (nodeGroupStat == null) {
                throw new IllegalStateException(
                        "Expected single stat, got: " + Utils.toJsonHtml(allStats));
            }
        }
        retStatEntry = allStats.entries.get("key3");
        assertTrue(retStatEntry.accumulatedValue == 200);
        assertTrue(retStatEntry.latestValue == 200);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.unit.equals("unit3"));
        stat.latestValue = 25;
        this.host.sendAndWaitExpectSuccess(Operation.createPatch(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        allStats = this.host.getServiceState(null, ServiceStats.class, UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink));
        retStatEntry = allStats.entries.get("key3");
        assertTrue(retStatEntry.latestValue == 225);
        assertTrue(retStatEntry.version == 2);
    }
    @Test
    public void testTimeSeriesStats() throws Throwable {
        long startTime = TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
        int numBins = 4;
        long interval = 1000;
        double value = 100;
        TimeSeriesStats timeSeriesStats = new TimeSeriesStats(numBins, interval, EnumSet.allOf(AggregationType.class));
        for (int i = 0; i < numBins; i++) {
            startTime += TimeUnit.MILLISECONDS.toMicros(interval);
            value += 1;
            timeSeriesStats.add(startTime, value);
        }
        assertTrue(timeSeriesStats.bins.size() == numBins);
        for (int i = 0; i < numBins / 2; i++) {
            startTime += TimeUnit.MILLISECONDS.toMicros(interval);
            value += 1;
            timeSeriesStats.add(startTime, value);
        }
        assertTrue(timeSeriesStats.bins.size() == numBins);
        long timeMicros = startTime - TimeUnit.MILLISECONDS.toMicros(interval * (numBins - 1));
        long timeMillis = TimeUnit.MICROSECONDS.toMillis(timeMicros);
        timeMillis -= (timeMillis % interval);
        assertTrue(timeSeriesStats.bins.firstKey() == timeMillis);
        double origValue = value;
        double accumulatedValue = value;
        double newValue = value;
        double count = 1;
        for (int i = 0; i < numBins / 2; i++) {
            newValue++;
            count++;
            timeSeriesStats.add(startTime, newValue);
            accumulatedValue += newValue;
        }
        TimeBin lastBin = timeSeriesStats.bins.get(timeSeriesStats.bins.lastKey());
        assertTrue(lastBin.avg.equals(accumulatedValue / count));
        assertTrue(lastBin.sum.equals(accumulatedValue));
        assertTrue(lastBin.count == count);
        assertTrue(lastBin.max.equals(newValue));
        assertTrue(lastBin.min.equals(origValue));
        timeSeriesStats = new TimeSeriesStats(numBins, interval, EnumSet.of(AggregationType.AVG));
        timeSeriesStats.add(startTime, value);
        lastBin = timeSeriesStats.bins.get(timeSeriesStats.bins.lastKey());
        assertTrue(lastBin.avg != null);
        assertTrue(lastBin.count != 0);
        assertTrue(lastBin.max == null);
        assertTrue(lastBin.min == null);
        timeSeriesStats = new TimeSeriesStats(numBins, interval, EnumSet.of(AggregationType.MIN, AggregationType.MAX));
        timeSeriesStats.add(startTime, value);
        lastBin = timeSeriesStats.bins.get(timeSeriesStats.bins.lastKey());
        assertTrue(lastBin.avg == null);
        assertTrue(lastBin.count == 0);
        assertTrue(lastBin.max != null);
        assertTrue(lastBin.min != null);
        String name = UUID.randomUUID().toString();
        ExampleServiceState s = new ExampleServiceState();
        s.name = name;
        Consumer<Operation> bodySetter = (o) -> {
            o.setBody(s);
        };
        URI factoryURI = UriUtils.buildFactoryUri(this.host, ExampleService.class);
        Map<URI, ExampleServiceState> states = this.host.doFactoryChildServiceStart(null, 1,
                ExampleServiceState.class, bodySetter, factoryURI);
        ExampleServiceState exampleServiceState = states.values().iterator().next();
        ServiceStats.ServiceStat stat = new ServiceStat();
        stat.name = "key1";
        stat.latestValue = 100;
        stat.timeSeriesStats = new TimeSeriesStats(numBins, 1, EnumSet.allOf(AggregationType.class));
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        for (int i = 0; i < numBins; i++) {
            Thread.sleep(1);
            this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                    this.host, exampleServiceState.documentSelfLink)).setBody(stat));
        }
        ServiceStats allStats = this.host.getServiceState(null, ServiceStats.class,
                UriUtils.buildStatsUri(
                        this.host, exampleServiceState.documentSelfLink));
        ServiceStat retStatEntry = allStats.entries.get(stat.name);
        assertTrue(retStatEntry.accumulatedValue == 100 * (numBins + 1));
        assertTrue(retStatEntry.latestValue == 100);
        assertTrue(retStatEntry.version == numBins + 1);
        assertTrue(retStatEntry.timeSeriesStats.bins.size() == numBins);
        String statName = UUID.randomUUID().toString();
        ExampleServiceState exampleState = new ExampleServiceState();
        exampleState.name = statName;
        Consumer<Operation> setter = (o) -> {
            o.setBody(exampleState);
        };
        Map<URI, ExampleServiceState> stateMap = this.host.doFactoryChildServiceStart(null, 1,
                ExampleServiceState.class, setter,
                UriUtils.buildFactoryUri(this.host, ExampleService.class));
        ExampleServiceState returnExampleState = stateMap.values().iterator().next();
        ServiceStats.ServiceStat sourceStat1 = new ServiceStat();
        sourceStat1.name = "sourceKey1";
        sourceStat1.latestValue = 100;
        Long sourceTimeMicrosUtc1 = 946713600000000L;
        sourceStat1.sourceTimeMicrosUtc = sourceTimeMicrosUtc1;
        ServiceStats.ServiceStat sourceStat2 = new ServiceStat();
        sourceStat2.name = "sourceKey2";
        sourceStat2.latestValue = 100;
        Long sourceTimeMicrosUtc2 = 946800000000000L;
        sourceStat2.sourceTimeMicrosUtc = sourceTimeMicrosUtc2;
        sourceStat1.timeSeriesStats = new TimeSeriesStats(numBins, 1, EnumSet.allOf(AggregationType.class));
        sourceStat2.timeSeriesStats = new TimeSeriesStats(numBins, 1, EnumSet.allOf(AggregationType.class));
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, returnExampleState.documentSelfLink)).setBody(sourceStat1));
        this.host.sendAndWaitExpectSuccess(Operation.createPost(UriUtils.buildStatsUri(
                this.host, returnExampleState.documentSelfLink)).setBody(sourceStat2));
        allStats = this.host.getServiceState(null, ServiceStats.class,
                UriUtils.buildStatsUri(
                        this.host, returnExampleState.documentSelfLink));
        retStatEntry = allStats.entries.get(sourceStat1.name);
        assertTrue(retStatEntry.accumulatedValue == 100);
        assertTrue(retStatEntry.latestValue == 100);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.timeSeriesStats.bins.size() == 1);
        assertTrue(retStatEntry.timeSeriesStats.bins.firstKey()
                .equals(TimeUnit.MICROSECONDS.toMillis(sourceTimeMicrosUtc1)));
        retStatEntry = allStats.entries.get(sourceStat2.name);
        assertTrue(retStatEntry.accumulatedValue == 100);
        assertTrue(retStatEntry.latestValue == 100);
        assertTrue(retStatEntry.version == 1);
        assertTrue(retStatEntry.timeSeriesStats.bins.size() == 1);
        assertTrue(retStatEntry.timeSeriesStats.bins.firstKey()
                .equals(TimeUnit.MICROSECONDS.toMillis(sourceTimeMicrosUtc2)));
    }
    public static class SetAvailableValidationService extends StatefulService {
        public SetAvailableValidationService() {
            super(ExampleServiceState.class);
        }
        @Override
        public void handleStart(Operation op) {
            setAvailable(false);
            op.complete();
        }
        @Override
        public void handlePatch(Operation op) {
            setAvailable(true);
            op.complete();
        }
    }
    @Test
    public void failureOnReservedSuffixServiceStart() throws Throwable {
        TestContext ctx = this.testCreate(ServiceHost.RESERVED_SERVICE_URI_PATHS.length);
        for (String reservedSuffix : ServiceHost.RESERVED_SERVICE_URI_PATHS) {
            Operation post = Operation.createPost(this.host,
                    UUID.randomUUID().toString() + "/" + reservedSuffix)
                    .setCompletion(ctx.getExpectedFailureCompletion());
            this.host.startService(post, new MinimalTestService());
        }
        this.testWait(ctx);
    }
    @Test
    public void testIsAvailableStatAndSuffix() throws Throwable {
        long c = 1;
        URI factoryURI = UriUtils.buildFactoryUri(this.host, ExampleService.class);
        String name = UUID.randomUUID().toString();
        ExampleServiceState s = new ExampleServiceState();
        s.name = name;
        Consumer<Operation> bodySetter = (o) -> {
            o.setBody(s);
        };
        Map<URI, ExampleServiceState> states = this.host.doFactoryChildServiceStart(null, c,
                ExampleServiceState.class, bodySetter, factoryURI);
        this.host.waitForServiceAvailable(factoryURI);
        TestContext ctx = testCreate(states.size());
        for (URI u : states.keySet()) {
            Operation get = Operation.createGet(UriUtils.buildAvailableUri(u))
                    .setCompletion(ctx.getCompletion());
            this.host.send(get);
        }
        testWait(ctx);
        ServiceStat body = new ServiceStat();
        body.name = Service.STAT_NAME_AVAILABLE;
        body.latestValue = 0.0;
        Operation put = Operation.createPut(
                UriUtils.buildAvailableUri(this.host, factoryURI.getPath()))
                .setBody(body);
        this.host.sendAndWaitExpectSuccess(put);
        Operation get = Operation.createGet(UriUtils.buildAvailableUri(factoryURI));
        this.host.sendAndWaitExpectFailure(get);
        ctx = testCreate(states.size());
        for (URI u : states.keySet()) {
            put = put.clone().setUri(UriUtils.buildAvailableUri(u))
                    .setBody(body)
                    .setCompletion(ctx.getCompletion());
            this.host.send(put);
        }
        testWait(ctx);
        ctx = testCreate(states.size());
        for (URI u : states.keySet()) {
            get = get.clone().setUri(UriUtils.buildAvailableUri(u))
                    .setCompletion(ctx.getExpectedFailureCompletion());
            this.host.send(get);
        }
        testWait(ctx);
        Service service = this.host.startServiceAndWait(new SetAvailableValidationService(),
                UUID.randomUUID().toString(), new ExampleServiceState());
        get = Operation.createGet(UriUtils.buildAvailableUri(service.getUri()));
        this.host.sendAndWaitExpectFailure(get);
        Operation patch = Operation.createPatch(service.getUri())
                .setBody(new ExampleServiceState());
        this.host.sendAndWaitExpectSuccess(patch);
        get = Operation.createGet(UriUtils.buildAvailableUri(service.getUri()));
        this.host.sendAndWaitExpectSuccess(get);
    }
    public void validateServiceUiHtmlResponse(Operation op) {
        assertTrue(op.getStatusCode() == Operation.STATUS_CODE_MOVED_TEMP);
        assertTrue(op.getResponseHeader("Location").contains(
                "/core/ui/default/#"));
    }
    public static void validateTimeSeriesStat(ServiceStat stat, long expectedBinDurationMillis) {
        assertTrue(stat != null);
        assertTrue(stat.timeSeriesStats != null);
        assertTrue(stat.version > 1);
        assertEquals(expectedBinDurationMillis, stat.timeSeriesStats.binDurationMillis);
        double maxAvg = 0;
        double countPerMaxAvgBin = 0;
        for (TimeBin bin : stat.timeSeriesStats.bins.values()) {
            if (bin.avg != null && bin.avg > maxAvg) {
                maxAvg = bin.avg;
                countPerMaxAvgBin = bin.count;
            }
        }
        assertTrue(maxAvg > 0);
        assertTrue(countPerMaxAvgBin >= 1);
    }
}
