
package com.vmware.xenon.services.common;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import com.vmware.xenon.common.AuthorizationSetupHelper;
import com.vmware.xenon.common.CommandLineArgumentParser;
import com.vmware.xenon.common.FactoryService;
import com.vmware.xenon.common.NodeSelectorService.SelectAndForwardRequest;
import com.vmware.xenon.common.NodeSelectorService.SelectOwnerResponse;
import com.vmware.xenon.common.NodeSelectorState;
import com.vmware.xenon.common.Operation;
import com.vmware.xenon.common.Operation.AuthorizationContext;
import com.vmware.xenon.common.Operation.CompletionHandler;
import com.vmware.xenon.common.OperationJoin;
import com.vmware.xenon.common.Service;
import com.vmware.xenon.common.Service.Action;
import com.vmware.xenon.common.Service.ProcessingStage;
import com.vmware.xenon.common.Service.ServiceOption;
import com.vmware.xenon.common.ServiceConfigUpdateRequest;
import com.vmware.xenon.common.ServiceConfiguration;
import com.vmware.xenon.common.ServiceDocument;
import com.vmware.xenon.common.ServiceDocumentDescription;
import com.vmware.xenon.common.ServiceDocumentQueryResult;
import com.vmware.xenon.common.ServiceHost;
import com.vmware.xenon.common.ServiceHost.HttpScheme;
import com.vmware.xenon.common.ServiceHost.ServiceHostState;
import com.vmware.xenon.common.ServiceStats;
import com.vmware.xenon.common.ServiceStats.ServiceStat;
import com.vmware.xenon.common.StatefulService;
import com.vmware.xenon.common.SynchronizationTaskService;
import com.vmware.xenon.common.TaskState;
import com.vmware.xenon.common.UriUtils;
import com.vmware.xenon.common.Utils;
import com.vmware.xenon.common.serialization.KryoSerializers;
import com.vmware.xenon.common.test.AuthorizationHelper;
import com.vmware.xenon.common.test.MinimalTestServiceState;
import com.vmware.xenon.common.test.RoundRobinIterator;
import com.vmware.xenon.common.test.TestContext;
import com.vmware.xenon.common.test.TestProperty;
import com.vmware.xenon.common.test.TestRequestSender;
import com.vmware.xenon.common.test.VerificationHost;
import com.vmware.xenon.common.test.VerificationHost.WaitHandler;
import com.vmware.xenon.services.common.ExampleService.ExampleServiceState;
import com.vmware.xenon.services.common.ExampleTaskService.ExampleTaskServiceState;
import com.vmware.xenon.services.common.MinimalTestService.MinimalTestServiceErrorResponse;
import com.vmware.xenon.services.common.NodeGroupBroadcastResult.PeerNodeResult;
import com.vmware.xenon.services.common.NodeGroupService.JoinPeerRequest;
import com.vmware.xenon.services.common.NodeGroupService.NodeGroupConfig;
import com.vmware.xenon.services.common.NodeGroupService.NodeGroupState;
import com.vmware.xenon.services.common.NodeState.NodeOption;
import com.vmware.xenon.services.common.QueryTask.Query;
import com.vmware.xenon.services.common.QueryTask.Query.Builder;
import com.vmware.xenon.services.common.QueryTask.QueryTerm.MatchType;
import com.vmware.xenon.services.common.ReplicationTestService.ReplicationTestServiceErrorResponse;
import com.vmware.xenon.services.common.ReplicationTestService.ReplicationTestServiceState;
import com.vmware.xenon.services.common.ResourceGroupService.PatchQueryRequest;
import com.vmware.xenon.services.common.ResourceGroupService.ResourceGroupState;
import com.vmware.xenon.services.common.RoleService.RoleState;
import com.vmware.xenon.services.common.UserService.UserState;
public class TestNodeGroupService {
    public static class PeriodicExampleFactoryService extends FactoryService {
        public static final String SELF_LINK = "test/examples-periodic";
        public PeriodicExampleFactoryService() {
            super(ExampleServiceState.class);
        }
        @Override
        public Service createServiceInstance() throws Throwable {
            ExampleService s = new ExampleService();
            s.toggleOption(ServiceOption.PERIODIC_MAINTENANCE, true);
            return s;
        }
    }
    public static class ExampleServiceWithCustomSelector extends StatefulService {
        public ExampleServiceWithCustomSelector() {
            super(ExampleServiceState.class);
            super.toggleOption(ServiceOption.REPLICATION, true);
            super.toggleOption(ServiceOption.OWNER_SELECTION, true);
            super.toggleOption(ServiceOption.PERSISTENCE, true);
        }
    }
    public static class ExampleFactoryServiceWithCustomSelector extends FactoryService {
        public ExampleFactoryServiceWithCustomSelector() {
            super(ExampleServiceState.class);
            super.setPeerNodeSelectorPath(CUSTOM_GROUP_NODE_SELECTOR);
        }
        @Override
        public Service createServiceInstance() throws Throwable {
            return new ExampleServiceWithCustomSelector();
        }
    }
    private static final String CUSTOM_EXAMPLE_SERVICE_KIND = "xenon:examplestate";
    private static final String CUSTOM_NODE_GROUP_NAME = "custom";
    private static final String CUSTOM_NODE_GROUP = UriUtils.buildUriPath(
            ServiceUriPaths.NODE_GROUP_FACTORY,
            CUSTOM_NODE_GROUP_NAME);
    private static final String CUSTOM_GROUP_NODE_SELECTOR = UriUtils.buildUriPath(
            ServiceUriPaths.NODE_SELECTOR_PREFIX,
            CUSTOM_NODE_GROUP_NAME);
    public static final long DEFAULT_MAINT_INTERVAL_MICROS = TimeUnit.MILLISECONDS
            .toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS);
    private VerificationHost host;
    public int testIterationCount = 1;
    public int nodeCount = 3;
    public int updateCount = 10;
    public int serviceCount = 10;
    public long testDurationSeconds;
    public long iterationCount = 1;
    public long totalOperationLimit = Long.MAX_VALUE;
    private NodeGroupConfig nodeGroupConfig = new NodeGroupConfig();
    private EnumSet<ServiceOption> postCreationServiceOptions = EnumSet.noneOf(ServiceOption.class);
    private boolean expectFailure;
    private long expectedFailureStartTimeMicros;
    private List<URI> expectedFailedHosts = new ArrayList<>();
    private String replicationTargetFactoryLink = ExampleService.FACTORY_LINK;
    private String replicationNodeSelector = ServiceUriPaths.DEFAULT_NODE_SELECTOR;
    private long replicationFactor;
    private BiPredicate<ExampleServiceState, ExampleServiceState> exampleStateConvergenceChecker = (
            initial, current) -> {
        if (current.name == null) {
            return false;
        }
        if (!this.host.isRemotePeerTest() &&
                !CUSTOM_EXAMPLE_SERVICE_KIND.equals(current.documentKind)) {
            return false;
        }
        return current.name.equals(initial.name);
    };
    private Function<ExampleServiceState, Void> exampleStateUpdateBodySetter = (
            ExampleServiceState state) -> {
        state.name = Utils.getNowMicrosUtc() + "";
        return null;
    };
    private boolean isPeerSynchronizationEnabled = true;
    private boolean isAuthorizationEnabled = false;
    private HttpScheme replicationUriScheme;
    private boolean skipAvailabilityChecks = false;
    private boolean isMultiLocationTest = false;
    private void setUp(int localHostCount) throws Throwable {
        if (this.host != null) {
            return;
        }
        CommandLineArgumentParser.parseFromProperties(this);
        this.host = VerificationHost.create(0);
        this.host.setAuthorizationEnabled(this.isAuthorizationEnabled);
        VerificationHost.createAndAttachSSLClient(this.host);
        if (this.replicationUriScheme == HttpScheme.HTTPS_ONLY) {
            this.host.setPort(ServiceHost.PORT_VALUE_LISTENER_DISABLED);
            this.host.setSecurePort(0);
        }
        if (this.testDurationSeconds > 0) {
            this.host.maintenanceIntervalMillis = TimeUnit.MICROSECONDS.toMillis(
                    ServiceHostState.DEFAULT_MAINTENANCE_INTERVAL_MICROS);
        }
        this.host.start();
        if (this.host.isAuthorizationEnabled()) {
            this.host.setSystemAuthorizationContext();
        }
        CommandLineArgumentParser.parseFromProperties(this.host);
        this.host.setStressTest(this.host.isStressTest);
        this.host.setPeerSynchronizationEnabled(this.isPeerSynchronizationEnabled);
        this.host.setMultiLocationTest(this.isMultiLocationTest);
        this.host.setUpPeerHosts(localHostCount);
        for (VerificationHost h1 : this.host.getInProcessHostMap().values()) {
            setUpPeerHostWithAdditionalServices(h1);
        }
        if (this.host.isRemotePeerTest()) {
            Utils.registerKind(ExampleServiceState.class,
                    Utils.toDocumentKind(ExampleServiceState.class));
        }
    }
    private void setUpPeerHostWithAdditionalServices(VerificationHost h1) throws Throwable {
        h1.setStressTest(this.host.isStressTest);
        h1.waitForServiceAvailable(ExampleService.FACTORY_LINK);
        Replication1xExampleFactoryService exampleFactory1x = new Replication1xExampleFactoryService();
        h1.startServiceAndWait(exampleFactory1x,
                Replication1xExampleFactoryService.SELF_LINK,
                null);
        Replication3xExampleFactoryService exampleFactory3x = new Replication3xExampleFactoryService();
        h1.startServiceAndWait(exampleFactory3x,
                Replication3xExampleFactoryService.SELF_LINK,
                null);
        ReplicationFactoryTestService ownerSelRplFactory = new ReplicationFactoryTestService();
        h1.startServiceAndWait(ownerSelRplFactory,
                ReplicationFactoryTestService.OWNER_SELECTION_SELF_LINK,
                null);
        ReplicationFactoryTestService strictReplFactory = new ReplicationFactoryTestService();
        h1.startServiceAndWait(strictReplFactory,
                ReplicationFactoryTestService.STRICT_SELF_LINK, null);
        ReplicationFactoryTestService replFactory = new ReplicationFactoryTestService();
        h1.startServiceAndWait(replFactory,
                ReplicationFactoryTestService.SIMPLE_REPL_SELF_LINK, null);
    }
    private Map<URI, URI> getFactoriesPerNodeGroup(String factoryLink) {
        Map<URI, URI> map = this.host.getNodeGroupToFactoryMap(factoryLink);
        for (URI h : this.expectedFailedHosts) {
            URI e = UriUtils.buildUri(h, ServiceUriPaths.DEFAULT_NODE_GROUP);
            map.remove(e);
        }
        return map;
    }
    @Before
    public void setUp() {
        CommandLineArgumentParser.parseFromProperties(this);
        Utils.registerKind(ExampleServiceState.class, CUSTOM_EXAMPLE_SERVICE_KIND);
    }
    private void setUpOnDemandLoad() throws Throwable {
        setUp();
        this.nodeCount = Math.max(5, this.nodeCount);
        this.isPeerSynchronizationEnabled = true;
        this.skipAvailabilityChecks = true;
        setUp(this.nodeCount);
        toggleOnDemandLoad();
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        this.host.setNodeGroupQuorum(this.host.getPeerCount() / 2 + 1);
    }
    private void toggleOnDemandLoad() {
        for (URI nodeUri : this.host.getNodeGroupMap().keySet()) {
            URI factoryUri = UriUtils.buildUri(nodeUri, ExampleService.FACTORY_LINK);
            this.host.toggleServiceOptions(factoryUri, EnumSet.of(ServiceOption.ON_DEMAND_LOAD),
                    null);
        }
    }
    @After
    public void tearDown() throws InterruptedException {
        Utils.registerKind(ExampleServiceState.class,
                Utils.toDocumentKind(ExampleServiceState.class));
        if (this.host == null) {
            return;
        }
        if (this.host.isRemotePeerTest()) {
            try {
                this.host.logNodeProcessLogs(this.host.getNodeGroupMap().keySet(),
                        ServiceUriPaths.PROCESS_LOG);
            } catch (Throwable e) {
                this.host.log("Failure retrieving process logs: %s", Utils.toString(e));
            }
            try {
                this.host.logNodeManagementState(this.host.getNodeGroupMap().keySet());
            } catch (Throwable e) {
                this.host.log("Failure retrieving management state: %s", Utils.toString(e));
            }
        }
        this.host.tearDownInProcessPeers();
        this.host.toggleNegativeTestMode(false);
        this.host.tearDown();
        this.host = null;
        System.clearProperty(
                NodeSelectorReplicationService.PROPERTY_NAME_REPLICA_NOT_FOUND_TIMEOUT_MICROS);
    }
    @Test
    public void synchronizationCollisionWithPosts() throws Throwable {
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        this.host.setNodeGroupQuorum(this.nodeCount);
        this.host.waitForNodeGroupConvergence(this.nodeCount);
        URI factoryUri = UriUtils.buildUri(this.host.getPeerHost(), ExampleService.FACTORY_LINK);
        waitForReplicatedFactoryServiceAvailable(factoryUri, this.replicationNodeSelector);
        String taskPath = UriUtils.buildUriPath(
                SynchronizationTaskService.FACTORY_LINK,
                UriUtils.convertPathCharsFromLink(ExampleService.FACTORY_LINK));
        VerificationHost owner = null;
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            if (peer.isOwner(ExampleService.FACTORY_LINK, ServiceUriPaths.DEFAULT_NODE_SELECTOR)) {
                owner = peer;
                break;
            }
        }
        this.host.log(Level.INFO, "Owner of synch-task is %s", owner.getId());
        URI taskUri = UriUtils.buildUri(owner, taskPath);
        SynchronizationTaskService.State taskState = this.host.getServiceState(
                null, SynchronizationTaskService.State.class, taskUri);
        long membershipUpdateTimeMicros = taskState.membershipUpdateTimeMicros;
        ExampleServiceState state = new ExampleServiceState();
        state.name = "testing";
        TestContext ctx = this.host.testCreate((this.serviceCount * 10) + 1);
        for (int i = 0; i < this.serviceCount * 10; i++) {
            if (i == 5) {
                SynchronizationTaskService.State task = new SynchronizationTaskService.State();
                task.documentSelfLink = UriUtils.convertPathCharsFromLink(ExampleService.FACTORY_LINK);
                task.factorySelfLink = ExampleService.FACTORY_LINK;
                task.factoryStateKind = Utils.buildKind(ExampleService.ExampleServiceState.class);
                task.membershipUpdateTimeMicros = membershipUpdateTimeMicros + 1;
                task.nodeSelectorLink = ServiceUriPaths.DEFAULT_NODE_SELECTOR;
                task.queryResultLimit = 1000;
                task.taskInfo = TaskState.create();
                task.taskInfo.isDirect = true;
                Operation post = Operation
                        .createPost(owner, SynchronizationTaskService.FACTORY_LINK)
                        .setBody(task)
                        .setReferer(this.host.getUri())
                        .setCompletion(ctx.getCompletion());
                this.host.sendRequest(post);
            }
            Operation post = Operation
                    .createPost(factoryUri)
                    .setBody(state)
                    .setReferer(this.host.getUri())
                    .setCompletion(ctx.getCompletion());
            this.host.sendRequest(post);
        }
        ctx.await();
    }
    @Test
    public void commandLineJoinRetries() throws Throwable {
        this.host = VerificationHost.create(0);
        this.host.start();
        ExampleServiceHost nodeA = null;
        TemporaryFolder tmpFolderA = new TemporaryFolder();
        tmpFolderA.create();
        this.setUp(1);
        try {
            nodeA = new ExampleServiceHost();
            String id = "nodeA-" + VerificationHost.hostNumber.incrementAndGet();
            int bogusPort = 1;
            String[] args = {
                    "--port=0",
                    "--id=" + id,
                    "--bindAddress=127.0.0.1",
                    "--sandbox="
                            + tmpFolderA.getRoot().getAbsolutePath(),
                    "--peerNodes=" + "http:
            };
            nodeA.initialize(args);
            nodeA.setMaintenanceIntervalMicros(TimeUnit.MILLISECONDS
                    .toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS));
            nodeA.start();
            URI nodeGroupUri = UriUtils.buildUri(nodeA, ServiceUriPaths.DEFAULT_NODE_GROUP);
            URI statsUri = UriUtils.buildStatsUri(nodeGroupUri);
            this.host.waitFor("expected stat did not converge", () -> {
                ServiceStats stats = this.host.getServiceState(null, ServiceStats.class, statsUri);
                ServiceStat st = stats.entries.get(NodeGroupService.STAT_NAME_JOIN_RETRY_COUNT);
                if (st == null || st.latestValue < 1) {
                    return false;
                }
                return true;
            });
        } finally {
            if (nodeA != null) {
                nodeA.stop();
                tmpFolderA.delete();
            }
        }
    }
    @Test
    public void synchronizationOnDemandLoad() throws Throwable {
        setUp(this.nodeCount);
        long intervalMicros = TimeUnit.MILLISECONDS.toMicros(200);
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.setServiceCacheClearDelayMicros(h.getMaintenanceIntervalMicros());
            OnDemandLoadFactoryService.create(h);
        }
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        this.host.setNodeGroupQuorum(this.nodeCount);
        this.host.waitForNodeGroupConvergence(this.nodeCount);
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(OnDemandLoadFactoryService.SELF_LINK),
                this.replicationNodeSelector);
        VerificationHost h = this.host.getPeerHost();
        Map<URI, ExampleServiceState> childServices = this.host.doFactoryChildServiceStart(
                null,
                this.serviceCount,
                ExampleServiceState.class,
                (o) -> {
                    ExampleServiceState initialState = new ExampleServiceState();
                    initialState.name = UUID.randomUUID().toString();
                    o.setBody(initialState);
                },
                UriUtils.buildFactoryUri(h, OnDemandLoadFactoryService.class));
        for (VerificationHost vh : this.host.getInProcessHostMap().values()) {
            this.host.waitFor("ODL services did not stop as expected",
                    () -> checkOdlServiceStopCount(vh, this.serviceCount));
        }
        VerificationHost newHost = this.host.setUpLocalPeerHost(0, h.getMaintenanceIntervalMicros(),
                null);
        newHost.setServiceCacheClearDelayMicros(intervalMicros);
        OnDemandLoadFactoryService.create(newHost);
        this.host.joinNodesAndVerifyConvergence(this.nodeCount + 1);
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(OnDemandLoadFactoryService.SELF_LINK),
                this.replicationNodeSelector);
        this.host.log(Level.INFO, "Verifying synchronization for ODL services");
        for (Entry<URI, ExampleServiceState> childService : childServices.entrySet()) {
            String childServicePath = childService.getKey().getPath();
            ExampleServiceState state = this.host.getServiceState(null,
                    ExampleServiceState.class, UriUtils.buildUri(newHost, childServicePath));
            assertNotNull(state);
        }
        this.host.waitFor("ODL services did not stop as expected",
                () -> checkOdlServiceStopCount(newHost, this.serviceCount));
    }
    private boolean checkOdlServiceStopCount(VerificationHost host, int serviceCount)
            throws Throwable {
        ServiceStat stopCount = host
                .getServiceStats(host.getManagementServiceUri())
                .get(ServiceHostManagementService.STAT_NAME_ODL_STOP_COUNT);
        if (stopCount == null || stopCount.latestValue < serviceCount) {
            this.host.log(Level.INFO,
                    "Current stopCount is %s",
                    (stopCount != null) ? String.valueOf(stopCount.latestValue) : "null");
            return false;
        }
        return true;
    }
    @Test
    public void customNodeGroupWithObservers() throws Throwable {
        for (int i = 0; i < this.iterationCount; i++) {
            Logger.getAnonymousLogger().info("Iteration: " + i);
            verifyCustomNodeGroupWithObservers();
            tearDown();
        }
    }
    private void verifyCustomNodeGroupWithObservers() throws Throwable {
        setUp(this.nodeCount);
        URI observerHostUri = this.host.getPeerHostUri();
        ServiceHostState observerHostState = this.host.getServiceState(null,
                ServiceHostState.class,
                UriUtils.buildUri(observerHostUri, ServiceUriPaths.CORE_MANAGEMENT));
        Map<URI, NodeState> selfStatePerNode = new HashMap<>();
        NodeState observerSelfState = new NodeState();
        observerSelfState.id = observerHostState.id;
        observerSelfState.options = EnumSet.of(NodeOption.OBSERVER);
        selfStatePerNode.put(observerHostUri, observerSelfState);
        this.host.createCustomNodeGroupOnPeers(CUSTOM_NODE_GROUP_NAME, selfStatePerNode);
        final String customFactoryLink = "custom-factory";
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            NodeSelectorState initialState = new NodeSelectorState();
            initialState.nodeGroupLink = CUSTOM_NODE_GROUP;
            h.startServiceAndWait(new ConsistentHashingNodeSelectorService(),
                    CUSTOM_GROUP_NODE_SELECTOR, initialState);
            h.startServiceAndWait(ExampleFactoryServiceWithCustomSelector.class, customFactoryLink);
        }
        URI customNodeGroupServiceOnObserver = UriUtils
                .buildUri(observerHostUri, CUSTOM_NODE_GROUP);
        Map<URI, EnumSet<NodeOption>> expectedOptionsPerNode = new HashMap<>();
        expectedOptionsPerNode.put(customNodeGroupServiceOnObserver,
                observerSelfState.options);
        this.host.joinNodesAndVerifyConvergence(CUSTOM_NODE_GROUP, this.nodeCount,
                this.nodeCount, expectedOptionsPerNode);
        this.host.setNodeGroupQuorum(2, customNodeGroupServiceOnObserver);
        this.host.waitForNodeSelectorQuorumConvergence(CUSTOM_GROUP_NODE_SELECTOR, 2);
        this.host.waitForNodeGroupIsAvailableConvergence(CUSTOM_NODE_GROUP);
        int restartCount = 0;
        for (URI hostUri : this.host.getNodeGroupMap().keySet()) {
            URI customNodeGroupUri = UriUtils.buildUri(hostUri, CUSTOM_NODE_GROUP);
            NodeGroupState ngs = this.host.getServiceState(null, NodeGroupState.class,
                    customNodeGroupUri);
            for (NodeState ns : ngs.nodes.values()) {
                if (ns.id.equals(observerHostState.id)) {
                    assertTrue(ns.options.contains(NodeOption.OBSERVER));
                } else {
                    assertTrue(ns.options.contains(NodeOption.PEER));
                }
            }
            ServiceStats nodeGroupStats = this.host.getServiceState(null, ServiceStats.class,
                    UriUtils.buildStatsUri(customNodeGroupUri));
            ServiceStat restartStat = nodeGroupStats.entries
                    .get(NodeGroupService.STAT_NAME_RESTARTING_SERVICES_COUNT);
            if (restartStat != null) {
                restartCount += restartStat.latestValue;
            }
        }
        assertEquals("expected different number of service restarts", restartCount, 0);
        this.host.joinNodesAndVerifyConvergence(this.nodeCount, true);
        URI observerFactoryUri = UriUtils.buildUri(observerHostUri, customFactoryLink);
        waitForReplicatedFactoryServiceAvailable(observerFactoryUri, CUSTOM_GROUP_NODE_SELECTOR);
        Map<URI, ExampleServiceState> serviceStatesOnPost = this.host.doFactoryChildServiceStart(
                null, this.serviceCount,
                ExampleServiceState.class,
                (o) -> {
                    ExampleServiceState body = new ExampleServiceState();
                    body.name = Utils.getNowMicrosUtc() + "";
                    o.setBody(body);
                },
                observerFactoryUri);
        ServiceDocumentQueryResult r = this.host.getFactoryState(observerFactoryUri);
        assertEquals(0, r.documentLinks.size());
        Map<URI, ExampleServiceState> serviceStatesFromGet = this.host.getServiceState(
                null, ExampleServiceState.class, serviceStatesOnPost.keySet());
        for (ExampleServiceState s : serviceStatesFromGet.values()) {
            if (observerHostState.id.equals(s.documentOwner)) {
                throw new IllegalStateException("Observer node reported state for service");
            }
        }
        createExampleServices(observerHostUri);
        QueryTask.QuerySpecification q = new QueryTask.QuerySpecification();
        q.query.setTermPropertyName(ServiceDocument.FIELD_NAME_KIND).setTermMatchValue(
                Utils.buildKind(ExampleServiceState.class));
        QueryTask task = QueryTask.create(q).setDirect(true);
        for (Entry<URI, URI> node : this.host.getNodeGroupMap().entrySet()) {
            URI nodeUri = node.getKey();
            URI serviceUri = UriUtils.buildUri(nodeUri, ServiceUriPaths.CORE_LOCAL_QUERY_TASKS);
            URI forwardQueryUri = UriUtils.buildForwardRequestUri(serviceUri, null,
                    CUSTOM_GROUP_NODE_SELECTOR);
            TestContext ctx = this.host.testCreate(1);
            Operation post = Operation
                    .createPost(forwardQueryUri)
                    .setBody(task)
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            ctx.fail(e);
                            return;
                        }
                        QueryTask rsp = o.getBody(QueryTask.class);
                        int resultCount = rsp.results.documentLinks.size();
                        if (resultCount != 2 * this.serviceCount) {
                            ctx.fail(new IllegalStateException(
                                    "Forwarded query returned unexpected document count " +
                                            resultCount));
                            return;
                        }
                        ctx.complete();
                    });
            this.host.send(post);
            ctx.await();
        }
        task.querySpec.options = EnumSet.of(QueryTask.QuerySpecification.QueryOption.BROADCAST);
        task.nodeSelectorLink = CUSTOM_GROUP_NODE_SELECTOR;
        URI queryPostUri = UriUtils.buildUri(observerHostUri, ServiceUriPaths.CORE_QUERY_TASKS);
        TestContext ctx = this.host.testCreate(1);
        Operation post = Operation
                .createPost(queryPostUri)
                .setBody(task)
                .setCompletion((o, e) -> {
                    if (e != null) {
                        ctx.fail(e);
                        return;
                    }
                    QueryTask rsp = o.getBody(QueryTask.class);
                    int resultCount = rsp.results.documentLinks.size();
                    if (resultCount != 2 * this.serviceCount) {
                        ctx.fail(new IllegalStateException(
                                "Broadcast query returned unexpected document count " +
                                        resultCount));
                        return;
                    }
                    ctx.complete();
                });
        this.host.send(post);
        ctx.await();
        URI existingNodeGroup = this.host.getPeerNodeGroupUri();
        Collection<VerificationHost> existingHosts = this.host.getInProcessHostMap().values();
        int additionalHostCount = this.nodeCount;
        this.host.setUpPeerHosts(additionalHostCount);
        List<ServiceHost> newHosts = Collections.synchronizedList(new ArrayList<>());
        newHosts.addAll(this.host.getInProcessHostMap().values());
        newHosts.removeAll(existingHosts);
        expectedOptionsPerNode.clear();
        TestContext testContext = this.host.testCreate(newHosts.size());
        for (ServiceHost h : newHosts) {
            URI newCustomNodeGroupUri = UriUtils.buildUri(h, ServiceUriPaths.DEFAULT_NODE_GROUP);
            JoinPeerRequest joinBody = JoinPeerRequest.create(existingNodeGroup, null);
            joinBody.localNodeOptions = EnumSet.of(NodeOption.PEER);
            this.host.send(Operation.createPost(newCustomNodeGroupUri)
                    .setBody(joinBody)
                    .setCompletion(testContext.getCompletion()));
            expectedOptionsPerNode.put(newCustomNodeGroupUri, joinBody.localNodeOptions);
        }
        testContext.await();
        this.host.waitForNodeGroupConvergence(this.host.getNodeGroupMap().values(),
                this.host.getNodeGroupMap().size(),
                this.host.getNodeGroupMap().size(),
                expectedOptionsPerNode, false);
        restartCount = 0;
        for (URI hostUri : this.host.getNodeGroupMap().keySet()) {
            URI nodeGroupUri = UriUtils.buildUri(hostUri, ServiceUriPaths.DEFAULT_NODE_GROUP);
            ServiceStats nodeGroupStats = this.host.getServiceState(null, ServiceStats.class,
                    UriUtils.buildStatsUri(nodeGroupUri));
            ServiceStat restartStat = nodeGroupStats.entries
                    .get(NodeGroupService.STAT_NAME_RESTARTING_SERVICES_COUNT);
            if (restartStat != null) {
                restartCount += restartStat.latestValue;
            }
        }
        assertEquals("expected different number of service restarts", 0,
                restartCount);
    }
    @Test
    public void verifyGossipForObservers() throws Throwable {
        setUp(this.nodeCount);
        Iterator<Entry<URI, URI>> nodeGroupIterator = this.host.getNodeGroupMap().entrySet()
                .iterator();
        URI observerUri = nodeGroupIterator.next().getKey();
        String observerId = this.host.getServiceState(null,
                ServiceHostState.class,
                UriUtils.buildUri(observerUri, ServiceUriPaths.CORE_MANAGEMENT)).id;
        Map<URI, NodeState> selfStatePerNode = new HashMap<>();
        NodeState observerSelfState = new NodeState();
        observerSelfState.id = observerId;
        observerSelfState.options = EnumSet.of(NodeOption.OBSERVER);
        selfStatePerNode.put(observerUri, observerSelfState);
        this.host.createCustomNodeGroupOnPeers(CUSTOM_NODE_GROUP_NAME, selfStatePerNode);
        URI peerUri = nodeGroupIterator.next().getKey();
        URI peerCustomUri = UriUtils.buildUri(peerUri, CUSTOM_NODE_GROUP);
        Map<URI, EnumSet<NodeOption>> expectedOptionsPerNode = new HashMap<>();
        Set<URI> customNodeUris = new HashSet<>();
        for (Entry<URI, URI> node : this.host.getNodeGroupMap().entrySet()) {
            URI nodeUri = node.getKey();
            URI nodeCustomUri = UriUtils.buildUri(nodeUri, CUSTOM_NODE_GROUP);
            JoinPeerRequest request = new JoinPeerRequest();
            request.memberGroupReference = nodeCustomUri;
            TestContext ctx = this.host.testCreate(1);
            Operation post = Operation
                    .createPost(peerCustomUri)
                    .setBody(request)
                    .setReferer(this.host.getReferer())
                    .setCompletion(ctx.getCompletion());
            this.host.sendRequest(post);
            ctx.await();
            expectedOptionsPerNode.put(nodeCustomUri,
                    EnumSet.of((nodeUri == observerUri)
                            ? NodeOption.OBSERVER : NodeOption.PEER));
            customNodeUris.add(nodeCustomUri);
        }
        this.host.waitForNodeGroupConvergence(
                customNodeUris, this.nodeCount, this.nodeCount, expectedOptionsPerNode, false);
    }
    @Test
    public void synchronizationOneByOneWithAbruptNodeShutdown() throws Throwable {
        setUp(this.nodeCount);
        this.replicationTargetFactoryLink = PeriodicExampleFactoryService.SELF_LINK;
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.startServiceAndWait(PeriodicExampleFactoryService.class,
                    PeriodicExampleFactoryService.SELF_LINK);
        }
        VerificationHost initialHost = this.host.getPeerHost();
        URI hostUriWithInitialState = initialHost.getUri();
        Map<String, ExampleServiceState> exampleStatesPerSelfLink = createExampleServices(
                hostUriWithInitialState);
        URI hostWithStateNodeGroup = UriUtils.buildUri(hostUriWithInitialState,
                ServiceUriPaths.DEFAULT_NODE_GROUP);
        for (URI hostUri : this.host.getNodeGroupMap().keySet()) {
            waitForReplicatedFactoryServiceAvailable(
                    UriUtils.buildUri(hostUri, this.replicationTargetFactoryLink),
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        }
        List<URI> joinedHosts = new ArrayList<>();
        Map<URI, URI> factories = new HashMap<>();
        factories.put(hostWithStateNodeGroup, UriUtils.buildUri(hostWithStateNodeGroup,
                this.replicationTargetFactoryLink));
        joinedHosts.add(hostWithStateNodeGroup);
        int fullQuorum = 1;
        for (URI nodeGroupUri : this.host.getNodeGroupMap().values()) {
            if (hostWithStateNodeGroup.equals(nodeGroupUri)) {
                continue;
            }
            this.host.log("Setting quorum to %d, already joined: %d",
                    fullQuorum + 1, joinedHosts.size());
            this.host.setNodeGroupQuorum(++fullQuorum);
            this.host.testStart(1);
            this.host.joinNodeGroup(hostWithStateNodeGroup, nodeGroupUri, fullQuorum);
            this.host.testWait();
            joinedHosts.add(nodeGroupUri);
            factories.put(nodeGroupUri, UriUtils.buildUri(nodeGroupUri,
                    this.replicationTargetFactoryLink));
            this.host.waitForNodeGroupConvergence(joinedHosts, fullQuorum, fullQuorum, true);
            this.host.waitForNodeGroupIsAvailableConvergence(nodeGroupUri.getPath(), joinedHosts);
            this.waitForReplicatedFactoryChildServiceConvergence(
                    factories,
                    exampleStatesPerSelfLink,
                    this.exampleStateConvergenceChecker, exampleStatesPerSelfLink.size(),
                    0);
            doExampleServicePatch(exampleStatesPerSelfLink,
                    joinedHosts.get(0));
            Set<String> ownerIds = this.host.getNodeStateMap().keySet();
            verifyDocumentOwnerAndEpoch(exampleStatesPerSelfLink, initialHost, joinedHosts, 0, 1,
                    ownerIds.size() - 1);
        }
        doNodeStopWithUpdates(exampleStatesPerSelfLink);
    }
    private void doExampleServicePatch(Map<String, ExampleServiceState> states,
            URI nodeGroupOnSomeHost) throws Throwable {
        this.host.log("Starting PATCH to %d example services", states.size());
        TestContext ctx = this.host
                .testCreate(this.updateCount * states.size());
        this.setOperationTimeoutMicros(TimeUnit.SECONDS.toMicros(this.host.getTimeoutSeconds()));
        for (int i = 0; i < this.updateCount; i++) {
            for (Entry<String, ExampleServiceState> e : states.entrySet()) {
                ExampleServiceState st = Utils.clone(e.getValue());
                st.counter = (long) i;
                Operation patch = Operation
                        .createPatch(UriUtils.buildUri(nodeGroupOnSomeHost, e.getKey()))
                        .setCompletion(ctx.getCompletion())
                        .setBody(st);
                this.host.send(patch);
            }
        }
        this.host.testWait(ctx);
        this.host.log("Done with PATCH to %d example services", states.size());
    }
    public void doNodeStopWithUpdates(Map<String, ExampleServiceState> exampleStatesPerSelfLink)
            throws Throwable {
        this.host.log("Starting to stop nodes and send updates");
        VerificationHost remainingHost = this.host.getPeerHost();
        Collection<VerificationHost> hostsToStop = new ArrayList<>(this.host.getInProcessHostMap()
                .values());
        hostsToStop.remove(remainingHost);
        List<URI> targetServices = new ArrayList<>();
        for (String link : exampleStatesPerSelfLink.keySet()) {
            targetServices.add(UriUtils.buildUri(remainingHost, link));
        }
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.setPeerSynchronizationTimeLimitSeconds(this.host.getTimeoutSeconds() / 3);
        }
        Map<URI, ServiceStats> prevStats = verifyMaintStatsAfterSynchronization(targetServices,
                null);
        stopHostsAndVerifyQueuing(hostsToStop, remainingHost, targetServices);
        Set<String> ownerIds = this.host.getNodeStateMap().keySet();
        List<URI> remainingHosts = new ArrayList<>(this.host.getNodeGroupMap().keySet());
        verifyDocumentOwnerAndEpoch(exampleStatesPerSelfLink,
                this.host.getInProcessHostMap().values().iterator().next(),
                remainingHosts, 0, 1,
                ownerIds.size() - 1);
        verifyMaintStatsAfterSynchronization(targetServices, prevStats);
        doExampleServicePatch(exampleStatesPerSelfLink, remainingHost.getUri());
        this.host.log("Done with stop nodes and send updates");
    }
    private void verifyDynamicMaintOptionToggle(Map<String, ExampleServiceState> childStates) {
        List<URI> targetServices = new ArrayList<>();
        childStates.keySet().forEach((l) -> targetServices.add(this.host.getPeerServiceUri(l)));
        List<URI> targetServiceStats = new ArrayList<>();
        List<URI> targetServiceConfig = new ArrayList<>();
        for (URI child : targetServices) {
            targetServiceStats.add(UriUtils.buildStatsUri(child));
            targetServiceConfig.add(UriUtils.buildConfigUri(child));
        }
        Map<URI, ServiceConfiguration> configPerService = this.host.getServiceState(
                null, ServiceConfiguration.class, targetServiceConfig);
        for (ServiceConfiguration cfg : configPerService.values()) {
            assertTrue(!cfg.options.contains(ServiceOption.PERIODIC_MAINTENANCE));
        }
        for (URI child : targetServices) {
            this.host.toggleServiceOptions(child,
                    EnumSet.of(ServiceOption.PERIODIC_MAINTENANCE),
                    null);
        }
        verifyMaintStatsAfterSynchronization(targetServices, null);
    }
    private Map<URI, ServiceStats> verifyMaintStatsAfterSynchronization(List<URI> targetServices,
            Map<URI, ServiceStats> statsPerService) {
        List<URI> targetServiceStats = new ArrayList<>();
        List<URI> targetServiceConfig = new ArrayList<>();
        for (URI child : targetServices) {
            targetServiceStats.add(UriUtils.buildStatsUri(child));
            targetServiceConfig.add(UriUtils.buildConfigUri(child));
        }
        if (statsPerService == null) {
            statsPerService = new HashMap<>();
        }
        final Map<URI, ServiceStats> previousStatsPerService = statsPerService;
        this.host.waitFor(
                "maintenance not enabled",
                () -> {
                    Map<URI, ServiceStats> stats = this.host.getServiceState(null,
                            ServiceStats.class, targetServiceStats);
                    for (Entry<URI, ServiceStats> currentEntry : stats.entrySet()) {
                        ServiceStats previousStats = previousStatsPerService.get(currentEntry
                                .getKey());
                        ServiceStats currentStats = currentEntry.getValue();
                        ServiceStat previousMaintStat = previousStats == null ? new ServiceStat()
                                : previousStats.entries
                                .get(Service.STAT_NAME_MAINTENANCE_COUNT);
                        double previousValue = previousMaintStat == null ? 0L
                                : previousMaintStat.latestValue;
                        ServiceStat maintStat = currentStats.entries
                                .get(Service.STAT_NAME_MAINTENANCE_COUNT);
                        if (maintStat == null || maintStat.latestValue <= previousValue) {
                            return false;
                        }
                    }
                    previousStatsPerService.putAll(stats);
                    return true;
                });
        return statsPerService;
    }
    private Map<String, ExampleServiceState> createExampleServices(URI hostUri) throws Throwable {
        URI factoryUri = UriUtils.buildUri(hostUri, this.replicationTargetFactoryLink);
        this.host.log("POSTing children to %s", hostUri);
        Map<URI, ExampleServiceState> exampleStates = this.host.doFactoryChildServiceStart(
                null,
                this.serviceCount,
                ExampleServiceState.class,
                (o) -> {
                    ExampleServiceState s = new ExampleServiceState();
                    s.name = UUID.randomUUID().toString();
                    o.setBody(s);
                }, factoryUri);
        Map<String, ExampleServiceState> exampleStatesPerSelfLink = new HashMap<>();
        for (ExampleServiceState s : exampleStates.values()) {
            exampleStatesPerSelfLink.put(s.documentSelfLink, s);
        }
        return exampleStatesPerSelfLink;
    }
    @Test
    public void synchronizationWithPeerNodeListAndDuplicates()
            throws Throwable {
        ExampleServiceHost h = null;
        TemporaryFolder tmpFolder = new TemporaryFolder();
        tmpFolder.create();
        try {
            setUp(this.nodeCount);
            this.host.setNodeGroupQuorum(1);
            Map<String, ExampleServiceState> exampleStatesPerSelfLink = new HashMap<>();
            int dupServiceCount = this.serviceCount;
            AtomicInteger counter = new AtomicInteger();
            Map<URI, ExampleServiceState> dupStates = new HashMap<>();
            for (VerificationHost v : this.host.getInProcessHostMap().values()) {
                counter.set(0);
                URI factoryUri = UriUtils.buildFactoryUri(v,
                        ExampleService.class);
                dupStates = this.host.doFactoryChildServiceStart(
                        null,
                        dupServiceCount,
                        ExampleServiceState.class,
                        (o) -> {
                            ExampleServiceState s = new ExampleServiceState();
                            s.documentSelfLink = "duplicateExampleInstance-"
                                    + counter.incrementAndGet();
                            s.name = s.documentSelfLink;
                            o.setBody(s);
                        }, factoryUri);
            }
            for (ExampleServiceState s : dupStates.values()) {
                exampleStatesPerSelfLink.put(s.documentSelfLink, s);
            }
            this.serviceCount = exampleStatesPerSelfLink.size();
            Collection<URI> peerNodeGroupUris = new ArrayList<>();
            StringBuilder peerNodes = new StringBuilder();
            for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
                peerNodeGroupUris.add(UriUtils.buildUri(peer, ServiceUriPaths.DEFAULT_NODE_GROUP));
                peerNodes.append(peer.getUri().toString()).append(",");
            }
            CountDownLatch notifications = new CountDownLatch(this.nodeCount);
            for (URI nodeGroup : this.host.getNodeGroupMap().values()) {
                this.host.subscribeForNodeGroupConvergence(nodeGroup, this.nodeCount + 1,
                        (o, e) -> {
                            if (e != null) {
                                this.host.log("Error in notificaiton: %s", Utils.toString(e));
                                return;
                            }
                            notifications.countDown();
                        });
            }
            h = new ExampleServiceHost();
            int quorum = this.host.getPeerCount() + 1;
            String mainHostId = "main-" + VerificationHost.hostNumber.incrementAndGet();
            String[] args = {
                    "--port=0",
                    "--id=" + mainHostId,
                    "--bindAddress=127.0.0.1",
                    "--sandbox="
                            + tmpFolder.getRoot().getAbsolutePath(),
                    "--peerNodes=" + peerNodes.toString()
            };
            h.initialize(args);
            h.setPeerSynchronizationEnabled(this.isPeerSynchronizationEnabled);
            h.setMaintenanceIntervalMicros(TimeUnit.MILLISECONDS
                    .toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS));
            h.start();
            URI mainHostNodeGroupUri = UriUtils.buildUri(h, ServiceUriPaths.DEFAULT_NODE_GROUP);
            int totalCount = this.nodeCount + 1;
            peerNodeGroupUris.add(mainHostNodeGroupUri);
            this.host.waitForNodeGroupIsAvailableConvergence();
            this.host.waitForNodeGroupConvergence(peerNodeGroupUris, totalCount,
                    totalCount, true);
            this.host.setNodeGroupQuorum(quorum, mainHostNodeGroupUri);
            this.host.setNodeGroupQuorum(quorum);
            this.host.scheduleSynchronizationIfAutoSyncDisabled(this.replicationNodeSelector);
            int peerNodeCount = h.getInitialPeerHosts().size();
            assertTrue(totalCount >= peerNodeCount + 1);
            verifyReplicatedInConflictPost(dupStates);
            waitForReplicatedFactoryChildServiceConvergence(
                    exampleStatesPerSelfLink,
                    this.exampleStateConvergenceChecker,
                    this.serviceCount, 0);
            doStateUpdateReplicationTest(Action.PATCH, this.serviceCount, this.updateCount, 0,
                    this.exampleStateUpdateBodySetter,
                    this.exampleStateConvergenceChecker,
                    exampleStatesPerSelfLink);
            URI exampleFactoryUri = this.host.getPeerServiceUri(ExampleService.FACTORY_LINK);
            waitForReplicatedFactoryServiceAvailable(
                    UriUtils.buildUri(exampleFactoryUri, ExampleService.FACTORY_LINK),
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        } finally {
            this.host.log("test finished");
            if (h != null) {
                h.stop();
                tmpFolder.delete();
            }
        }
    }
    private void verifyReplicatedInConflictPost(Map<URI, ExampleServiceState> dupStates)
            throws Throwable {
        Thread.sleep(TimeUnit.MICROSECONDS.toMillis(
                this.host.getPeerHost().getMaintenanceIntervalMicros()));
        TestContext ctx = this.host.testCreate(dupStates.size());
        for (ExampleServiceState st : dupStates.values()) {
            URI factoryUri = this.host.getPeerServiceUri(ExampleService.FACTORY_LINK);
            Operation post = Operation.createPost(factoryUri).setBody(st)
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            if (o.getStatusCode() != Operation.STATUS_CODE_CONFLICT) {
                                ctx.failIteration(new IllegalStateException(
                                        "Expected conflict status, got " + o.getStatusCode()));
                                return;
                            }
                            ctx.completeIteration();
                            return;
                        }
                        ctx.failIteration(new IllegalStateException(
                                "Expected failure on duplicate POST"));
                    });
            this.host.send(post);
        }
        this.host.testWait(ctx);
    }
    @Test
    public void replicationWithQuorumAfterAbruptNodeStopOnDemandLoad() throws Throwable {
        tearDown();
        for (int i = 0; i < this.testIterationCount; i++) {
            setUpOnDemandLoad();
            int hostStopCount = 2;
            doReplicationWithQuorumAfterAbruptNodeStop(hostStopCount);
            this.host.log("Done with iteration %d", i);
            tearDown();
            this.host = null;
        }
    }
    private void doReplicationWithQuorumAfterAbruptNodeStop(int hostStopCount)
            throws Throwable {
        Map<String, ExampleServiceState> childStates = doExampleFactoryPostReplicationTest(
                this.serviceCount, null, null);
        updateExampleServiceOptions(childStates);
        int i = 0;
        for (Entry<URI, VerificationHost> e : this.host.getInProcessHostMap().entrySet()) {
            this.expectedFailedHosts.add(e.getKey());
            this.host.stopHost(e.getValue());
            if (++i >= hostStopCount) {
                break;
            }
        }
        int expectedVersion = this.updateCount;
        childStates = doStateUpdateReplicationTest(Action.PATCH, this.serviceCount,
                this.updateCount,
                expectedVersion,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
    }
    @Test
    public void replicationWithQuorumAfterAbruptNodeStopMultiLocation()
            throws Throwable {
        this.nodeCount = 6;
        this.isPeerSynchronizationEnabled = true;
        this.skipAvailabilityChecks = true;
        this.isMultiLocationTest = true;
        if (this.host == null) {
            setUp(this.nodeCount);
            this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
            this.host.setNodeGroupQuorum(2);
        }
        Map<String, ExampleServiceState> childStates = doExampleFactoryPostReplicationTest(
                this.serviceCount, null, null);
        updateExampleServiceOptions(childStates);
        for (Entry<URI, VerificationHost> e : this.host.getInProcessHostMap().entrySet()) {
            VerificationHost h = e.getValue();
            if (h.getLocation().equals(VerificationHost.LOCATION2)) {
                this.expectedFailedHosts.add(e.getKey());
                this.host.stopHost(h);
            }
        }
        int expectedVersion = this.updateCount;
        childStates = doStateUpdateReplicationTest(Action.PATCH, this.serviceCount,
                this.updateCount,
                expectedVersion,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
    }
    @Test
    public void nodeRestartWithSameAddressDifferentId() throws Throwable {
        int failedNodeCount = 1;
        int afterFailureQuorum = this.nodeCount - failedNodeCount;
        setUp(this.nodeCount);
        setOperationTimeoutMicros(TimeUnit.SECONDS.toMicros(5));
        this.host.joinNodesAndVerifyConvergence(this.nodeCount);
        this.host.log("Stopping node");
        this.host.setNodeGroupQuorum(afterFailureQuorum);
        List<ServiceHostState> hostStates = stopHostsToSimulateFailure(failedNodeCount);
        URI remainingPeerNodeGroup = this.host.getPeerNodeGroupUri();
        this.host.waitForNodeGroupConvergence(this.nodeCount - failedNodeCount);
        ServiceHostState stoppedHostState = hostStates.get(0);
        this.host.testStart(1);
        VerificationHost newHost = this.host.setUpLocalPeerHost(stoppedHostState.httpPort,
                VerificationHost.FAST_MAINT_INTERVAL_MILLIS, null);
        this.host.testWait();
        URI newHostNodeGroupService = UriUtils
                .buildUri(newHost.getUri(), ServiceUriPaths.DEFAULT_NODE_GROUP);
        this.host.testStart(1);
        this.host.joinNodeGroup(newHostNodeGroupService, remainingPeerNodeGroup);
        this.host.testWait();
        this.host.waitForNodeGroupConvergence(this.nodeCount);
    }
    public void setMaintenanceIntervalMillis(long defaultMaintIntervalMillis) {
        for (VerificationHost h1 : this.host.getInProcessHostMap().values()) {
            h1.setMaintenanceIntervalMicros(TimeUnit.MILLISECONDS
                    .toMicros(defaultMaintIntervalMillis));
        }
    }
    @Test
    public void synchronizationRequestQueuing() throws Throwable {
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        this.host.setNodeGroupQuorum(this.nodeCount);
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(ExampleService.FACTORY_LINK),
                ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        waitForReplicationFactoryConvergence();
        VerificationHost peerHost = this.host.getPeerHost();
        List<URI> exampleUris = new ArrayList<>();
        this.host.createExampleServices(peerHost, 1, exampleUris, null);
        URI instanceUri = exampleUris.get(0);
        ExampleServiceState synchState = new ExampleServiceState();
        synchState.documentSelfLink = UriUtils.getLastPathSegment(instanceUri);
        TestContext ctx = this.host.testCreate(this.updateCount);
        for (int i = 0; i < this.updateCount; i++) {
            Operation op = Operation.createPost(peerHost, ExampleService.FACTORY_LINK)
                    .setBody(synchState)
                    .addPragmaDirective(Operation.PRAGMA_DIRECTIVE_SYNCH_OWNER)
                    .setReferer(this.host.getUri())
                    .setCompletion(ctx.getCompletion());
            this.host.sendRequest(op);
        }
        ctx.await();
    }
    @Test
    public void enforceHighQuorumWithNodeConcurrentStop()
            throws Throwable {
        int hostRestartCount = 2;
        Map<String, ExampleServiceState> childStates = doExampleFactoryPostReplicationTest(
                this.serviceCount, null, null);
        updateExampleServiceOptions(childStates);
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.setPeerSynchronizationTimeLimitSeconds(1);
        }
        this.host.setNodeGroupConfig(this.nodeGroupConfig);
        this.host.setNodeGroupQuorum((this.nodeCount + 1) / 2);
        childStates = doStateUpdateReplicationTest(Action.PATCH, this.serviceCount,
                this.updateCount,
                0,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
        long now = Utils.getNowMicrosUtc();
        validatePerOperationReplicationQuorum(childStates, now);
        this.expectFailure = true;
        long opTimeoutMicros = TimeUnit.MILLISECONDS.toMicros(500);
        setOperationTimeoutMicros(opTimeoutMicros);
        int i = 0;
        for (URI h : this.host.getInProcessHostMap().keySet()) {
            this.expectedFailedHosts.add(h);
            if (++i >= hostRestartCount) {
                break;
            }
        }
        stopHostsToSimulateFailure(1);
        Runnable r = () -> {
            stopHostsToSimulateFailure(hostRestartCount - 1);
            this.expectedFailureStartTimeMicros = Utils.getNowMicrosUtc()
                    + TimeUnit.MILLISECONDS.toMicros(250);
        };
        this.host.schedule(r, 1, TimeUnit.MILLISECONDS);
        childStates = doStateUpdateReplicationTest(Action.PATCH, this.serviceCount,
                this.updateCount,
                this.updateCount,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
        doStateUpdateReplicationTest(Action.PATCH, childStates.size(), this.updateCount,
                this.updateCount * 2,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
        doStateUpdateReplicationTest(Action.PATCH, childStates.size(), 1,
                this.updateCount * 2,
                this.exampleStateUpdateBodySetter,
                this.exampleStateConvergenceChecker,
                childStates);
    }
    private void validatePerOperationReplicationQuorum(Map<String, ExampleServiceState> childStates,
            long now) throws Throwable {
        Random r = new Random();
        for (Entry<String, ExampleServiceState> e : childStates.entrySet()) {
            TestContext ctx = this.host.testCreate(1);
            ExampleServiceState body = e.getValue();
            body.counter = now;
            Operation patch = Operation.createPatch(this.host.getPeerServiceUri(e.getKey()))
                    .setCompletion(ctx.getCompletion())
                    .setBody(body);
            if (r.nextBoolean()) {
                patch.addRequestHeader(Operation.REPLICATION_QUORUM_HEADER,
                        Operation.REPLICATION_QUORUM_HEADER_VALUE_ALL);
            } else {
                patch.addRequestHeader(Operation.REPLICATION_QUORUM_HEADER,
                        this.nodeCount + "");
            }
            this.host.send(patch);
            this.host.testWait(ctx);
            for (URI hostBaseUri : this.host.getNodeGroupMap().keySet()) {
                URI indexUri = UriUtils.buildUri(hostBaseUri, ServiceUriPaths.CORE_DOCUMENT_INDEX);
                indexUri = UriUtils.buildIndexQueryUri(indexUri,
                        e.getKey(), true, false, ServiceOption.PERSISTENCE);
                ExampleServiceState afterState = this.host.getServiceState(null,
                        ExampleServiceState.class, indexUri);
                assertEquals(body.counter, afterState.counter);
            }
        }
        this.host.toggleNegativeTestMode(true);
        for (Entry<String, ExampleServiceState> e : childStates.entrySet()) {
            TestContext ctx = this.host.testCreate(1);
            ExampleServiceState body = e.getValue();
            body.counter = now;
            Operation patch = Operation.createPatch(this.host.getPeerServiceUri(e.getKey()))
                    .addRequestHeader(Operation.REPLICATION_QUORUM_HEADER,
                            (this.nodeCount * 2) + "")
                    .setCompletion(ctx.getExpectedFailureCompletion())
                    .setBody(body);
            this.host.send(patch);
            this.host.testWait(ctx);
            break;
        }
        this.host.toggleNegativeTestMode(false);
    }
    private void setOperationTimeoutMicros(long opTimeoutMicros) {
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            h.setOperationTimeOutMicros(opTimeoutMicros);
        }
        this.host.setOperationTimeOutMicros(opTimeoutMicros);
    }
    @Test
    public void replicationWithCrossServiceDependencies() throws Throwable {
        this.isPeerSynchronizationEnabled = false;
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        Consumer<Operation> setBodyCallback = (o) -> {
            ReplicationTestServiceState s = new ReplicationTestServiceState();
            s.stringField = UUID.randomUUID().toString();
            o.setBody(s);
        };
        URI hostUri = this.host.getPeerServiceUri(null);
        URI factoryUri = UriUtils.buildUri(hostUri,
                ReplicationFactoryTestService.SIMPLE_REPL_SELF_LINK);
        doReplicatedServiceFactoryPost(this.serviceCount, setBodyCallback, factoryUri);
        factoryUri = UriUtils.buildUri(hostUri,
                ReplicationFactoryTestService.OWNER_SELECTION_SELF_LINK);
        Map<URI, ReplicationTestServiceState> ownerSelectedServices = doReplicatedServiceFactoryPost(
                this.serviceCount, setBodyCallback, factoryUri);
        factoryUri = UriUtils.buildUri(hostUri, ReplicationFactoryTestService.STRICT_SELF_LINK);
        doReplicatedServiceFactoryPost(this.serviceCount, setBodyCallback, factoryUri);
        QueryTask.QuerySpecification q = new QueryTask.QuerySpecification();
        Query kindClause = new Query();
        kindClause.setTermPropertyName(ServiceDocument.FIELD_NAME_KIND)
                .setTermMatchValue(Utils.buildKind(ReplicationTestServiceState.class));
        q.query.addBooleanClause(kindClause);
        Query nameClause = new Query();
        nameClause.setTermPropertyName("stringField")
                .setTermMatchValue("*")
                .setTermMatchType(MatchType.WILDCARD);
        q.query.addBooleanClause(nameClause);
        int expectedServiceCount = this.serviceCount * 3;
        Date exp = this.host.getTestExpiration();
        while (exp.after(new Date())) {
            int count = 10;
            URI queryFactoryUri = UriUtils.extendUri(hostUri,
                    ServiceUriPaths.CORE_QUERY_TASKS);
            TestContext testContext = this.host.testCreate(count);
            Map<String, QueryTask> taskResults = new ConcurrentSkipListMap<>();
            for (int i = 0; i < count; i++) {
                QueryTask qt = QueryTask.create(q);
                qt.taskInfo.isDirect = true;
                qt.documentSelfLink = UUID.randomUUID().toString();
                Operation startPost = Operation
                        .createPost(queryFactoryUri)
                        .setBody(qt)
                        .setCompletion(
                                (o, e) -> {
                                    if (e != null) {
                                        testContext.fail(e);
                                        return;
                                    }
                                    QueryTask rsp = o.getBody(QueryTask.class);
                                    qt.results = rsp.results;
                                    qt.documentOwner = rsp.documentOwner;
                                    taskResults.put(rsp.documentSelfLink, qt);
                                    testContext.complete();
                                });
                this.host.send(startPost);
            }
            testContext.await();
            this.host.logThroughput();
            boolean converged = true;
            for (QueryTask qt : taskResults.values()) {
                if (qt.results == null || qt.results.documentLinks == null) {
                    throw new IllegalStateException("Missing results");
                }
                if (qt.results.documentLinks.size() != expectedServiceCount) {
                    this.host.log("%s", Utils.toJsonHtml(qt));
                    converged = false;
                    break;
                }
            }
            if (!converged) {
                Thread.sleep(250);
                continue;
            }
            break;
        }
        if (exp.before(new Date())) {
            throw new TimeoutException();
        }
        URI childUri = ownerSelectedServices.keySet().iterator().next();
        TestContext testContext = this.host.testCreate(1);
        ReplicationTestServiceState badRequestBody = new ReplicationTestServiceState();
        this.host
                .send(Operation
                        .createPatch(childUri)
                        .setBody(badRequestBody)
                        .setCompletion(
                                (o, e) -> {
                                    if (e == null) {
                                        testContext.fail(new IllegalStateException(
                                                "Expected failure"));
                                        return;
                                    }
                                    ReplicationTestServiceErrorResponse rsp = o
                                            .getBody(ReplicationTestServiceErrorResponse.class);
                                    if (!ReplicationTestServiceErrorResponse.KIND
                                            .equals(rsp.documentKind)) {
                                        testContext.fail(new IllegalStateException(
                                                "Expected custom response body"));
                                        return;
                                    }
                                    testContext.complete();
                                }));
        testContext.await();
        Map<URI, ReplicationTestServiceState> latestState = this.host.getServiceState(null,
                ReplicationTestServiceState.class, ownerSelectedServices.keySet());
        Map<String, String> ownerIdPerLink = new HashMap<>();
        List<URI> statsUris = new ArrayList<>();
        for (ReplicationTestServiceState state : latestState.values()) {
            URI statsUri = this.host.getPeerServiceUri(UriUtils.buildUriPath(
                    state.documentSelfLink, ServiceHost.SERVICE_URI_SUFFIX_STATS));
            ownerIdPerLink.put(state.documentSelfLink, state.documentOwner);
            statsUris.add(statsUri);
        }
        Map<URI, ServiceStats> latestStats = this.host.getServiceState(null, ServiceStats.class,
                statsUris);
        for (ServiceStats perServiceStats : latestStats.values()) {
            String serviceLink = UriUtils.getParentPath(perServiceStats.documentSelfLink);
            String expectedOwnerId = ownerIdPerLink.get(serviceLink);
            if (expectedOwnerId.equals(perServiceStats.documentOwner)) {
                continue;
            }
            throw new IllegalStateException("owner routing issue with stats: "
                    + Utils.toJsonHtml(perServiceStats));
        }
        exp = this.host.getTestExpiration();
        while (new Date().before(exp)) {
            boolean isConverged = true;
            for (VerificationHost peerHost : this.host.getInProcessHostMap().values()) {
                factoryUri = UriUtils.buildUri(peerHost,
                        ReplicationFactoryTestService.SIMPLE_REPL_SELF_LINK);
                ServiceDocumentQueryResult rsp = this.host.getFactoryState(factoryUri);
                if (rsp.documentLinks.size() != latestState.size()) {
                    this.host.log("Factory %s reporting %d children, expected %d", factoryUri,
                            rsp.documentLinks.size(), latestState.size());
                    isConverged = false;
                    break;
                }
            }
            if (!isConverged) {
                Thread.sleep(250);
                continue;
            }
            break;
        }
        if (new Date().after(exp)) {
            throw new TimeoutException("factories did not converge");
        }
        this.host.log("Inducing synchronization");
        this.host.scheduleSynchronizationIfAutoSyncDisabled(this.replicationNodeSelector);
        Thread.sleep(2000);
        Map<URI, ReplicationTestServiceState> latestStateAfter = this.host.getServiceState(null,
                ReplicationTestServiceState.class, ownerSelectedServices.keySet());
        for (Entry<URI, ReplicationTestServiceState> afterEntry : latestStateAfter.entrySet()) {
            ReplicationTestServiceState beforeState = latestState.get(afterEntry.getKey());
            ReplicationTestServiceState afterState = afterEntry.getValue();
            assertEquals(beforeState.documentVersion, afterState.documentVersion);
        }
        verifyOperationJoinAcrossPeers(latestStateAfter);
    }
    private Map<URI, ReplicationTestServiceState> doReplicatedServiceFactoryPost(int serviceCount,
            Consumer<Operation> setBodyCallback, URI factoryUri) throws Throwable,
            InterruptedException, TimeoutException {
        ServiceDocumentDescription sdd = this.host
                .buildDescription(ReplicationTestServiceState.class);
        Map<URI, ReplicationTestServiceState> serviceMap = this.host.doFactoryChildServiceStart(
                null, serviceCount, ReplicationTestServiceState.class, setBodyCallback, factoryUri);
        Date expiration = this.host.getTestExpiration();
        boolean isConverged = true;
        Map<URI, String> uriToSignature = new HashMap<>();
        while (new Date().before(expiration)) {
            isConverged = true;
            uriToSignature.clear();
            for (Entry<URI, VerificationHost> e : this.host.getInProcessHostMap().entrySet()) {
                URI baseUri = e.getKey();
                VerificationHost h = e.getValue();
                URI u = UriUtils.buildUri(baseUri, factoryUri.getPath());
                u = UriUtils.buildExpandLinksQueryUri(u);
                ServiceDocumentQueryResult r = this.host.getFactoryState(u);
                if (r.documents.size() != serviceCount) {
                    this.host.log("instance count mismatch, expected %d, got %d, from %s",
                            serviceCount, r.documents.size(), u);
                    isConverged = false;
                    break;
                }
                for (URI instanceUri : serviceMap.keySet()) {
                    ReplicationTestServiceState initialState = serviceMap.get(instanceUri);
                    ReplicationTestServiceState newState = Utils.fromJson(
                            r.documents.get(instanceUri.getPath()),
                            ReplicationTestServiceState.class);
                    if (newState.documentVersion == 0) {
                        this.host.log("version mismatch, expected %d, got %d, from %s", 0,
                                newState.documentVersion, instanceUri);
                        isConverged = false;
                        break;
                    }
                    if (initialState.stringField.equals(newState.stringField)) {
                        this.host.log("field mismatch, expected %s, got %s, from %s",
                                initialState.stringField, newState.stringField, instanceUri);
                        isConverged = false;
                        break;
                    }
                    if (newState.queryTaskLink == null) {
                        this.host.log("missing query task link from %s", instanceUri);
                        isConverged = false;
                        break;
                    }
                    if (!newState.documentSelfLink
                            .contains(ReplicationFactoryTestService.STRICT_SELF_LINK)
                            && !newState.documentSelfLink
                            .contains(ReplicationFactoryTestService.SIMPLE_REPL_SELF_LINK)
                            && !newState.stringField.equals(newState.documentSelfLink)) {
                        this.host.log("State not in final state");
                        isConverged = false;
                        break;
                    }
                    String sig = uriToSignature.get(instanceUri);
                    if (sig == null) {
                        sig = Utils.computeSignature(newState, sdd);
                        uriToSignature.put(instanceUri, sig);
                    } else {
                        String newSig = Utils.computeSignature(newState, sdd);
                        if (!sig.equals(newSig)) {
                            isConverged = false;
                            this.host.log("signature mismatch, expected %s, got %s, from %s",
                                    sig, newSig, instanceUri);
                        }
                    }
                    ProcessingStage ps = h.getServiceStage(newState.queryTaskLink);
                    if (ps == null || ps != ProcessingStage.AVAILABLE) {
                        this.host.log("missing query task service from %s", newState.queryTaskLink,
                                instanceUri);
                        isConverged = false;
                        break;
                    }
                }
                if (isConverged == false) {
                    break;
                }
            }
            if (isConverged == true) {
                break;
            }
            Thread.sleep(100);
        }
        if (!isConverged) {
            throw new TimeoutException("States did not converge");
        }
        return serviceMap;
    }
    @Test
    public void replicationWithOutOfOrderPostAndUpdates() throws Throwable {
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        this.host.setNodeGroupQuorum(1);
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(ExampleService.FACTORY_LINK),
                ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        waitForReplicationFactoryConvergence();
        ExampleServiceState state = new ExampleServiceState();
        state.name = "testing";
        state.counter = 1L;
        VerificationHost peer = this.host.getPeerHost();
        TestContext ctx = this.host.testCreate(this.serviceCount * this.updateCount);
        for (int i = 0; i < this.serviceCount; i++) {
            Operation post = Operation
                    .createPost(peer, ExampleService.FACTORY_LINK)
                    .setBody(state)
                    .setReferer(this.host.getUri())
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            ctx.failIteration(e);
                            return;
                        }
                        ExampleServiceState rsp = o.getBody(ExampleServiceState.class);
                        for (int k = 0; k < this.updateCount; k++) {
                            ExampleServiceState update = new ExampleServiceState();
                            state.counter = (long) k;
                            Operation patch = Operation
                                    .createPatch(peer, rsp.documentSelfLink)
                                    .setBody(update)
                                    .setReferer(this.host.getUri())
                                    .setCompletion(ctx.getCompletion());
                            this.host.sendRequest(patch);
                        }
                    });
            this.host.sendRequest(post);
        }
        ctx.await();
    }
    @Test
    public void replication() throws Throwable {
        this.replicationTargetFactoryLink = ExampleService.FACTORY_LINK;
        doReplication();
    }
    @Test
    public void replicationSsl() throws Throwable {
        this.replicationUriScheme = ServiceHost.HttpScheme.HTTPS_ONLY;
        this.replicationTargetFactoryLink = ExampleService.FACTORY_LINK;
        doReplication();
    }
    @Test
    public void replication1x() throws Throwable {
        this.replicationFactor = 1L;
        this.replicationNodeSelector = ServiceUriPaths.DEFAULT_1X_NODE_SELECTOR;
        this.replicationTargetFactoryLink = Replication1xExampleFactoryService.SELF_LINK;
        doReplication();
    }
    @Test
    public void replication3x() throws Throwable {
        this.replicationFactor = 3L;
        this.replicationNodeSelector = ServiceUriPaths.DEFAULT_3X_NODE_SELECTOR;
        this.replicationTargetFactoryLink = Replication3xExampleFactoryService.SELF_LINK;
        this.nodeCount = Math.max(5, this.nodeCount);
        doReplication();
    }
    private void doReplication() throws Throwable {
        this.isPeerSynchronizationEnabled = false;
        CommandLineArgumentParser.parseFromProperties(this);
        Date expiration = new Date();
        if (this.testDurationSeconds > 0) {
            expiration = new Date(expiration.getTime()
                    + TimeUnit.SECONDS.toMillis(this.testDurationSeconds));
        }
        Map<Action, Long> elapsedTimePerAction = new HashMap<>();
        Map<Action, Long> countPerAction = new HashMap<>();
        long totalOperations = 0;
        int iterationCount = 0;
        do {
            if (this.host == null) {
                setUp(this.nodeCount);
                this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
                this.host.setNodeGroupQuorum(this.nodeCount);
                this.host.scheduleSynchronizationIfAutoSyncDisabled(this.replicationNodeSelector);
                waitForReplicatedFactoryServiceAvailable(
                        this.host.getPeerServiceUri(this.replicationTargetFactoryLink),
                        this.replicationNodeSelector);
                waitForReplicationFactoryConvergence();
                if (this.replicationUriScheme == ServiceHost.HttpScheme.HTTPS_ONLY) {
                    for (URI nodeGroup : this.host.getNodeGroupMap().values()) {
                        assertTrue(UriUtils.HTTPS_SCHEME.equals(nodeGroup.getScheme()));
                    }
                }
            }
            Map<String, ExampleServiceState> childStates = doExampleFactoryPostReplicationTest(
                    this.serviceCount, countPerAction, elapsedTimePerAction);
            totalOperations += this.serviceCount;
            if (this.testDurationSeconds == 0) {
                this.host.doExampleServiceUpdateAndQueryByVersion(this.host.getPeerHostUri(),
                        this.serviceCount);
                verifyReplicatedForcedPostAfterDelete(childStates);
                verifyInstantNotFoundFailureOnBadLinks();
                verifyReplicatedIdempotentPost(childStates);
                verifyDynamicMaintOptionToggle(childStates);
            }
            totalOperations += this.serviceCount;
            if (expiration == null) {
                expiration = this.host.getTestExpiration();
            }
            int expectedVersion = this.updateCount;
            if (!this.host.isStressTest()
                    && (this.host.getPeerCount() > 16
                    || this.serviceCount * this.updateCount > 100)) {
                this.host.setStressTest(true);
            }
            long opCount = this.serviceCount * this.updateCount;
            childStates = doStateUpdateReplicationTest(Action.PATCH, this.serviceCount,
                    this.updateCount,
                    expectedVersion,
                    this.exampleStateUpdateBodySetter,
                    this.exampleStateConvergenceChecker,
                    childStates,
                    countPerAction,
                    elapsedTimePerAction);
            expectedVersion += this.updateCount;
            totalOperations += opCount;
            childStates = doStateUpdateReplicationTest(Action.PUT, this.serviceCount,
                    this.updateCount,
                    expectedVersion,
                    this.exampleStateUpdateBodySetter,
                    this.exampleStateConvergenceChecker,
                    childStates,
                    countPerAction,
                    elapsedTimePerAction);
            totalOperations += opCount;
            Date queryExp = this.host.getTestExpiration();
            if (expiration.after(queryExp)) {
                queryExp = expiration;
            }
            while (new Date().before(queryExp)) {
                Set<String> links = verifyReplicatedServiceCountWithBroadcastQueries();
                if (links.size() < this.serviceCount) {
                    this.host.log("Found only %d links across nodes, retrying", links.size());
                    Thread.sleep(500);
                    continue;
                }
                break;
            }
            totalOperations += this.serviceCount;
            if (queryExp.before(new Date())) {
                throw new TimeoutException();
            }
            expectedVersion += 1;
            doStateUpdateReplicationTest(Action.DELETE, this.serviceCount, 1,
                    expectedVersion,
                    this.exampleStateUpdateBodySetter,
                    this.exampleStateConvergenceChecker,
                    childStates,
                    countPerAction,
                    elapsedTimePerAction);
            totalOperations += this.serviceCount;
            ExampleServiceState st = childStates.values().iterator().next();
            String json = Utils.toJson(st);
            int byteCount = KryoSerializers.serializeDocument(st, 4096).position();
            int jsonByteCount = json.getBytes(Utils.CHARSET).length;
            long totalBytes = jsonByteCount * totalOperations
                    + (this.nodeCount - 1) * byteCount * totalOperations;
            this.host.log(
                    "Bytes per json:%d, per binary: %d, Total operations: %d, Total bytes:%d",
                    jsonByteCount,
                    byteCount,
                    totalOperations,
                    totalBytes);
            if (iterationCount++ < 2 && this.testDurationSeconds > 0) {
                countPerAction.clear();
                elapsedTimePerAction.clear();
            }
        } while (new Date().before(expiration) && this.totalOperationLimit > totalOperations);
        logHostStats();
        logPerActionThroughput(elapsedTimePerAction, countPerAction);
        this.host.doNodeGroupStatsVerification(this.host.getNodeGroupMap());
    }
    private void logHostStats() {
        for (URI u : this.host.getNodeGroupMap().keySet()) {
            URI mgmtUri = UriUtils.buildUri(u, ServiceHostManagementService.SELF_LINK);
            mgmtUri = UriUtils.buildStatsUri(mgmtUri);
            ServiceStats stats = this.host.getServiceState(null, ServiceStats.class, mgmtUri);
            this.host.log("%s: %s", u, Utils.toJsonHtml(stats));
        }
    }
    private void logPerActionThroughput(Map<Action, Long> elapsedTimePerAction,
            Map<Action, Long> countPerAction) {
        for (Action a : EnumSet.allOf(Action.class)) {
            Long count = countPerAction.get(a);
            if (count == null) {
                continue;
            }
            Long elapsedMicros = elapsedTimePerAction.get(a);
            double thpt = (count * 1.0) / (1.0 * elapsedMicros);
            thpt *= 1000000;
            this.host.log("Total ops for %s: %d, Throughput (ops/sec): %f", a, count, thpt);
        }
    }
    private void updatePerfDataPerAction(Action a, Long startTime, Long opCount,
            Map<Action, Long> countPerAction, Map<Action, Long> elapsedTime) {
        if (opCount == null || countPerAction != null) {
            countPerAction.merge(a, opCount, (e, n) -> {
                if (e == null) {
                    return n;
                }
                return e + n;
            });
        }
        if (startTime == null || elapsedTime == null) {
            return;
        }
        long delta = Utils.getNowMicrosUtc() - startTime;
        elapsedTime.merge(a, delta, (e, n) -> {
            if (e == null) {
                return n;
            }
            return e + n;
        });
    }
    private void verifyReplicatedIdempotentPost(Map<String, ExampleServiceState> childStates)
            throws Throwable {
        Map<URI, URI> exampleFactoryUris = this.host
                .getNodeGroupToFactoryMap(ExampleService.FACTORY_LINK);
        for (URI factoryUri : exampleFactoryUris.values()) {
            this.host.toggleServiceOptions(factoryUri,
                    EnumSet.of(ServiceOption.IDEMPOTENT_POST), null);
        }
        TestContext ctx = this.host.testCreate(childStates.size());
        for (Entry<String, ExampleServiceState> entry : childStates.entrySet()) {
            Operation post = Operation
                    .createPost(this.host.getPeerServiceUri(ExampleService.FACTORY_LINK))
                    .setBody(entry.getValue())
                    .setCompletion(ctx.getCompletion());
            this.host.send(post);
        }
        ctx.await();
    }
    private void verifyReplicatedForcedPostAfterDelete(Map<String, ExampleServiceState> childStates)
            throws Throwable {
        Entry<String, ExampleServiceState> childEntry = childStates.entrySet().iterator().next();
        TestContext ctx = this.host.testCreate(1);
        Operation delete = Operation
                .createDelete(this.host.getPeerServiceUri(childEntry.getKey()))
                .setCompletion(ctx.getCompletion());
        this.host.send(delete);
        ctx.await();
        if (!this.host.isRemotePeerTest()) {
            this.host.waitFor("services not deleted", () -> {
                for (VerificationHost h : this.host.getInProcessHostMap().values()) {
                    ProcessingStage stg = h.getServiceStage(childEntry.getKey());
                    if (stg != null) {
                        this.host.log("Service exists %s on host %s, stage %s",
                                childEntry.getKey(), h.toString(), stg);
                        return false;
                    }
                }
                return true;
            });
        }
        TestContext postCtx = this.host.testCreate(1);
        Operation opPost = Operation
                .createPost(this.host.getPeerServiceUri(this.replicationTargetFactoryLink))
                .addPragmaDirective(Operation.PRAGMA_DIRECTIVE_FORCE_INDEX_UPDATE)
                .setBody(childEntry.getValue())
                .setCompletion((o, e) -> {
                    if (e != null) {
                        postCtx.failIteration(e);
                    } else {
                        postCtx.completeIteration();
                    }
                });
        this.host.send(opPost);
        this.host.testWait(postCtx);
    }
    private void waitForReplicationFactoryConvergence() throws Throwable {
        WaitHandler wh = () -> {
            TestContext ctx = this.host.testCreate(1);
            boolean[] isReady = new boolean[1];
            CompletionHandler ch = (o, e) -> {
                if (e != null) {
                    isReady[0] = false;
                } else {
                    isReady[0] = true;
                }
                ctx.completeIteration();
            };
            VerificationHost peerHost = this.host.getPeerHost();
            if (peerHost == null) {
                NodeGroupUtils.checkServiceAvailability(ch, this.host,
                        this.host.getPeerServiceUri(this.replicationTargetFactoryLink),
                        this.replicationNodeSelector);
            } else {
                peerHost.checkReplicatedServiceAvailable(ch, this.replicationTargetFactoryLink);
            }
            ctx.await();
            return isReady[0];
        };
        this.host.waitFor("available check timeout for " + this.replicationTargetFactoryLink, wh);
    }
    private Set<String> verifyReplicatedServiceCountWithBroadcastQueries()
            throws Throwable {
        URI nodeUri = this.host.getPeerHostUri();
        QueryTask.QuerySpecification q = new QueryTask.QuerySpecification();
        q.query.setTermPropertyName(ServiceDocument.FIELD_NAME_KIND).setTermMatchValue(
                Utils.buildKind(ExampleServiceState.class));
        QueryTask task = QueryTask.create(q).setDirect(true);
        URI queryTaskFactoryUri = UriUtils
                .buildUri(nodeUri, ServiceUriPaths.CORE_LOCAL_QUERY_TASKS);
        URI forwardingService = UriUtils.buildBroadcastRequestUri(queryTaskFactoryUri,
                ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        Set<String> links = new HashSet<>();
        TestContext testContext = this.host.testCreate(1);
        Operation postQuery = Operation
                .createPost(forwardingService)
                .setBody(task)
                .setCompletion(
                        (o, e) -> {
                            if (e != null) {
                                this.host.failIteration(e);
                                return;
                            }
                            NodeGroupBroadcastResponse rsp = o
                                    .getBody(NodeGroupBroadcastResponse.class);
                            NodeGroupBroadcastResult broadcastResponse = NodeGroupUtils.toBroadcastResult(rsp);
                            if (broadcastResponse.hasFailure()) {
                                testContext.fail(new IllegalStateException(
                                        "Failure from query tasks: " + Utils.toJsonHtml(rsp)));
                                return;
                            }
                            Set<String> ownerIds = new HashSet<>();
                            for (PeerNodeResult successResponse : broadcastResponse.successResponses) {
                                QueryTask qt = successResponse.castBodyTo(QueryTask.class);
                                this.host.log("Broadcast response from %s %s", qt.documentSelfLink,
                                        qt.documentOwner);
                                ownerIds.add(qt.documentOwner);
                                if (qt.results == null) {
                                    this.host.log("Node %s had no results", successResponse.requestUri);
                                    continue;
                                }
                                for (String l : qt.results.documentLinks) {
                                    links.add(l);
                                }
                            }
                            testContext.completeIteration();
                        });
        this.host.send(postQuery);
        testContext.await();
        return links;
    }
    private void verifyInstantNotFoundFailureOnBadLinks() throws Throwable {
        this.host.toggleNegativeTestMode(true);
        TestContext testContext = this.host.testCreate(this.serviceCount);
        CompletionHandler c = (o, e) -> {
            if (e != null) {
                testContext.complete();
                return;
            }
            for (VerificationHost h : this.host.getInProcessHostMap().values()) {
                ProcessingStage stg = h.getServiceStage(o.getUri().getPath());
                if (stg != null) {
                    this.host.log("Service exists %s on host %s, stage %s",
                            o.getUri().getPath(), h.toString(), stg);
                }
            }
            testContext.fail(new Throwable("Expected service to not exist:"
                    + o.toString()));
        };
        for (int i = 0; i < this.serviceCount; i++) {
            URI factoryURI = this.host.getNodeGroupToFactoryMap(ExampleService.FACTORY_LINK)
                    .values().iterator().next();
            URI bogusChild = UriUtils.extendUri(factoryURI,
                    Utils.getNowMicrosUtc() + UUID.randomUUID().toString());
            Operation patch = Operation.createPatch(bogusChild)
                    .setCompletion(c)
                    .setBody(new ExampleServiceState());
            this.host.send(patch);
        }
        testContext.await();
        this.host.toggleNegativeTestMode(false);
    }
    @Test
    public void factorySynchronization() throws Throwable {
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.nodeCount);
        factorySynchronizationNoChildren();
        factoryDuplicatePost();
    }
    @Test
    public void replicationWithAuthzCacheClear() throws Throwable {
        this.isAuthorizationEnabled = true;
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.nodeCount);
        this.host.setNodeGroupQuorum(this.nodeCount);
        VerificationHost groupHost = this.host.getPeerHost();
        groupHost.waitForReplicatedFactoryServiceAvailable(
                UriUtils.buildUri(groupHost, UserService.FACTORY_LINK));
        groupHost.waitForReplicatedFactoryServiceAvailable(
                UriUtils.buildUri(groupHost, UserGroupService.FACTORY_LINK));
        groupHost.waitForReplicatedFactoryServiceAvailable(
                UriUtils.buildUri(groupHost, ResourceGroupService.FACTORY_LINK));
        groupHost.waitForReplicatedFactoryServiceAvailable(
                UriUtils.buildUri(groupHost, RoleService.FACTORY_LINK));
        String fooUserLink = UriUtils.buildUriPath(ServiceUriPaths.CORE_AUTHZ_USERS,
                "foo@vmware.com");
        String barUserLink = UriUtils.buildUriPath(ServiceUriPaths.CORE_AUTHZ_USERS,
                "bar@vmware.com");
        String bazUserLink = UriUtils.buildUriPath(ServiceUriPaths.CORE_AUTHZ_USERS,
                "baz@vmware.com");
        groupHost.setSystemAuthorizationContext();
        TestContext testContext = this.host.testCreate(1);
        AuthorizationSetupHelper.create()
                .setHost(groupHost)
                .setUserSelfLink("foo@vmware.com")
                .setUserEmail("foo@vmware.com")
                .setUserPassword("password")
                .setDocumentKind(Utils.buildKind(ExampleServiceState.class))
                .setUserGroupName("foo-user-group")
                .setResourceGroupName("foo-resource-group")
                .setRoleName("foo-role-1")
                .setCompletion(testContext.getCompletion())
                .start();
        testContext.await();
        TestContext ctxToCreateAnotherRole = this.host.testCreate(1);
        AuthorizationSetupHelper.create()
                .setHost(groupHost)
                .setUserSelfLink(fooUserLink)
                .setDocumentKind(Utils.buildKind(ExampleServiceState.class))
                .setRoleName("foo-role-2")
                .setCompletion(ctxToCreateAnotherRole.getCompletion())
                .setupRole();
        ctxToCreateAnotherRole.await();
        TestContext ctxToCreateBar = this.host.testCreate(1);
        AuthorizationSetupHelper.create()
                .setHost(groupHost)
                .setUserSelfLink("bar@vmware.com")
                .setUserEmail("bar@vmware.com")
                .setUserPassword("password")
                .setDocumentKind(Utils.buildKind(ExampleServiceState.class))
                .setCompletion(ctxToCreateBar.getCompletion())
                .start();
        ctxToCreateBar.await();
        TestContext ctxToCreateBaz = this.host.testCreate(1);
        AuthorizationSetupHelper.create()
                .setHost(groupHost)
                .setUserSelfLink("baz@vmware.com")
                .setUserEmail("baz@vmware.com")
                .setUserPassword("password")
                .setDocumentKind(Utils.buildKind(ExampleServiceState.class))
                .setCompletion(ctxToCreateBaz.getCompletion())
                .start();
        ctxToCreateBaz.await();
        AuthorizationContext fooAuthContext = groupHost.assumeIdentity(fooUserLink);
        AuthorizationContext barAuthContext = groupHost.assumeIdentity(barUserLink);
        AuthorizationContext bazAuthContext = groupHost.assumeIdentity(bazUserLink);
        String fooToken = fooAuthContext.getToken();
        String barToken = barAuthContext.getToken();
        String bazToken = bazAuthContext.getToken();
        groupHost.resetSystemAuthorizationContext();
        populateAuthCacheInAllPeers(fooAuthContext);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(
                        UriUtils.buildUri(groupHost, "/core/authz/users/foo@vmware.com")));
        groupHost.resetSystemAuthorizationContext();
        checkCacheInAllPeers(fooToken, true);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(
                        UriUtils.buildUri(groupHost, "/core/authz/user-groups/foo-user-group")));
        groupHost.resetSystemAuthorizationContext();
        checkCacheInAllPeers(fooToken, true);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(
                        UriUtils.buildUri(groupHost, "/core/authz/resource-groups/foo-resource-group")));
        groupHost.resetSystemAuthorizationContext();
        checkCacheInAllPeers(fooToken, true);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(
                        UriUtils.buildUri(groupHost, "/core/authz/roles/foo-role-1")));
        groupHost.resetSystemAuthorizationContext();
        checkCacheInAllPeers(fooToken, true);
        populateAuthCacheInAllPeers(fooAuthContext);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createDelete(
                        UriUtils.buildUri(groupHost, "/core/authz/roles/foo-role-1")));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(fooToken);
        populateAuthCacheInAllPeers(fooAuthContext);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createDelete(
                        UriUtils.buildUri(groupHost, "/core/authz/user-groups/foo-user-group")));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(fooToken);
        populateAuthCacheInAllPeers(barAuthContext);
        groupHost.setSystemAuthorizationContext();
        Query q = Builder.create()
                .addFieldClause(
                        ExampleServiceState.FIELD_NAME_KIND,
                        Utils.buildKind(ExampleServiceState.class))
                .build();
        TestContext ctxToCreateAnotherRoleForBar = this.host.testCreate(1);
        AuthorizationSetupHelper.create()
                .setHost(groupHost)
                .setUserSelfLink(barUserLink)
                .setResourceGroupName("/core/authz/resource-groups/new-rg")
                .setResourceQuery(q)
                .setRoleName("bar-role-2")
                .setCompletion(ctxToCreateAnotherRoleForBar.getCompletion())
                .setupRole();
        ctxToCreateAnotherRoleForBar.await();
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(barToken);
        populateAuthCacheInAllPeers(barAuthContext);
        groupHost.setSystemAuthorizationContext();
        String newResourceGroupLink = "/core/authz/resource-groups/new-rg";
        Query updateResourceGroupQuery = Builder.create()
                .addFieldClause(ExampleServiceState.FIELD_NAME_NAME, "bar")
                .build();
        ResourceGroupState resourceGroupState = new ResourceGroupState();
        resourceGroupState.query = updateResourceGroupQuery;
        this.host.sendAndWaitExpectSuccess(
                Operation.createPut(UriUtils.buildUri(groupHost, newResourceGroupLink))
                        .setBody(resourceGroupState));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(barToken);
        populateAuthCacheInAllPeers(fooAuthContext);
        groupHost.setSystemAuthorizationContext();
        UserState userState = new UserState();
        userState.userGroupLinks = new HashSet<>();
        userState.userGroupLinks.add("foo");
        this.host.sendAndWaitExpectSuccess(
                Operation.createPatch(UriUtils.buildUri(groupHost, fooUserLink))
                        .setBody(userState));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(fooToken);
        populateAuthCacheInAllPeers(bazAuthContext);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createDelete(UriUtils.buildUri(groupHost, bazUserLink)));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(bazToken);
        TestRequestSender sender = new TestRequestSender(this.host.getPeerHost());
        groupHost.setSystemAuthorizationContext();
        Operation newResourceGroupGetOp = Operation.createGet(groupHost, newResourceGroupLink);
        ResourceGroupState newResourceGroupState = sender.sendAndWait(newResourceGroupGetOp, ResourceGroupState.class);
        groupHost.resetSystemAuthorizationContext();
        PatchQueryRequest patchBody = PatchQueryRequest.create(newResourceGroupState.query, false);
        populateAuthCacheInAllPeers(barAuthContext);
        groupHost.setSystemAuthorizationContext();
        this.host.sendAndWaitExpectSuccess(
                Operation.createPatch(UriUtils.buildUri(groupHost, newResourceGroupLink))
                        .setBody(patchBody));
        groupHost.resetSystemAuthorizationContext();
        verifyAuthCacheHasClearedInAllPeers(barToken);
    }
    private void populateAuthCacheInAllPeers(AuthorizationContext authContext) throws Throwable {
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            peer.setAuthorizationContext(authContext);
            this.host.sendAndWaitExpectSuccess(
                    Operation.createGet(UriUtils.buildStatsUri(peer, ExampleService.FACTORY_LINK)));
        }
        this.host.waitFor("Timeout waiting for correct auth cache state",
                () -> checkCacheInAllPeers(authContext.getToken(), true));
    }
    private void verifyAuthCacheHasClearedInAllPeers(String userToken) {
        this.host.waitFor("Timeout waiting for correct auth cache state",
                () -> checkCacheInAllPeers(userToken, false));
    }
    private boolean checkCacheInAllPeers(String token, boolean expectEntries) throws Throwable {
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            peer.setSystemAuthorizationContext();
            MinimalTestService s = new MinimalTestService();
            peer.addPrivilegedService(MinimalTestService.class);
            peer.startServiceAndWait(s, UUID.randomUUID().toString(), null);
            peer.resetSystemAuthorizationContext();
            boolean contextFound = peer.getAuthorizationContext(s, token) != null;
            if ((expectEntries && !contextFound) || (!expectEntries && contextFound)) {
                return false;
            }
        }
        return true;
    }
    private void factoryDuplicatePost() throws Throwable, InterruptedException, TimeoutException {
        VerificationHost serviceHost = this.host.getPeerHost();
        Consumer<Operation> setBodyCallback = (o) -> {
            ReplicationTestServiceState s = new ReplicationTestServiceState();
            s.stringField = UUID.randomUUID().toString();
            o.setBody(s);
        };
        URI factoryUri = this.host
                .getPeerServiceUri(ReplicationFactoryTestService.OWNER_SELECTION_SELF_LINK);
        Map<URI, ReplicationTestServiceState> states = doReplicatedServiceFactoryPost(
                this.serviceCount, setBodyCallback, factoryUri);
        TestContext testContext = serviceHost.testCreate(states.size());
        ReplicationTestServiceState initialState = new ReplicationTestServiceState();
        for (URI uri : states.keySet()) {
            initialState.documentSelfLink = uri.toString().substring(uri.toString()
                    .lastIndexOf(UriUtils.URI_PATH_CHAR) + 1);
            Operation createPost = Operation
                    .createPost(factoryUri)
                    .setBody(initialState)
                    .setCompletion(
                            (o, e) -> {
                                if (o.getStatusCode() != Operation.STATUS_CODE_CONFLICT) {
                                    testContext.fail(
                                            new IllegalStateException(
                                                    "Incorrect response code received"));
                                    return;
                                }
                                testContext.complete();
                            });
            serviceHost.send(createPost);
        }
        testContext.await();
    }
    private void factorySynchronizationNoChildren() throws Throwable {
        int factoryCount = Math.max(this.serviceCount, 25);
        setUp(this.nodeCount);
        TestContext testContext = this.host.testCreate(this.nodeCount * factoryCount);
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            for (int i = 0; i < factoryCount; i++) {
                Operation startPost = Operation.createPost(
                        UriUtils.buildUri(h,
                                UriUtils.buildUriPath(ExampleService.FACTORY_LINK, UUID
                                        .randomUUID().toString())))
                        .setCompletion(testContext.getCompletion());
                h.startService(startPost, ExampleService.createFactory());
            }
        }
        testContext.await();
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
    }
    @Test
    public void forwardingAndSelection() throws Throwable {
        this.isPeerSynchronizationEnabled = false;
        setUp(this.nodeCount);
        this.host.joinNodesAndVerifyConvergence(this.nodeCount);
        for (int i = 0; i < this.iterationCount; i++) {
            directOwnerSelection();
            forwardingToPeerId();
            forwardingToKeyHashNode();
            broadcast();
        }
    }
    public void broadcast() throws Throwable {
        URI nodeGroup = this.host.getPeerNodeGroupUri();
        long c = this.updateCount * this.nodeCount;
        List<ServiceDocument> initialStates = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            ServiceDocument s = this.host.buildMinimalTestState();
            s.documentSelfLink = UUID.randomUUID().toString();
            initialStates.add(s);
        }
        TestContext testContext = this.host.testCreate(c * this.host.getPeerCount());
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            for (ServiceDocument s : initialStates) {
                Operation post = Operation.createPost(UriUtils.buildUri(peer, s.documentSelfLink))
                        .setCompletion(testContext.getCompletion())
                        .setBody(s);
                peer.startService(post, new MinimalTestService());
            }
        }
        testContext.await();
        nodeGroup = this.host.getPeerNodeGroupUri();
        testContext = this.host.testCreate(initialStates.size());
        for (ServiceDocument s : initialStates) {
            URI serviceUri = UriUtils.buildUri(nodeGroup, s.documentSelfLink);
            URI u = UriUtils.buildBroadcastRequestUri(serviceUri,
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
            MinimalTestServiceState body = (MinimalTestServiceState) this.host
                    .buildMinimalTestState();
            body.id = serviceUri.getPath();
            this.host.send(Operation.createPut(u)
                    .setCompletion(testContext.getCompletion())
                    .setBody(body));
        }
        testContext.await();
        for (URI baseHostUri : this.host.getNodeGroupMap().keySet()) {
            List<URI> uris = new ArrayList<>();
            for (ServiceDocument s : initialStates) {
                URI serviceUri = UriUtils.buildUri(baseHostUri, s.documentSelfLink);
                uris.add(serviceUri);
            }
            Map<URI, MinimalTestServiceState> states = this.host.getServiceState(null,
                    MinimalTestServiceState.class, uris);
            for (MinimalTestServiceState s : states.values()) {
                if (!s.id.equals(s.documentSelfLink)) {
                    throw new IllegalStateException("Service broadcast failure");
                }
            }
        }
    }
    public void forwardingToKeyHashNode() throws Throwable {
        long c = this.updateCount * this.nodeCount;
        Map<String, List<String>> ownersPerServiceLink = new HashMap<>();
        List<ServiceDocument> initialStates = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            ServiceDocument s = this.host.buildMinimalTestState();
            s.documentSelfLink = UUID.randomUUID().toString();
            initialStates.add(s);
        }
        TestContext testContext = this.host.testCreate(c * this.host.getPeerCount());
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            for (ServiceDocument s : initialStates) {
                Operation post = Operation.createPost(UriUtils.buildUri(peer, s.documentSelfLink))
                        .setCompletion(testContext.getCompletion())
                        .setBody(s);
                peer.startService(post, new MinimalTestService());
            }
        }
        testContext.await();
        URI nodeGroup = this.host.getPeerNodeGroupUri();
        testContext = this.host.testCreate(initialStates.size());
        for (ServiceDocument s : initialStates) {
            URI serviceUri = UriUtils.buildUri(nodeGroup, s.documentSelfLink);
            URI u = UriUtils.buildForwardRequestUri(serviceUri,
                    null,
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
            MinimalTestServiceState body = (MinimalTestServiceState) this.host
                    .buildMinimalTestState();
            body.id = serviceUri.getPath();
            this.host.send(Operation.createPut(u)
                    .setCompletion(testContext.getCompletion())
                    .setBody(body));
        }
        testContext.await();
        this.host.logThroughput();
        AtomicInteger assignedLinks = new AtomicInteger();
        TestContext testContextForPost = this.host.testCreate(initialStates.size());
        for (ServiceDocument s : initialStates) {
            String key = UriUtils.normalizeUriPath(s.documentSelfLink);
            s.documentSelfLink = key;
            SelectAndForwardRequest body = new SelectAndForwardRequest();
            body.key = key;
            Operation post = Operation.createPost(UriUtils.buildUri(nodeGroup,
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR))
                    .setBody(body)
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            testContextForPost.fail(e);
                            return;
                        }
                        synchronized (ownersPerServiceLink) {
                            SelectOwnerResponse rsp = o.getBody(SelectOwnerResponse.class);
                            List<String> links = ownersPerServiceLink.get(rsp.ownerNodeId);
                            if (links == null) {
                                links = new ArrayList<>();
                                ownersPerServiceLink.put(rsp.ownerNodeId, links);
                            }
                            links.add(key);
                            ownersPerServiceLink.put(rsp.ownerNodeId, links);
                        }
                        assignedLinks.incrementAndGet();
                        testContextForPost.complete();
                    });
            this.host.send(post);
        }
        testContextForPost.await();
        assertTrue(assignedLinks.get() == initialStates.size());
        for (Entry<String, List<String>> e : ownersPerServiceLink.entrySet()) {
            String nodeId = e.getKey();
            List<String> links = e.getValue();
            NodeState ns = this.host.getNodeStateMap().get(nodeId);
            List<URI> uris = new ArrayList<>();
            for (String l : links) {
                uris.add(UriUtils.buildUri(ns.groupReference, l));
            }
            Map<URI, MinimalTestServiceState> states = this.host.getServiceState(null,
                    MinimalTestServiceState.class, uris);
            for (MinimalTestServiceState s : states.values()) {
                if (!s.id.equals(s.documentSelfLink)) {
                    throw new IllegalStateException("Service forwarding failure");
                } else {
                }
            }
        }
    }
    public void forwardingToPeerId() throws Throwable {
        long c = this.updateCount * this.nodeCount;
        List<ServiceDocument> initialStates = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            ServiceDocument s = this.host.buildMinimalTestState();
            s.documentSelfLink = UUID.randomUUID().toString();
            initialStates.add(s);
        }
        TestContext testContext = this.host.testCreate(c * this.host.getPeerCount());
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            for (ServiceDocument s : initialStates) {
                s = Utils.clone(s);
                s.documentOwner = peer.getId();
                Operation post = Operation.createPost(UriUtils.buildUri(peer, s.documentSelfLink))
                        .setCompletion(testContext.getCompletion())
                        .setBody(s);
                peer.startService(post, new MinimalTestService());
            }
        }
        testContext.await();
        VerificationHost peerEntryPoint = this.host.getPeerHost();
        String headerName = MinimalTestService.TEST_HEADER_NAME.toLowerCase();
        UUID id = UUID.randomUUID();
        String headerRequestValue = "request-" + id;
        String headerResponseValue = "response-" + id;
        TestContext testContextForPut = this.host.testCreate(initialStates.size() * this.nodeCount);
        for (ServiceDocument s : initialStates) {
            for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
                URI localForwardingUri = UriUtils.buildUri(peerEntryPoint.getUri(),
                        s.documentSelfLink);
                localForwardingUri = UriUtils.extendUriWithQuery(localForwardingUri, "k", "v",
                        "k1", "v1", "k2", "v2");
                URI u = UriUtils.buildForwardToPeerUri(localForwardingUri, peer.getId(),
                        ServiceUriPaths.DEFAULT_NODE_SELECTOR, EnumSet.noneOf(ServiceOption.class));
                MinimalTestServiceState body = (MinimalTestServiceState) this.host
                        .buildMinimalTestState();
                body.id = peer.getId();
                this.host.send(Operation.createPut(u)
                        .addRequestHeader(headerName, headerRequestValue)
                        .setCompletion(
                                (o, e) -> {
                                    if (e != null) {
                                        testContextForPut.fail(e);
                                        return;
                                    }
                                    String value = o.getResponseHeader(headerName);
                                    if (value == null || !value.equals(headerResponseValue)) {
                                        testContextForPut.fail(new IllegalArgumentException(
                                                "response header not found"));
                                        return;
                                    }
                                    testContextForPut.complete();
                                })
                        .setBody(body));
            }
        }
        testContextForPut.await();
        this.host.logThroughput();
        TestContext ctx = this.host.testCreate(c * this.host.getPeerCount());
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            for (ServiceDocument s : initialStates) {
                Operation get = Operation.createGet(UriUtils.buildUri(peer, s.documentSelfLink))
                        .setCompletion(
                                (o, e) -> {
                                    if (e != null) {
                                        ctx.fail(e);
                                        return;
                                    }
                                    MinimalTestServiceState rsp = o
                                            .getBody(MinimalTestServiceState.class);
                                    if (!rsp.id.equals(rsp.documentOwner)) {
                                        ctx.fail(
                                                new IllegalStateException("Expected: "
                                                        + rsp.documentOwner + " was: " + rsp.id));
                                    } else {
                                        ctx.complete();
                                    }
                                });
                this.host.send(get);
            }
        }
        ctx.await();
        this.host.toggleDebuggingMode(true);
        TestContext testCxtForPut = this.host.testCreate(this.host.getInProcessHostMap().size());
        for (VerificationHost peer : this.host.getInProcessHostMap().values()) {
            ServiceDocument s = initialStates.get(0);
            URI serviceUri = UriUtils.buildUri(peerEntryPoint.getUri(), s.documentSelfLink);
            URI u = UriUtils.buildForwardToPeerUri(serviceUri, peer.getId(),
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR,
                    null);
            MinimalTestServiceState body = (MinimalTestServiceState) this.host
                    .buildMinimalTestState();
            body.id = null;
            this.host.send(Operation.createPut(u)
                    .setCompletion(
                            (o, e) -> {
                                if (e == null) {
                                    testCxtForPut.fail(new IllegalStateException(
                                            "expected failure"));
                                    return;
                                }
                                MinimalTestServiceErrorResponse rsp = o
                                        .getBody(MinimalTestServiceErrorResponse.class);
                                if (rsp.message == null || rsp.message.isEmpty()) {
                                    testCxtForPut.fail(new IllegalStateException(
                                            "expected error response message"));
                                    return;
                                }
                                if (!MinimalTestServiceErrorResponse.KIND.equals(rsp.documentKind)
                                        || 0 != Double.compare(Math.PI, rsp.customErrorField)) {
                                    testCxtForPut.fail(new IllegalStateException(
                                            "expected custom error fields"));
                                    return;
                                }
                                testCxtForPut.complete();
                            })
                    .setBody(body));
        }
        testCxtForPut.await();
        this.host.toggleDebuggingMode(false);
    }
    private void directOwnerSelection() throws Throwable {
        Map<URI, Map<String, Long>> keyToNodeAssignmentsPerNode = new HashMap<>();
        List<String> keys = new ArrayList<>();
        long c = this.updateCount * this.nodeCount;
        for (int i = 0; i < c; i++) {
            keys.add(Utils.getNowMicrosUtc() + "");
        }
        for (URI nodeGroup : this.host.getNodeGroupMap().values()) {
            keyToNodeAssignmentsPerNode.put(nodeGroup, new HashMap<>());
        }
        this.host.waitForNodeGroupConvergence(this.nodeCount);
        TestContext testContext = this.host.testCreate(c * this.nodeCount);
        for (URI nodeGroup : this.host.getNodeGroupMap().values()) {
            for (String key : keys) {
                SelectAndForwardRequest body = new SelectAndForwardRequest();
                body.key = key;
                Operation post = Operation
                        .createPost(UriUtils.buildUri(nodeGroup,
                                ServiceUriPaths.DEFAULT_NODE_SELECTOR))
                        .setBody(body)
                        .setCompletion(
                                (o, e) -> {
                                    try {
                                        synchronized (keyToNodeAssignmentsPerNode) {
                                            SelectOwnerResponse rsp = o
                                                    .getBody(SelectOwnerResponse.class);
                                            Map<String, Long> assignmentsPerNode = keyToNodeAssignmentsPerNode
                                                    .get(nodeGroup);
                                            Long l = assignmentsPerNode.get(rsp.ownerNodeId);
                                            if (l == null) {
                                                l = 0L;
                                            }
                                            assignmentsPerNode.put(rsp.ownerNodeId, l + 1);
                                            testContext.complete();
                                        }
                                    } catch (Throwable ex) {
                                        testContext.fail(ex);
                                    }
                                });
                this.host.send(post);
            }
        }
        testContext.await();
        this.host.logThroughput();
        Map<String, Long> countPerNode = null;
        for (URI nodeGroup : this.host.getNodeGroupMap().values()) {
            Map<String, Long> assignmentsPerNode = keyToNodeAssignmentsPerNode.get(nodeGroup);
            if (countPerNode == null) {
                countPerNode = assignmentsPerNode;
            }
            this.host.log("Node group %s assignments: %s", nodeGroup, assignmentsPerNode);
            for (Entry<String, Long> e : assignmentsPerNode.entrySet()) {
                assertTrue(e.getValue() > 0);
                Long count = countPerNode.get(e.getKey());
                if (count == null) {
                    continue;
                }
                if (!count.equals(e.getValue())) {
                    this.host.logNodeGroupState();
                    throw new IllegalStateException(
                            "Node id got assigned the same key different number of times, on one of the nodes");
                }
            }
        }
    }
    @Test
    public void replicationFullQuorumMissingServiceOnPeer() throws Throwable {
        System.setProperty(
                NodeSelectorReplicationService.PROPERTY_NAME_REPLICA_NOT_FOUND_TIMEOUT_MICROS,
                Long.toString(TimeUnit.MILLISECONDS.toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS)));
        this.host = VerificationHost.create(0);
        this.host.setPeerSynchronizationEnabled(false);
        this.host.setMaintenanceIntervalMicros(TimeUnit.MILLISECONDS.toMicros(
                VerificationHost.FAST_MAINT_INTERVAL_MILLIS));
        this.host.start();
        List<URI> exampleUris = new ArrayList<>();
        this.host.createExampleServices(this.host, this.serviceCount, exampleUris, null);
        VerificationHost host2 = new VerificationHost();
        try {
            TemporaryFolder tmpFolder = new TemporaryFolder();
            tmpFolder.create();
            String mainHostId = "main-" + VerificationHost.hostNumber.incrementAndGet();
            String[] args = {
                    "--id=" + mainHostId,
                    "--port=0",
                    "--bindAddress=127.0.0.1",
                    "--sandbox="
                            + tmpFolder.getRoot().getAbsolutePath(),
                    "--peerNodes=" + this.host.getUri()
            };
            host2.initialize(args);
            host2.setPeerSynchronizationEnabled(false);
            host2.setMaintenanceIntervalMicros(
                    TimeUnit.MILLISECONDS.toMicros(VerificationHost.FAST_MAINT_INTERVAL_MILLIS));
            host2.start();
            this.host.addPeerNode(host2);
            List<URI> nodeGroupUris = new ArrayList<>();
            nodeGroupUris.add(UriUtils.buildUri(this.host, ServiceUriPaths.DEFAULT_NODE_GROUP));
            nodeGroupUris.add(UriUtils.buildUri(host2, ServiceUriPaths.DEFAULT_NODE_GROUP));
            this.host.waitForNodeGroupConvergence(nodeGroupUris, 2, 2, true);
            this.host.setNodeGroupQuorum(2, nodeGroupUris.get(0));
            host2.setNodeGroupQuorum(2);
            List<URI> host1Examples = exampleUris.stream()
                    .filter(e -> this.host.isOwner(e.getPath(), ServiceUriPaths.DEFAULT_NODE_SELECTOR))
                    .collect(Collectors.toList());
            ExampleServiceState state = new ExampleServiceState();
            state.counter = 1L;
            if (host1Examples.size() > 0) {
                this.host.log(Level.INFO, "Starting patches");
                TestContext ctx = this.host.testCreate(host1Examples.size());
                for (URI exampleUri : host1Examples) {
                    Operation patch = Operation
                            .createPatch(exampleUri)
                            .setBody(state)
                            .setReferer("localhost")
                            .setCompletion(ctx.getCompletion());
                    this.host.sendRequest(patch);
                }
                ctx.await();
            }
        } finally {
            host2.tearDown();
        }
    }
    @Test
    public void replicationWithAuthAndNodeRestart() throws Throwable {
        AuthorizationHelper authHelper;
        this.isAuthorizationEnabled = true;
        setUp(this.nodeCount);
        authHelper = new AuthorizationHelper(this.host);
        this.host.setSystemAuthorizationContext();
        Map<ServiceHost, Collection<String>> roleLinksByHost = new HashMap<>();
        for (VerificationHost h : this.host.getInProcessHostMap().values()) {
            String email = "jane@doe.com";
            authHelper.createUserService(h, email);
            authHelper.createRoles(h, email);
        }
        Map<ServiceHost, Map<URI, RoleState>> roleStateByHost = getRolesByHost(roleLinksByHost);
        this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        Map<ServiceHost, Map<URI, RoleState>> newRoleStateByHost = getRolesByHost(roleLinksByHost);
        for (ServiceHost h : roleStateByHost.keySet()) {
            Map<URI, RoleState> roleState = roleStateByHost.get(h);
            for (URI u : roleState.keySet()) {
                RoleState oldRole = roleState.get(u);
                RoleState newRole = newRoleStateByHost.get(h).get(u);
                assertTrue("version should have advanced",
                        newRole.documentVersion > oldRole.documentVersion);
                assertTrue("epoch should have advanced",
                        newRole.documentEpoch > oldRole.documentEpoch);
            }
        }
        Map<String, Long> versions = new HashMap<>();
        Map<String, Long> epochs = new HashMap<>();
        Map<String, String> owners = new HashMap<>();
        for (ServiceHost h : newRoleStateByHost.keySet()) {
            Map<URI, RoleState> roleState = newRoleStateByHost.get(h);
            for (URI u : roleState.keySet()) {
                RoleState newRole = roleState.get(u);
                if (versions.containsKey(newRole.documentSelfLink)) {
                    assertTrue(versions.get(newRole.documentSelfLink) == newRole.documentVersion);
                } else {
                    versions.put(newRole.documentSelfLink, newRole.documentVersion);
                }
                if (epochs.containsKey(newRole.documentSelfLink)) {
                    assertTrue(Objects.equals(epochs.get(newRole.documentSelfLink),
                            newRole.documentEpoch));
                } else {
                    epochs.put(newRole.documentSelfLink, newRole.documentEpoch);
                }
                if (owners.containsKey(newRole.documentSelfLink)) {
                    assertEquals(owners.get(newRole.documentSelfLink), newRole.documentOwner);
                } else {
                    owners.put(newRole.documentSelfLink, newRole.documentOwner);
                }
            }
        }
        Set<String> exampleTaskLinks = new ConcurrentSkipListSet<>();
        createReplicatedExampleTasks(exampleTaskLinks, null);
        Set<String> exampleLinks = new ConcurrentSkipListSet<>();
        verifyReplicatedAuthorizedPost(exampleLinks);
        VerificationHost hostToStop = this.host.getInProcessHostMap().values().iterator().next();
        stopAndRestartHost(exampleLinks, exampleTaskLinks, hostToStop);
    }
    private void createReplicatedExampleTasks(Set<String> exampleTaskLinks, String name)
            throws Throwable {
        URI factoryUri = UriUtils.buildFactoryUri(this.host.getPeerHost(),
                ExampleTaskService.class);
        this.host.setSystemAuthorizationContext();
        ExampleTaskServiceState exampleServiceState = new ExampleTaskServiceState();
        if (name != null) {
            exampleServiceState.customQueryClause = Query.Builder.create()
                    .addFieldClause(ExampleServiceState.FIELD_NAME_NAME, name).build();
        }
        this.host.log("creating example *task* instances");
        TestContext testContext = this.host.testCreate(this.serviceCount);
        for (int i = 0; i < this.serviceCount; i++) {
            CompletionHandler c = (o, e) -> {
                if (e != null) {
                    testContext.fail(e);
                    return;
                }
                ExampleTaskServiceState rsp = o.getBody(ExampleTaskServiceState.class);
                synchronized (exampleTaskLinks) {
                    exampleTaskLinks.add(rsp.documentSelfLink);
                }
                testContext.complete();
            };
            this.host.send(Operation
                    .createPost(factoryUri)
                    .setBody(exampleServiceState)
                    .setCompletion(c));
        }
        testContext.await();
        this.host.waitFor("Example tasks did not finish", () -> {
            ServiceDocumentQueryResult rsp = this.host.getExpandedFactoryState(factoryUri);
            for (Object o : rsp.documents.values()) {
                ExampleTaskServiceState doc = Utils.fromJson(o, ExampleTaskServiceState.class);
                if (TaskState.isFailed(doc.taskInfo)) {
                    this.host.log("task %s failed: %s", doc.documentSelfLink, doc.failureMessage);
                    throw new IllegalStateException("task failed");
                }
                if (!TaskState.isFinished(doc.taskInfo)) {
                    return false;
                }
            }
            return true;
        });
    }
    private void verifyReplicatedAuthorizedPost(Set<String> exampleLinks)
            throws Throwable {
        Collection<VerificationHost> hosts = this.host.getInProcessHostMap().values();
        RoundRobinIterator<VerificationHost> it = new RoundRobinIterator<>(hosts);
        int exampleServiceCount = this.serviceCount;
        String userLink = UriUtils.buildUriPath(ServiceUriPaths.CORE_AUTHZ_USERS, "jane@doe.com");
        this.host.assumeIdentity(userLink);
        ExampleServiceState exampleServiceState = new ExampleServiceState();
        exampleServiceState.name = "jane";
        this.host.log("creating example instances");
        TestContext testContext = this.host.testCreate(exampleServiceCount);
        for (int i = 0; i < exampleServiceCount; i++) {
            CompletionHandler c = (o, e) -> {
                if (e != null) {
                    testContext.fail(e);
                    return;
                }
                try {
                    ExampleServiceState state = o.getBody(ExampleServiceState.class);
                    assertEquals(state.documentAuthPrincipalLink,
                            userLink);
                    exampleLinks.add(state.documentSelfLink);
                    testContext.complete();
                } catch (Throwable e2) {
                    testContext.fail(e2);
                }
            };
            this.host.send(Operation
                    .createPost(UriUtils.buildFactoryUri(it.next(), ExampleService.class))
                    .setBody(exampleServiceState)
                    .setCompletion(c));
        }
        testContext.await();
        this.host.toggleNegativeTestMode(true);
        ExampleServiceState invalidExampleServiceState = new ExampleServiceState();
        invalidExampleServiceState.name = "somebody other than jane";
        this.host.log("issuing non authorized request");
        TestContext testCtx = this.host.testCreate(exampleServiceCount);
        for (int i = 0; i < exampleServiceCount; i++) {
            this.host.send(Operation
                    .createPost(UriUtils.buildFactoryUri(it.next(), ExampleService.class))
                    .setBody(invalidExampleServiceState)
                    .setCompletion((o, e) -> {
                        if (e != null) {
                            assertEquals(Operation.STATUS_CODE_FORBIDDEN, o.getStatusCode());
                            testCtx.complete();
                            return;
                        }
                        testCtx.fail(new IllegalStateException("expected failure"));
                    }));
        }
        testCtx.await();
        this.host.toggleNegativeTestMode(false);
    }
    private void stopAndRestartHost(Set<String> exampleLinks, Set<String> exampleTaskLinks,
            VerificationHost hostToStop)
            throws Throwable, InterruptedException {
        this.host.setNodeGroupQuorum(this.nodeCount - 1);
        NodeGroupConfig cfg = new NodeGroupConfig();
        cfg.nodeRemovalDelayMicros = TimeUnit.SECONDS.toMicros(1);
        this.host.setNodeGroupConfig(cfg);
        this.host.stopHostAndPreserveState(hostToStop);
        this.host.waitForNodeGroupConvergence(2, 2);
        VerificationHost existingHost = this.host.getInProcessHostMap().values().iterator().next();
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(ExampleTaskService.FACTORY_LINK),
                ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        waitForReplicatedFactoryServiceAvailable(
                this.host.getPeerServiceUri(ExampleService.FACTORY_LINK),
                ServiceUriPaths.DEFAULT_NODE_SELECTOR);
        createReplicatedExampleTasks(exampleTaskLinks, UUID.randomUUID().toString());
        Set<String> deletedExampleLinks = deleteSomeServices(exampleLinks);
        this.host.setNodeGroupQuorum(this.nodeCount);
        hostToStop.setPort(0);
        hostToStop.setSecurePort(0);
        if (!VerificationHost.restartStatefulHost(hostToStop)) {
            this.host.log("Failed restart of host, aborting");
            return;
        }
        URI nodeGroupU = UriUtils.buildUri(hostToStop, ServiceUriPaths.DEFAULT_NODE_GROUP);
        URI eNodeGroupU = UriUtils.buildUri(existingHost, ServiceUriPaths.DEFAULT_NODE_GROUP);
        this.host.testStart(1);
        this.host.setSystemAuthorizationContext();
        this.host.joinNodeGroup(nodeGroupU, eNodeGroupU, this.nodeCount);
        this.host.testWait();
        this.host.addPeerNode(hostToStop);
        this.host.waitForNodeGroupConvergence(this.nodeCount);
        this.host.setNodeGroupQuorum(this.nodeCount);
        this.host.resetAuthorizationContext();
        this.host.waitFor("Task services not started in restarted host:" + exampleTaskLinks, () -> {
            return checkChildServicesIfStarted(exampleTaskLinks, hostToStop) == 0;
        });
        this.host.waitFor("Services not started in restarted host:" + exampleLinks, () -> {
            return checkChildServicesIfStarted(exampleLinks, hostToStop) == 0;
        });
        int deletedCount = deletedExampleLinks.size();
        this.host.waitFor("Deleted services still present in restarted host", () -> {
            return checkChildServicesIfStarted(deletedExampleLinks, hostToStop) == deletedCount;
        });
    }
    private Set<String> deleteSomeServices(Set<String> exampleLinks)
            throws Throwable {
        int deleteCount = exampleLinks.size() / 3;
        Iterator<String> itLinks = exampleLinks.iterator();
        Set<String> deletedExampleLinks = new HashSet<>();
        TestContext testContext = this.host.testCreate(deleteCount);
        for (int i = 0; i < deleteCount; i++) {
            String link = itLinks.next();
            deletedExampleLinks.add(link);
            exampleLinks.remove(link);
            Operation delete = Operation.createDelete(this.host.getPeerServiceUri(link))
                    .setCompletion(testContext.getCompletion());
            this.host.send(delete);
        }
        testContext.await();
        this.host.log("Deleted links: %s", deletedExampleLinks);
        return deletedExampleLinks;
    }
    private int checkChildServicesIfStarted(Set<String> exampleTaskLinks,
            VerificationHost host) {
        this.host.setSystemAuthorizationContext();
        int notStartedCount = 0;
        for (String s : exampleTaskLinks) {
            ProcessingStage st = host.getServiceStage(s);
            if (st == null) {
                notStartedCount++;
            }
        }
        this.host.resetAuthorizationContext();
        if (notStartedCount > 0) {
            this.host.log("%d services not started on %s (%s)", notStartedCount,
                    host.getPublicUri(), host.getId());
        }
        return notStartedCount;
    }
    private Map<ServiceHost, Map<URI, RoleState>> getRolesByHost(
            Map<ServiceHost, Collection<String>> roleLinksByHost) throws Throwable {
        Map<ServiceHost, Map<URI, RoleState>> roleStateByHost = new HashMap<>();
        for (ServiceHost h : roleLinksByHost.keySet()) {
            Collection<String> roleLinks = roleLinksByHost.get(h);
            Collection<URI> roleURIs = new ArrayList<>();
            for (String link : roleLinks) {
                roleURIs.add(UriUtils.buildUri(h, link));
            }
            Map<URI, RoleState> serviceState = this.host.getServiceState(null, RoleState.class,
                    roleURIs);
            roleStateByHost.put(h, serviceState);
        }
        return roleStateByHost;
    }
    private void verifyOperationJoinAcrossPeers(Map<URI, ReplicationTestServiceState> childStates)
            throws Throwable {
        List<Operation> joinedOps = new ArrayList<>();
        for (ReplicationTestServiceState st : childStates.values()) {
            Operation get = Operation.createGet(
                    this.host.getPeerServiceUri(st.documentSelfLink)).setReferer(
                    this.host.getReferer());
            joinedOps.add(get);
        }
        TestContext testContext = this.host.testCreate(1);
        OperationJoin
                .create(joinedOps)
                .setCompletion(
                        (ops, exc) -> {
                            if (exc != null) {
                                testContext.fail(exc.values().iterator().next());
                                return;
                            }
                            for (Operation o : ops.values()) {
                                ReplicationTestServiceState state = o.getBody(
                                        ReplicationTestServiceState.class);
                                if (state.stringField == null) {
                                    testContext.fail(new IllegalStateException());
                                    return;
                                }
                            }
                            testContext.complete();
                        })
                .sendWith(this.host.getPeerHost());
        testContext.await();
    }
    public Map<String, Set<String>> computeOwnerIdsPerLink(VerificationHost joinedHost,
            Collection<String> links)
            throws Throwable {
        TestContext testContext = this.host.testCreate(links.size());
        Map<String, Set<String>> expectedOwnersPerLink = new ConcurrentSkipListMap<>();
        CompletionHandler c = (o, e) -> {
            if (e != null) {
                testContext.fail(e);
                return;
            }
            SelectOwnerResponse rsp = o.getBody(SelectOwnerResponse.class);
            Set<String> eligibleNodeIds = new HashSet<>();
            for (NodeState ns : rsp.selectedNodes) {
                eligibleNodeIds.add(ns.id);
            }
            expectedOwnersPerLink.put(rsp.key, eligibleNodeIds);
            testContext.complete();
        };
        for (String link : links) {
            Operation selectOp = Operation.createGet(null)
                    .setCompletion(c)
                    .setExpiration(this.host.getOperationTimeoutMicros() + Utils.getNowMicrosUtc());
            joinedHost.selectOwner(this.replicationNodeSelector, link, selectOp);
        }
        testContext.await();
        return expectedOwnersPerLink;
    }
    public <T extends ServiceDocument> void verifyDocumentOwnerAndEpoch(Map<String, T> childStates,
            VerificationHost joinedHost,
            List<URI> joinedHostUris,
            int minExpectedEpochRetries,
            int maxExpectedEpochRetries, int expectedOwnerChanges)
            throws Throwable, InterruptedException, TimeoutException {
        Map<URI, NodeGroupState> joinedHostNodeGroupStates = this.host.getServiceState(null,
                NodeGroupState.class, joinedHostUris);
        Set<String> joinedHostOwnerIds = new HashSet<>();
        for (NodeGroupState st : joinedHostNodeGroupStates.values()) {
            joinedHostOwnerIds.add(st.documentOwner);
        }
        this.host.waitFor("ownership did not converge", () -> {
            Map<String, Set<String>> ownerIdsPerLink = computeOwnerIdsPerLink(joinedHost,
                    childStates.keySet());
            boolean isConverged = true;
            Map<String, Set<Long>> epochsPerLink = new HashMap<>();
            List<URI> nodeSelectorStatsUris = new ArrayList<>();
            for (URI baseNodeUri : joinedHostUris) {
                nodeSelectorStatsUris.add(UriUtils.buildUri(baseNodeUri,
                        ServiceUriPaths.DEFAULT_NODE_SELECTOR,
                        ServiceHost.SERVICE_URI_SUFFIX_STATS));
                URI factoryUri = UriUtils.buildUri(
                        baseNodeUri, this.replicationTargetFactoryLink);
                ServiceDocumentQueryResult factoryRsp = this.host
                        .getFactoryState(factoryUri);
                if (factoryRsp.documentLinks == null
                        || factoryRsp.documentLinks.size() != childStates.size()) {
                    isConverged = false;
                    this.host.log("Node %s does not have all services: %s", baseNodeUri,
                            Utils.toJsonHtml(factoryRsp));
                    break;
                }
                List<URI> childUris = new ArrayList<>();
                for (String link : childStates.keySet()) {
                    childUris.add(UriUtils.buildUri(baseNodeUri, link));
                }
                Map<URI, ServiceDocument> childDocs = this.host.getServiceState(null,
                        ServiceDocument.class, childUris);
                List<URI> childStatUris = new ArrayList<>();
                for (ServiceDocument state : childDocs.values()) {
                    if (state.documentOwner == null) {
                        this.host.log("Owner not set in service on new node: %s",
                                Utils.toJsonHtml(state));
                        isConverged = false;
                        break;
                    }
                    URI statUri = UriUtils.buildUri(baseNodeUri, state.documentSelfLink,
                            ServiceHost.SERVICE_URI_SUFFIX_STATS);
                    childStatUris.add(statUri);
                    Set<Long> epochs = epochsPerLink.get(state.documentEpoch);
                    if (epochs == null) {
                        epochs = new HashSet<>();
                        epochsPerLink.put(state.documentSelfLink, epochs);
                    }
                    epochs.add(state.documentEpoch);
                    Set<String> eligibleNodeIds = ownerIdsPerLink.get(state.documentSelfLink);
                    if (!joinedHostOwnerIds.contains(state.documentOwner)) {
                        this.host.log("Owner id for %s not expected: %s, valid ids: %s",
                                state.documentSelfLink, state.documentOwner, joinedHostOwnerIds);
                        isConverged = false;
                        break;
                    }
                    if (eligibleNodeIds != null && !eligibleNodeIds.contains(state.documentOwner)) {
                        this.host.log("Owner id for %s not eligible: %s, eligible ids: %s",
                                state.documentSelfLink, state.documentOwner, joinedHostOwnerIds);
                        isConverged = false;
                        break;
                    }
                }
                int nodeGroupMaintCount = 0;
                int docOwnerToggleOffCount = 0;
                int docOwnerToggleCount = 0;
                Map<URI, ServiceStats> allChildStats = this.host.getServiceState(null,
                        ServiceStats.class, childStatUris);
                for (ServiceStats childStats : allChildStats.values()) {
                    String parentLink = UriUtils.getParentPath(childStats.documentSelfLink);
                    Set<String> eligibleNodes = ownerIdsPerLink.get(parentLink);
                    if (!eligibleNodes.contains(childStats.documentOwner)) {
                        this.host.log("Stats for %s owner not expected. Is %s, should be %s",
                                parentLink, childStats.documentOwner,
                                ownerIdsPerLink.get(parentLink));
                        isConverged = false;
                        break;
                    }
                    ServiceStat maintStat = childStats.entries
                            .get(Service.STAT_NAME_NODE_GROUP_CHANGE_MAINTENANCE_COUNT);
                    if (maintStat != null) {
                        nodeGroupMaintCount++;
                    }
                    ServiceStat docOwnerToggleOffStat = childStats.entries
                            .get(Service.STAT_NAME_DOCUMENT_OWNER_TOGGLE_OFF_MAINT_COUNT);
                    if (docOwnerToggleOffStat != null) {
                        docOwnerToggleOffCount++;
                    }
                    ServiceStat docOwnerToggleStat = childStats.entries
                            .get(Service.STAT_NAME_DOCUMENT_OWNER_TOGGLE_ON_MAINT_COUNT);
                    if (docOwnerToggleStat != null) {
                        docOwnerToggleCount++;
                    }
                }
                this.host.log("Node group change maintenance observed: %d", nodeGroupMaintCount);
                if (nodeGroupMaintCount < expectedOwnerChanges) {
                    isConverged = false;
                }
                this.host.log("Toggled off doc owner count: %d, toggle on count: %d",
                        docOwnerToggleOffCount, docOwnerToggleCount);
                if (docOwnerToggleCount < childStates.size()) {
                    isConverged = false;
                }
                for (Set<Long> epochs : epochsPerLink.values()) {
                    if (epochs.size() > 1) {
                        this.host.log("Documents have different epochs:%s", epochs.toString());
                        isConverged = false;
                    }
                }
                if (!isConverged) {
                    break;
                }
            }
            return isConverged;
        });
    }
    private <T extends ServiceDocument> Map<String, T> doStateUpdateReplicationTest(Action action,
            int childCount, int updateCount,
            long expectedVersion,
            Function<T, Void> updateBodySetter,
            BiPredicate<T, T> convergenceChecker,
            Map<String, T> initialStatesPerChild) throws Throwable {
        return doStateUpdateReplicationTest(action, childCount, updateCount, expectedVersion,
                updateBodySetter, convergenceChecker, initialStatesPerChild, null, null);
    }
    private <T extends ServiceDocument> Map<String, T> doStateUpdateReplicationTest(Action action,
            int childCount, int updateCount,
            long expectedVersion,
            Function<T, Void> updateBodySetter,
            BiPredicate<T, T> convergenceChecker,
            Map<String, T> initialStatesPerChild,
            Map<Action, Long> countsPerAction,
            Map<Action, Long> elapsedTimePerAction) throws Throwable {
        int testCount = childCount * updateCount;
        String testName = "Replication with " + action;
        TestContext testContext = this.host.testCreate(testCount);
        testContext.setTestName(testName).logBefore();
        if (!this.expectFailure) {
            for (URI fu : this.host.getNodeGroupToFactoryMap(this.replicationTargetFactoryLink)
                    .values()) {
                waitForReplicatedFactoryServiceAvailable(fu, ServiceUriPaths.DEFAULT_NODE_SELECTOR);
            }
        }
        long before = Utils.getNowMicrosUtc();
        AtomicInteger failedCount = new AtomicInteger();
        for (T initState : initialStatesPerChild.values()) {
            updateBodySetter.apply(initState);
            for (int i = 0; i < updateCount; i++) {
                long sentTime = 0;
                if (this.expectFailure) {
                    sentTime = Utils.getNowMicrosUtc();
                }
                URI factoryOnRandomPeerUri = this.host
                        .getPeerServiceUri(this.replicationTargetFactoryLink);
                long finalSentTime = sentTime;
                this.host
                        .send(Operation
                                .createPatch(UriUtils.buildUri(factoryOnRandomPeerUri,
                                        initState.documentSelfLink))
                                .setAction(action)
                                .forceRemote()
                                .setBodyNoCloning(initState)
                                .setCompletion(
                                        (o, e) -> {
                                            if (e != null) {
                                                if (this.expectFailure) {
                                                    failedCount.incrementAndGet();
                                                    testContext.complete();
                                                    return;
                                                }
                                                testContext.fail(e);
                                                return;
                                            }
                                            if (this.expectFailure
                                                    && this.expectedFailureStartTimeMicros > 0
                                                    && finalSentTime > this.expectedFailureStartTimeMicros) {
                                                testContext.fail(new IllegalStateException(
                                                        "Request should have failed: %s"
                                                                + o.toString()
                                                                + " sent at " + finalSentTime));
                                                return;
                                            }
                                            testContext.complete();
                                        }));
            }
        }
        testContext.await();
        testContext.logAfter();
        updatePerfDataPerAction(action, before, (long) (childCount * updateCount), countsPerAction,
                elapsedTimePerAction);
        if (this.expectFailure) {
            this.host.log("Failed count: %d", failedCount.get());
            if (failedCount.get() == 0) {
                throw new IllegalStateException(
                        "Possible false negative but expected at least one failure");
            }
            return initialStatesPerChild;
        }
        if (action != Action.DELETE) {
            return waitForReplicatedFactoryChildServiceConvergence(initialStatesPerChild,
                    convergenceChecker,
                    childCount,
                    expectedVersion);
        }
        return waitForReplicatedFactoryChildServiceConvergence(initialStatesPerChild,
                convergenceChecker,
                0,
                expectedVersion);
    }
    private Map<String, ExampleServiceState> doExampleFactoryPostReplicationTest(int childCount,
            Map<Action, Long> countPerAction,
            Map<Action, Long> elapsedTimePerAction)
            throws Throwable {
        return doExampleFactoryPostReplicationTest(childCount, null,
                countPerAction, elapsedTimePerAction);
    }
    private Map<String, ExampleServiceState> doExampleFactoryPostReplicationTest(int childCount,
            EnumSet<TestProperty> props,
            Map<Action, Long> countPerAction,
            Map<Action, Long> elapsedTimePerAction) throws Throwable {
        if (props == null) {
            props = EnumSet.noneOf(TestProperty.class);
        }
        if (this.host == null) {
            setUp(this.nodeCount);
            this.host.joinNodesAndVerifyConvergence(this.host.getPeerCount());
        }
        if (props.contains(TestProperty.FORCE_FAILURE)) {
            this.host.toggleNegativeTestMode(true);
        }
        String factoryPath = this.replicationTargetFactoryLink;
        Map<String, ExampleServiceState> serviceStates = new HashMap<>();
        long before = Utils.getNowMicrosUtc();
        TestContext testContext = this.host.testCreate(childCount);
        testContext.setTestName("POST replication");
        testContext.logBefore();
        for (int i = 0; i < childCount; i++) {
            URI factoryOnRandomPeerUri = this.host.getPeerServiceUri(factoryPath);
            Operation post = Operation
                    .createPost(factoryOnRandomPeerUri)
                    .setCompletion(testContext.getCompletion());
            ExampleServiceState initialState = new ExampleServiceState();
            initialState.name = "" + post.getId();
            initialState.counter = Long.MIN_VALUE;
            initialState.documentSelfLink = "" + post.getId();
            post.setReferer(this.host.getReferer());
            this.host.sendRequest(post.setBody(initialState));
            initialState.documentSelfLink = UriUtils.buildUriPath(factoryPath,
                    initialState.documentSelfLink);
            serviceStates.put(initialState.documentSelfLink, initialState);
        }
        if (props.contains(TestProperty.FORCE_FAILURE)) {
            return serviceStates;
        }
        testContext.await();
        updatePerfDataPerAction(Action.POST, before, (long) this.serviceCount, countPerAction,
                elapsedTimePerAction);
        testContext.logAfter();
        return waitForReplicatedFactoryChildServiceConvergence(serviceStates,
                this.exampleStateConvergenceChecker,
                childCount,
                0L);
    }
    private void updateExampleServiceOptions(
            Map<String, ExampleServiceState> statesPerSelfLink) throws Throwable {
        if (this.postCreationServiceOptions == null || this.postCreationServiceOptions.isEmpty()) {
            return;
        }
        TestContext testContext = this.host.testCreate(statesPerSelfLink.size());
        URI nodeGroup = this.host.getNodeGroupMap().values().iterator().next();
        for (String link : statesPerSelfLink.keySet()) {
            URI bUri = UriUtils.buildBroadcastRequestUri(
                    UriUtils.buildUri(nodeGroup, link, ServiceHost.SERVICE_URI_SUFFIX_CONFIG),
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
            ServiceConfigUpdateRequest cfgBody = ServiceConfigUpdateRequest.create();
            cfgBody.addOptions = this.postCreationServiceOptions;
            this.host.send(Operation.createPatch(bUri)
                    .setBody(cfgBody)
                    .setCompletion(testContext.getCompletion()));
        }
        testContext.await();
    }
    private <T extends ServiceDocument> Map<String, T> waitForReplicatedFactoryChildServiceConvergence(
            Map<String, T> serviceStates,
            BiPredicate<T, T> stateChecker,
            int expectedChildCount, long expectedVersion)
            throws Throwable, TimeoutException {
        return waitForReplicatedFactoryChildServiceConvergence(
                getFactoriesPerNodeGroup(this.replicationTargetFactoryLink),
                serviceStates,
                stateChecker,
                expectedChildCount,
                expectedVersion);
    }
    private <T extends ServiceDocument> Map<String, T> waitForReplicatedFactoryChildServiceConvergence(
            Map<URI, URI> factories,
            Map<String, T> serviceStates,
            BiPredicate<T, T> stateChecker,
            int expectedChildCount, long expectedVersion)
            throws Throwable, TimeoutException {
        Map<String, T> updatedStatesPerSelfLink = new HashMap<>();
        Date expiration = new Date(new Date().getTime()
                + TimeUnit.SECONDS.toMillis(this.host.getTimeoutSeconds()));
        do {
            URI node = factories.keySet().iterator().next();
            AtomicInteger getFailureCount = new AtomicInteger();
            if (expectedChildCount != 0) {
                for (String link : serviceStates.keySet()) {
                    TestContext ctx = this.host.testCreate(1);
                    Operation get = Operation.createGet(UriUtils.buildUri(node, link))
                            .setReferer(this.host.getReferer())
                            .setExpiration(Utils.getNowMicrosUtc() + TimeUnit.SECONDS.toMicros(5))
                            .setCompletion(
                                    (o, e) -> {
                                        if (e != null) {
                                            getFailureCount.incrementAndGet();
                                        }
                                        ctx.completeIteration();
                                    });
                    this.host.sendRequest(get);
                    this.host.testWait(ctx);
                }
            }
            if (getFailureCount.get() > 0) {
                this.host.log("Child services not propagated yet. Failure count: %d",
                        getFailureCount.get());
                Thread.sleep(500);
                continue;
            }
            TestContext testContext = this.host.testCreate(factories.size());
            Map<URI, ServiceDocumentQueryResult> childServicesPerNode = new HashMap<>();
            for (URI remoteFactory : factories.values()) {
                URI factoryUriWithExpand = UriUtils.buildExpandLinksQueryUri(remoteFactory);
                Operation get = Operation.createGet(factoryUriWithExpand)
                        .setCompletion(
                                (o, e) -> {
                                    if (e != null) {
                                        testContext.complete();
                                        return;
                                    }
                                    if (!o.hasBody()) {
                                        testContext.complete();
                                        return;
                                    }
                                    ServiceDocumentQueryResult r = o
                                            .getBody(ServiceDocumentQueryResult.class);
                                    synchronized (childServicesPerNode) {
                                        childServicesPerNode.put(o.getUri(), r);
                                    }
                                    testContext.complete();
                                });
                this.host.send(get);
            }
            testContext.await();
            long expectedNodeCountPerLinkMax = factories.size();
            long expectedNodeCountPerLinkMin = expectedNodeCountPerLinkMax;
            if (this.replicationFactor != 0) {
                expectedNodeCountPerLinkMax = this.replicationFactor + 1;
                expectedNodeCountPerLinkMin = this.replicationFactor;
            }
            if (expectedChildCount == 0) {
                expectedNodeCountPerLinkMax = 0;
                expectedNodeCountPerLinkMin = 0;
            }
            Map<String, Set<URI>> linkToNodeMap = new HashMap<>();
            boolean isConverged = true;
            for (Entry<URI, ServiceDocumentQueryResult> entry : childServicesPerNode.entrySet()) {
                for (String link : entry.getValue().documentLinks) {
                    if (!serviceStates.containsKey(link)) {
                        this.host.log("service %s not expected, node: %s", link, entry.getKey());
                        isConverged = false;
                        continue;
                    }
                    Set<URI> hostsPerLink = linkToNodeMap.get(link);
                    if (hostsPerLink == null) {
                        hostsPerLink = new HashSet<>();
                    }
                    hostsPerLink.add(entry.getKey());
                    linkToNodeMap.put(link, hostsPerLink);
                }
            }
            if (!isConverged) {
                Thread.sleep(500);
                continue;
            }
            for (Entry<String, Set<URI>> e : linkToNodeMap.entrySet()) {
                if (e.getValue() == null && this.replicationFactor == 0) {
                    this.host.log("Service %s not found on any nodes", e.getKey());
                    isConverged = false;
                    continue;
                }
                if (e.getValue().size() < expectedNodeCountPerLinkMin
                        || e.getValue().size() > expectedNodeCountPerLinkMax) {
                    this.host.log("Service %s found on %d nodes, expected %d -> %d", e.getKey(), e
                                    .getValue().size(), expectedNodeCountPerLinkMin,
                            expectedNodeCountPerLinkMax);
                    isConverged = false;
                }
            }
            if (!isConverged) {
                Thread.sleep(500);
                continue;
            }
            if (expectedChildCount == 0) {
                return updatedStatesPerSelfLink;
            }
            URI factoryUri = factories.values().iterator().next();
            Class<?> stateType = serviceStates.values().iterator().next().getClass();
            waitForReplicatedFactoryServiceAvailable(factoryUri,
                    ServiceUriPaths.DEFAULT_NODE_SELECTOR);
            isConverged = true;
            for (Entry<String, Set<URI>> entry : linkToNodeMap.entrySet()) {
                String selfLink = entry.getKey();
                int convergedNodeCount = 0;
                for (URI nodeUri : entry.getValue()) {
                    ServiceDocumentQueryResult childLinksAndDocsPerHost = childServicesPerNode
                            .get(nodeUri);
                    Object jsonState = childLinksAndDocsPerHost.documents.get(selfLink);
                    if (jsonState == null && this.replicationFactor == 0) {
                        this.host
                                .log("Service %s not present on host %s", selfLink, entry.getKey());
                        continue;
                    }
                    if (jsonState == null) {
                        continue;
                    }
                    T initialState = serviceStates.get(selfLink);
                    if (initialState == null) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    T stateOnNode = (T) Utils.fromJson(jsonState, stateType);
                    if (!stateChecker.test(initialState, stateOnNode)) {
                        this.host
                                .log("State for %s not converged on node %s. Current state: %s, Initial: %s",
                                        selfLink, nodeUri, Utils.toJsonHtml(stateOnNode),
                                        Utils.toJsonHtml(initialState));
                        break;
                    }
                    if (stateOnNode.documentVersion < expectedVersion) {
                        this.host
                                .log("Version (%d, expected %d) not converged, state: %s",
                                        stateOnNode.documentVersion,
                                        expectedVersion,
                                        Utils.toJsonHtml(stateOnNode));
                        break;
                    }
                    if (stateOnNode.documentEpoch == null) {
                        this.host.log("Epoch is missing, state: %s",
                                Utils.toJsonHtml(stateOnNode));
                        break;
                    }
                    updatedStatesPerSelfLink.put(selfLink, stateOnNode);
                    convergedNodeCount++;
                }
                if (convergedNodeCount < expectedNodeCountPerLinkMin
                        || convergedNodeCount > expectedNodeCountPerLinkMax) {
                    isConverged = false;
                    break;
                }
            }
            if (isConverged) {
                return updatedStatesPerSelfLink;
            }
            Thread.sleep(500);
        } while (new Date().before(expiration));
        throw new TimeoutException();
    }
    private List<ServiceHostState> stopHostsToSimulateFailure(int failedNodeCount) {
        int k = 0;
        List<ServiceHostState> stoppedHosts = new ArrayList<>();
        for (VerificationHost hostToStop : this.host.getInProcessHostMap().values()) {
            this.host.log("Stopping host %s", hostToStop);
            stoppedHosts.add(hostToStop.getState());
            this.host.stopHost(hostToStop);
            k++;
            if (k >= failedNodeCount) {
                break;
            }
        }
        return stoppedHosts;
    }
    public static class StopVerificationTestService extends StatefulService {
        public Collection<URI> serviceTargets;
        public AtomicInteger outboundRequestCompletion = new AtomicInteger();
        public AtomicInteger outboundRequestFailureCompletion = new AtomicInteger();
        public StopVerificationTestService() {
            super(MinimalTestServiceState.class);
        }
        @Override
        public void handleStop(Operation deleteForStop) {
            for (URI uri : this.serviceTargets) {
                ReplicationTestServiceState body = new ReplicationTestServiceState();
                body.stringField = ReplicationTestServiceState.CLIENT_PATCH_HINT;
                for (int i = 0; i < 10; i++) {
                    Operation op = Operation.createPatch(this, uri.getPath()).setBody(body)
                            .setTargetReplicated(true)
                            .setCompletion((o, e) -> {
                                if (e != null) {
                                    this.outboundRequestFailureCompletion.incrementAndGet();
                                } else {
                                    this.outboundRequestCompletion.incrementAndGet();
                                }
                            });
                    sendRequest(op);
                }
            }
        }
    }
    private void stopHostsAndVerifyQueuing(Collection<VerificationHost> hostsToStop,
            VerificationHost remainingHost,
            Collection<URI> serviceTargets) throws Throwable {
        this.host.log("Starting to stop hosts and verify queuing");
        this.nodeGroupConfig.nodeRemovalDelayMicros = remainingHost.getMaintenanceIntervalMicros();
        this.host.setNodeGroupConfig(this.nodeGroupConfig);
        this.setOperationTimeoutMicros(TimeUnit.SECONDS.toMicros(10));
        this.host.setNodeGroupQuorum(1);
        List<StopVerificationTestService> verificationServices = new ArrayList<>();
        for (VerificationHost h : hostsToStop) {
            StopVerificationTestService s = new StopVerificationTestService();
            verificationServices.add(s);
            s.serviceTargets = serviceTargets;
            h.startServiceAndWait(s, UUID.randomUUID().toString(), null);
            this.host.stopHost(h);
        }
        Date exp = this.host.getTestExpiration();
        while (new Date().before(exp)) {
            boolean isConverged = true;
            for (StopVerificationTestService s : verificationServices) {
                if (s.outboundRequestCompletion.get() > 0) {
                    throw new IllegalStateException("Replicated request succeeded");
                }
                if (s.outboundRequestFailureCompletion.get() < serviceTargets.size()) {
                    this.host.log(
                            "Not converged yet: service %s on host %s has %d outbound request failures, which is lower than %d",
                            s.getSelfLink(), s.getHost().getId(),
                            s.outboundRequestFailureCompletion.get(), serviceTargets.size());
                    isConverged = false;
                    break;
                }
            }
            if (isConverged) {
                this.host.log("Done with stop hosts and verify queuing");
                return;
            }
            Thread.sleep(250);
        }
        throw new TimeoutException();
    }
    private void waitForReplicatedFactoryServiceAvailable(URI uri, String nodeSelectorPath)
            throws Throwable {
        if (this.skipAvailabilityChecks) {
            return;
        }
        if (UriUtils.isHostEqual(this.host, uri)) {
            VerificationHost host = this.host;
            URI peerUri = UriUtils.buildUri(uri.toString().replace(uri.getPath(), ""));
            VerificationHost peer = this.host.getInProcessHostMap().get(peerUri);
            if (peer != null) {
                host = peer;
            }
            TestContext ctx = host.testCreate(1);
            CompletionHandler ch = (o, e) -> {
                if (e != null) {
                    String msg = "Failed to check replicated factory service availability";
                    ctx.failIteration(new RuntimeException(msg, e));
                    return;
                }
                ctx.completeIteration();
            };
            host.registerForServiceAvailability(ch, nodeSelectorPath, true, uri.getPath());
            ctx.await();
        } else {
            this.host.waitForReplicatedFactoryServiceAvailable(uri, nodeSelectorPath);
        }
    }
}
