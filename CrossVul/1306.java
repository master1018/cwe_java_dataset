
package com.vmware.xenon.common;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.After;
import org.junit.Test;
import com.vmware.xenon.common.Service.Action;
import com.vmware.xenon.common.ServiceSubscriptionState.ServiceSubscriber;
import com.vmware.xenon.common.http.netty.NettyHttpServiceClient;
import com.vmware.xenon.common.test.MinimalTestServiceState;
import com.vmware.xenon.common.test.TestContext;
import com.vmware.xenon.common.test.VerificationHost;
import com.vmware.xenon.services.common.ExampleService;
import com.vmware.xenon.services.common.ExampleService.ExampleServiceState;
import com.vmware.xenon.services.common.MinimalTestService;
import com.vmware.xenon.services.common.NodeGroupService.NodeGroupConfig;
import com.vmware.xenon.services.common.ServiceUriPaths;
public class TestSubscriptions extends BasicTestCase {
    private final int NODE_COUNT = 2;
    public int serviceCount = 100;
    public long updateCount = 10;
    public long iterationCount = 0;
    public void beforeHostStart(VerificationHost host) {
        host.setMaintenanceIntervalMicros(TimeUnit.MILLISECONDS
                .toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS));
    }
    @After
    public void tearDown() {
        this.host.tearDown();
        this.host.tearDownInProcessPeers();
    }
    private void setUpPeers() throws Throwable {
        this.host.setUpPeerHosts(this.NODE_COUNT);
        this.host.joinNodesAndVerifyConvergence(this.NODE_COUNT);
    }
    @Test
    public void remoteAndReliableSubscriptionsLoop() throws Throwable {
        for (int i = 0; i < this.iterationCount; i++) {
            tearDown();
            this.host = createHost();
            initializeHost(this.host);
            beforeHostStart(this.host);
            this.host.start();
            remoteAndReliableSubscriptions();
        }
    }
    @Test
    public void remoteAndReliableSubscriptions() throws Throwable {
        setUpPeers();
        VerificationHost serviceHost = this.host.getPeerHost();
        URI factoryUri = UriUtils.buildUri(serviceHost, ExampleService.FACTORY_LINK);
        this.host.waitForReplicatedFactoryServiceAvailable(factoryUri);
        VerificationHost localHost = this.host;
        int serviceCount = 1;
        List<URI> exampleURIs = new ArrayList<>();
        serviceHost.createExampleServices(serviceHost, serviceCount, exampleURIs, null);
        TestContext oneUseNotificationCtx = this.host.testCreate(1);
        StatelessService notificationTarget = new StatelessService() {
            @Override
            public void handleRequest(Operation update) {
                update.complete();
                if (update.getAction().equals(Action.PATCH)) {
                    if (update.getUri().getHost() == null) {
                        oneUseNotificationCtx.fail(new IllegalStateException(
                                "Notification URI does not have host specified"));
                        return;
                    }
                    oneUseNotificationCtx.complete();
                }
            }
        };
        String[] ownerHostId = new String[1];
        URI uri = exampleURIs.get(0);
        URI subUri = UriUtils.buildUri(serviceHost.getUri(), uri.getPath());
        TestContext subscribeCtx = this.host.testCreate(1);
        Operation subscribe = Operation.createPost(subUri)
                .setCompletion(subscribeCtx.getCompletion());
        subscribe.setReferer(localHost.getReferer());
        subscribe.forceRemote();
        serviceHost.startSubscriptionService(subscribe, notificationTarget, ServiceSubscriber
                .create(false).setUsePublicUri(true));
        this.host.testWait(subscribeCtx);
        TestContext updateCtx = this.host.testCreate(1);
        ExampleServiceState body = new ExampleServiceState();
        body.name = UUID.randomUUID().toString();
        this.host.send(Operation.createPatch(uri).setBody(body).setCompletion((o, e) -> {
            if (e != null) {
                updateCtx.fail(e);
                return;
            }
            ExampleServiceState rsp = o.getBody(ExampleServiceState.class);
            ownerHostId[0] = rsp.documentOwner;
            updateCtx.complete();
        }));
        this.host.testWait(updateCtx);
        this.host.testWait(oneUseNotificationCtx);
        TestContext unSubscribeCtx = this.host.testCreate(1);
        Operation unSubscribe = subscribe.clone()
                .setCompletion(unSubscribeCtx.getCompletion())
                .setAction(Action.DELETE);
        serviceHost.stopSubscriptionService(unSubscribe,
                notificationTarget.getUri());
        this.host.testWait(unSubscribeCtx);
        this.verifySubscriberCount(new URI[] { uri }, 0);
        VerificationHost ownerHost = null;
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            if (!h.getId().equals(ownerHostId[0])) {
                serviceHost = h;
            } else {
                ownerHost = h;
            }
        }
        this.host.log("Owner node: %s, subscriber node: %s (%s)", ownerHostId[0],
                serviceHost.getId(), serviceHost.getUri());
        AtomicInteger reliableNotificationCount = new AtomicInteger();
        TestContext subscribeCtxNonOwner = this.host.testCreate(1);
        subscribe.setCompletion(subscribeCtxNonOwner.getCompletion());
        serviceHost.startReliableSubscriptionService(subscribe, (o) -> {
            reliableNotificationCount.incrementAndGet();
            o.complete();
        });
        localHost.testWait(subscribeCtxNonOwner);
        body.name = UUID.randomUUID().toString();
        this.host.send(Operation.createPatch(uri).setBody(body));
        while (reliableNotificationCount.get() < 1) {
            Thread.sleep(100);
        }
        reliableNotificationCount.set(0);
        this.verifySubscriberCount(new URI[] { uri }, 1);
        List<URI> exampleSubUris = new ArrayList<>();
        for (URI hostUri : this.host.getNodeGroupMap().keySet()) {
            exampleSubUris.add(UriUtils.buildUri(hostUri, uri.getPath(),
                    ServiceHost.SERVICE_URI_SUFFIX_SUBSCRIPTIONS));
        }
        NodeGroupConfig cfg = new NodeGroupConfig();
        cfg.nodeRemovalDelayMicros = TimeUnit.SECONDS.toMicros(2);
        this.host.setNodeGroupConfig(cfg);
        this.host.setNodeGroupQuorum(1);
        this.host.stopHost(ownerHost);
        factoryUri = UriUtils.buildUri(serviceHost, ExampleService.FACTORY_LINK);
        this.host.waitForReplicatedFactoryServiceAvailable(factoryUri);
        uri = UriUtils.buildUri(serviceHost.getUri(), uri.getPath());
        this.verifySubscriberCount(new URI[] { uri }, 1);
        this.host.log("Sending PATCH requests to %s", uri);
        long c = this.updateCount;
        for (int i = 0; i < c; i++) {
            body.name = "post-stop-" + UUID.randomUUID().toString();
            this.host.send(Operation.createPatch(uri).setBody(body));
        }
        Date exp = this.host.getTestExpiration();
        while (reliableNotificationCount.get() < c) {
            Thread.sleep(250);
            this.host.log("Received %d notifications, expecting %d",
                    reliableNotificationCount.get(), c);
            if (new Date().after(exp)) {
                throw new TimeoutException();
            }
        }
    }
    @Test
    public void subscriptionsToFactoryAndChildren() throws Throwable {
        this.host.stop();
        this.host.setPort(0);
        this.host.start();
        this.host.setPublicUri(UriUtils.buildUri("localhost", this.host.getPort(), "", null));
        this.host.waitForServiceAvailable(ExampleService.FACTORY_LINK);
        URI factoryUri = UriUtils.buildFactoryUri(this.host, ExampleService.class);
        String prefix = "example-";
        Long counterValue = Long.MAX_VALUE;
        URI[] childUris = new URI[this.serviceCount];
        doFactoryPostNotifications(factoryUri, this.serviceCount, prefix, counterValue, childUris);
        doNotificationsWithReplayState(childUris);
        doNotificationsWithFailure(childUris);
        doNotificationsWithLimitAndPublicUri(childUris);
        doNotificationsWithExpiration(childUris);
        doDeleteNotifications(childUris, counterValue);
    }
    @Test
    public void testSubscriptionsWithAuth() throws Throwable {
        VerificationHost hostWithAuth = null;
        try {
            String testUserEmail = "foo@vmware.com";
            hostWithAuth = VerificationHost.create(0);
            hostWithAuth.setAuthorizationEnabled(true);
            hostWithAuth.start();
            hostWithAuth.setSystemAuthorizationContext();
            TestContext waitContext = new TestContext(1, Duration.ofSeconds(5));
            AuthorizationSetupHelper.create()
                    .setHost(hostWithAuth)
                    .setDocumentKind(Utils.buildKind(MinimalTestServiceState.class))
                    .setUserEmail(testUserEmail)
                    .setUserSelfLink(testUserEmail)
                    .setUserPassword(testUserEmail)
                    .setCompletion(waitContext.getCompletion())
                    .start();
            hostWithAuth.testWait(waitContext);
            hostWithAuth.resetSystemAuthorizationContext();
            hostWithAuth.assumeIdentity(UriUtils.buildUriPath(ServiceUriPaths.CORE_AUTHZ_USERS, testUserEmail));
            MinimalTestService s = new MinimalTestService();
            MinimalTestServiceState serviceState = new MinimalTestServiceState();
            serviceState.id = UUID.randomUUID().toString();
            String minimalServiceUUID = UUID.randomUUID().toString();
            TestContext notifyContext = new TestContext(1, Duration.ofSeconds(5));
            hostWithAuth.startServiceAndWait(s, minimalServiceUUID, serviceState);
            Consumer<Operation> notifyC = (nOp) -> {
                nOp.complete();
                switch (nOp.getAction()) {
                case PUT:
                    notifyContext.completeIteration();
                    break;
                default:
                    break;
                }
            };
            Operation subscribe = Operation.createPost(UriUtils.buildUri(hostWithAuth, minimalServiceUUID));
            subscribe.setReferer(hostWithAuth.getReferer());
            ServiceSubscriber subscriber = new ServiceSubscriber();
            subscriber.replayState = true;
            hostWithAuth.startSubscriptionService(subscribe, notifyC, subscriber);
            hostWithAuth.testWait(notifyContext);
        } finally {
            if (hostWithAuth != null) {
                hostWithAuth.tearDown();
            }
        }
    }
    @Test
    public void subscribeAndWaitForServiceAvailability() throws Throwable {
        this.serviceCount = NettyHttpServiceClient.DEFAULT_CONNECTIONS_PER_HOST / 2;
        this.host.getClient().setConnectionLimitPerHost(this.serviceCount * 4);
        setUpPeers();
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.getClient().setConnectionLimitPerHost(this.serviceCount * 4);
        }
        this.host.waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(ExampleService.FACTORY_LINK));
        VerificationHost serviceHost = this.host.getPeerHost();
        List<ExampleServiceState> states = new ArrayList<>();
        for (int i = 0; i < this.serviceCount; i++) {
            ExampleServiceState state = new ExampleServiceState();
            state.documentSelfLink = UriUtils.buildUriPath(
                    ExampleService.FACTORY_LINK,
                    UUID.randomUUID().toString());
            state.name = UUID.randomUUID().toString();
            states.add(state);
        }
        AtomicInteger notifications = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget((update) -> {
            if (update.getAction() != Action.PATCH) {
                return false;
            }
            this.host.completeIteration();
            this.host.log("notification %d", notifications.incrementAndGet());
            update.complete();
            return true;
        });
        this.host.log("Subscribing to %d services", this.serviceCount);
        for (ExampleServiceState state : states) {
            URI uri = UriUtils.buildUri(serviceHost, state.documentSelfLink);
            subscribeToService(uri, sr);
        }
        this.host.testStart(2 * this.serviceCount);
        this.host.log("Sending parallel POST for %d services", this.serviceCount);
        AtomicInteger postCount = new AtomicInteger();
        for (ExampleServiceState state : states) {
            URI uri = UriUtils.buildFactoryUri(serviceHost, ExampleService.class);
            Operation op = Operation.createPost(uri)
                    .setBody(state)
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            this.host.failIteration(e);
                            return;
                        }
                        this.host.log("POST count %d", postCount.incrementAndGet());
                        this.host.completeIteration();
                    });
            this.host.send(op);
        }
        this.host.testWait();
        this.host.testStart(2 * this.serviceCount);
        for (ExampleServiceState state : states) {
            URI u = UriUtils.buildUri(serviceHost, state.documentSelfLink);
            state.counter = Utils.getNowMicrosUtc();
            Operation patch = Operation.createPatch(u)
                    .setBody(state)
                    .setCompletion(this.host.getCompletion());
            this.host.send(patch);
        }
        this.host.testWait();
    }
    private void doFactoryPostNotifications(URI factoryUri, int childCount, String prefix,
            Long counterValue,
            URI[] childUris) throws Throwable {
        this.host.log("starting subscription to factory");
        this.host.testStart(1);
        Operation subscribeOp = Operation.createPost(factoryUri)
                .setReferer(this.host.getReferer())
                .setCompletion(this.host.getCompletion());
        URI notificationTarget = host.startSubscriptionService(subscribeOp, (o) -> {
            if (o.getAction() == Action.POST) {
                this.host.completeIteration();
            } else {
                this.host.failIteration(new IllegalStateException("Unexpected notification: "
                        + o.toString()));
            }
        });
        this.host.testWait();
        this.host.testStart(childCount * 2);
        for (int i = 0; i < childCount; i++) {
            ExampleServiceState initialState = new ExampleServiceState();
            initialState.name = initialState.documentSelfLink = prefix + i;
            initialState.counter = counterValue;
            final int finalI = i;
            this.host.send(Operation
                    .createPost(factoryUri)
                    .setBody(initialState).setCompletion((o, e) -> {
                        if (e != null) {
                            this.host.failIteration(e);
                            return;
                        }
                        ServiceDocument rsp = o.getBody(ServiceDocument.class);
                        childUris[finalI] = UriUtils.buildUri(this.host, rsp.documentSelfLink);
                        this.host.completeIteration();
                    }));
        }
        this.host.testWait();
        this.host.testStart(1);
        Operation delete = subscribeOp.clone().setUri(factoryUri).setAction(Action.DELETE);
        this.host.stopSubscriptionService(delete, notificationTarget);
        this.host.testWait();
        this.verifySubscriberCount(new URI[]{factoryUri}, 0);
    }
    private void doNotificationsWithReplayState(URI[] childUris)
            throws Throwable {
        this.host.log("starting subscription with replay");
        final AtomicInteger deletesRemainingCount = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget(
                UUID.randomUUID().toString(),
                deletesRemainingCount);
        sr.replayState = true;
        subscribeToServices(childUris, sr);
        verifySubscriberCount(childUris, 1);
        patchChildren(childUris, false);
        patchChildren(childUris, false);
        unsubscribeFromChildren(childUris, sr.reference, false);
        verifySubscriberCount(childUris, 0);
        deleteNotificationTarget(deletesRemainingCount, sr);
    }
    private void doNotificationsWithExpiration(URI[] childUris)
            throws Throwable {
        this.host.log("starting subscription with expiration");
        final AtomicInteger deletesRemainingCount = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget(UUID.randomUUID()
                .toString(), deletesRemainingCount, false, false);
        sr.documentExpirationTimeMicros = Utils.getNowMicrosUtc()
                + this.host.getMaintenanceIntervalMicros() * 2;
        subscribeToServices(childUris, sr);
        verifySubscriberCount(childUris, 1);
        Thread.sleep((this.host.getMaintenanceIntervalMicros() / 1000) * 2);
        patchChildren(childUris, true);
        verifySubscriberCount(childUris, 0);
        deleteNotificationTarget(deletesRemainingCount, sr);
    }
    private void deleteNotificationTarget(AtomicInteger deletesRemainingCount,
            ServiceSubscriber sr) throws Throwable {
        deletesRemainingCount.set(1);
        TestContext ctx = testCreate(1);
        this.host.send(Operation.createDelete(sr.reference)
                .setCompletion((o, e) -> ctx.completeIteration()));
        testWait(ctx);
    }
    private void doNotificationsWithFailure(URI[] childUris) throws Throwable, InterruptedException {
        this.host.log("starting subscription with failure, stopping notification target");
        final AtomicInteger deletesRemainingCount = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget(UUID.randomUUID()
                .toString(), deletesRemainingCount);
        subscribeToServices(childUris, sr);
        verifySubscriberCount(childUris, 1);
        deleteNotificationTarget(deletesRemainingCount, sr);
        patchChildren(childUris, true);
        verifySubscriberCount(true, childUris, 1, 1L);
        boolean expectSkippedNotificationsPragma = true;
        this.host.log("restarting notification target");
        createAndStartNotificationTarget(sr.reference.getPath(),
                deletesRemainingCount, expectSkippedNotificationsPragma, true);
        patchChildren(childUris, false);
        verifySubscriberCount(true, childUris, 1, 0L);
        this.host.log("stopping notification target, again");
        deleteNotificationTarget(deletesRemainingCount, sr);
        while (!verifySubscriberCount(false, childUris, 0, null)) {
            Thread.sleep(VerificationHost.FAST_MAINT_INTERVAL_MILLIS);
            patchChildren(childUris, true);
        }
        this.host.log("Verifying all subscriptions have been removed");
        verifySubscriberCount(childUris, 0);
    }
    private void doNotificationsWithLimitAndPublicUri(URI[] childUris) throws Throwable,
            InterruptedException, TimeoutException {
        this.host.log("starting subscription with limit and public uri");
        final AtomicInteger deletesRemainingCount = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget(UUID.randomUUID()
                .toString(), deletesRemainingCount);
        deletesRemainingCount.set(childUris.length + 1);
        sr.usePublicUri = true;
        sr.notificationLimit = this.updateCount;
        subscribeToServices(childUris, sr);
        verifySubscriberCount(childUris, 1);
        patchChildren(childUris, false);
        verifySubscriberCount(childUris, 0);
        Date exp = this.host.getTestExpiration();
        while (deletesRemainingCount.get() != 1) {
            Thread.sleep(250);
            if (new Date().after(exp)) {
                throw new TimeoutException("DELETEs not received at notification target:"
                        + deletesRemainingCount.get());
            }
        }
        deleteNotificationTarget(deletesRemainingCount, sr);
    }
    private void doDeleteNotifications(URI[] childUris, Long counterValue) throws Throwable {
        this.host.log("starting subscription for DELETEs");
        final AtomicInteger deletesRemainingCount = new AtomicInteger();
        ServiceSubscriber sr = createAndStartNotificationTarget(UUID.randomUUID()
                .toString(), deletesRemainingCount);
        subscribeToServices(childUris, sr);
        this.host.testStart(childUris.length * 2);
        for (URI child : childUris) {
            ExampleServiceState initialState = new ExampleServiceState();
            initialState.counter = counterValue;
            Operation delete = Operation
                    .createDelete(child)
                    .setBody(initialState)
                    .setCompletion(this.host.getCompletion());
            this.host.send(delete);
        }
        this.host.testWait();
        deleteNotificationTarget(deletesRemainingCount, sr);
    }
    private ServiceSubscriber createAndStartNotificationTarget(String link,
            final AtomicInteger deletesRemainingCount) throws Throwable {
        return createAndStartNotificationTarget(link, deletesRemainingCount, false, true);
    }
    private ServiceSubscriber createAndStartNotificationTarget(String link,
            final AtomicInteger deletesRemainingCount,
            boolean expectSkipNotificationsPragma,
            boolean completeIterations) throws Throwable {
        final AtomicBoolean seenSkippedNotificationPragma =
                new AtomicBoolean(false);
        return createAndStartNotificationTarget(link, (update) -> {
            if (!update.isNotification()) {
                if (update.getAction() == Action.DELETE) {
                    int r = deletesRemainingCount.decrementAndGet();
                    if (r != 0) {
                        update.complete();
                        return true;
                    }
                }
                return false;
            }
            if (update.getAction() != Action.PATCH &&
                    update.getAction() != Action.PUT &&
                    update.getAction() != Action.DELETE) {
                update.complete();
                return true;
            }
            if (expectSkipNotificationsPragma) {
                String pragma = update.getRequestHeader(Operation.PRAGMA_HEADER);
                if (!seenSkippedNotificationPragma.get() && (pragma == null
                        || !pragma.contains(Operation.PRAGMA_DIRECTIVE_SKIPPED_NOTIFICATIONS))) {
                    this.host.failIteration(new IllegalStateException(
                            "Missing skipped notification pragma"));
                    return true;
                } else {
                    seenSkippedNotificationPragma.set(true);
                }
            }
            if (completeIterations) {
                this.host.completeIteration();
            }
            update.complete();
            return true;
        });
    }
    private ServiceSubscriber createAndStartNotificationTarget(
            Function<Operation, Boolean> h) throws Throwable {
        return createAndStartNotificationTarget(UUID.randomUUID().toString(), h);
    }
    private ServiceSubscriber createAndStartNotificationTarget(
            String link,
            Function<Operation, Boolean> h) throws Throwable {
        StatelessService notificationTarget = createNotificationTargetService(h);
        Operation startOp = Operation
                .createPost(UriUtils.buildUri(this.host, link))
                .setCompletion(this.host.getCompletion())
                .setReferer(this.host.getReferer());
        this.host.testStart(1);
        this.host.startService(startOp, notificationTarget);
        this.host.testWait();
        ServiceSubscriber sr = new ServiceSubscriber();
        sr.reference = notificationTarget.getUri();
        return sr;
    }
    private StatelessService createNotificationTargetService(Function<Operation, Boolean> h) {
        return new StatelessService() {
            @Override
            public void handleRequest(Operation update) {
                if (!h.apply(update)) {
                    super.handleRequest(update);
                }
            }
        };
    }
    private void subscribeToServices(URI[] uris, ServiceSubscriber sr) throws Throwable {
        int expectedCompletions = uris.length;
        if (sr.replayState) {
            expectedCompletions *= 2;
        }
        subscribeToServices(uris, sr, expectedCompletions);
    }
    private void subscribeToServices(URI[] uris, ServiceSubscriber sr, int expectedCompletions) throws Throwable {
        this.host.testStart(expectedCompletions);
        for (int i = 0; i < uris.length; i++) {
            subscribeToService(uris[i], sr);
        }
        this.host.testWait();
    }
    private void subscribeToService(URI uri, ServiceSubscriber sr) {
        if (sr.usePublicUri) {
            sr = Utils.clone(sr);
            sr.reference = UriUtils.buildPublicUri(this.host, sr.reference.getPath());
        }
        URI subUri = UriUtils.buildSubscriptionUri(uri);
        this.host.send(Operation.createPost(subUri)
                .setCompletion(this.host.getCompletion())
                .setReferer(this.host.getReferer())
                .setBody(sr)
                .addPragmaDirective(Operation.PRAGMA_DIRECTIVE_QUEUE_FOR_SERVICE_AVAILABILITY));
    }
    private void unsubscribeFromChildren(URI[] uris, URI targetUri,
            boolean useServiceHostStopSubscription) throws Throwable {
        int count = uris.length;
        TestContext ctx = testCreate(count);
        for (int i = 0; i < count; i++) {
            if (useServiceHostStopSubscription) {
                host.stopSubscriptionService(
                        Operation.createDelete(uris[i])
                                .setCompletion(ctx.getCompletion()),
                        targetUri);
                continue;
            }
            ServiceSubscriber unsubscribeBody = new ServiceSubscriber();
            unsubscribeBody.reference = targetUri;
            URI subUri = UriUtils.buildSubscriptionUri(uris[i]);
            this.host.send(Operation.createDelete(subUri)
                    .setCompletion(ctx.getCompletion())
                    .setBody(unsubscribeBody));
        }
        testWait(ctx);
    }
    private boolean verifySubscriberCount(URI[] uris, int subscriberCount) throws Throwable {
        return verifySubscriberCount(true, uris, subscriberCount, null);
    }
    private boolean verifySubscriberCount(boolean wait, URI[] uris, int subscriberCount,
            Long failedNotificationCount)
            throws Throwable {
        URI[] subUris = new URI[uris.length];
        int i = 0;
        for (URI u : uris) {
            URI subUri = UriUtils.buildSubscriptionUri(u);
            subUris[i++] = subUri;
        }
        Date exp = this.host.getTestExpiration();
        while (new Date().before(exp)) {
            boolean isConverged = true;
            Map<URI, ServiceSubscriptionState> subStates = this.host.getServiceState(null,
                    ServiceSubscriptionState.class, subUris);
            for (ServiceSubscriptionState state : subStates.values()) {
                int expected = subscriberCount;
                int actual = state.subscribers.size();
                if (actual != expected) {
                    isConverged = false;
                    break;
                }
                if (failedNotificationCount == null) {
                    continue;
                }
                for (ServiceSubscriber sr : state.subscribers.values()) {
                    if (sr.failedNotificationCount == null && failedNotificationCount == 0) {
                        continue;
                    }
                    if (sr.failedNotificationCount == null
                            || 0 != sr.failedNotificationCount.compareTo(failedNotificationCount)) {
                        isConverged = false;
                        break;
                    }
                }
            }
            if (isConverged) {
                return true;
            }
            if (!wait) {
                return false;
            }
            Thread.sleep(250);
        }
        throw new TimeoutException("Subscriber count did not converge to " + subscriberCount);
    }
    private void patchChildren(URI[] uris, boolean expectFailure) throws Throwable {
        int count = expectFailure ? uris.length : uris.length * 2;
        long c = this.updateCount;
        if (!expectFailure) {
            count *= this.updateCount;
        } else {
            c = 1;
        }
        this.host.testStart(count);
        for (int i = 0; i < uris.length; i++) {
            for (int k = 0; k < c; k++) {
                ExampleServiceState initialState = new ExampleServiceState();
                initialState.counter = Long.MAX_VALUE;
                Operation patch = Operation
                        .createPatch(uris[i])
                        .setBody(initialState)
                        .setCompletion(this.host.getCompletion());
                this.host.send(patch);
            }
        }
        this.host.testWait();
    }
}
