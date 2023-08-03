
package com.vmware.xenon.common;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.vmware.xenon.common.Operation.AuthorizationContext;
import com.vmware.xenon.common.Operation.CompletionHandler;
import com.vmware.xenon.common.Service.Action;
import com.vmware.xenon.common.TestAuthorization.AuthzStatefulService.AuthzState;
import com.vmware.xenon.common.test.AuthorizationHelper;
import com.vmware.xenon.common.test.QueryTestUtils;
import com.vmware.xenon.common.test.TestContext;
import com.vmware.xenon.common.test.TestRequestSender;
import com.vmware.xenon.common.test.TestRequestSender.FailureResponse;
import com.vmware.xenon.common.test.VerificationHost;
import com.vmware.xenon.services.common.AuthorizationCacheUtils;
import com.vmware.xenon.services.common.AuthorizationContextService;
import com.vmware.xenon.services.common.ExampleService;
import com.vmware.xenon.services.common.ExampleService.ExampleServiceState;
import com.vmware.xenon.services.common.GuestUserService;
import com.vmware.xenon.services.common.MinimalTestService;
import com.vmware.xenon.services.common.QueryTask;
import com.vmware.xenon.services.common.QueryTask.Query;
import com.vmware.xenon.services.common.QueryTask.Query.Builder;
import com.vmware.xenon.services.common.QueryTask.QueryTerm.MatchType;
import com.vmware.xenon.services.common.RoleService;
import com.vmware.xenon.services.common.RoleService.Policy;
import com.vmware.xenon.services.common.RoleService.RoleState;
import com.vmware.xenon.services.common.SystemUserService;
import com.vmware.xenon.services.common.UserGroupService;
import com.vmware.xenon.services.common.UserGroupService.UserGroupState;
import com.vmware.xenon.services.common.UserService.UserState;
public class TestAuthorization extends BasicTestCase {
    public static class AuthzStatelessService extends StatelessService {
        @Override
        public void handleRequest(Operation op) {
            if (op.getAction() == Action.PATCH) {
                op.complete();
                return;
            }
            super.handleRequest(op);
        }
    }
    public static class AuthzStatefulService extends StatefulService {
        public static class AuthzState extends ServiceDocument {
            public String userLink;
        }
        public AuthzStatefulService() {
            super(AuthzState.class);
        }
        @Override
        public void handleStart(Operation post) {
            AuthzState body = post.getBody(AuthzState.class);
            AuthorizationContext authorizationContext = getAuthorizationContextForSubject(
                    body.userLink);
            if (authorizationContext == null ||
                    !authorizationContext.getClaims().getSubject().equals(body.userLink)) {
                post.fail(Operation.STATUS_CODE_INTERNAL_ERROR);
                return;
            }
            post.complete();
        }
    }
    public int serviceCount = 10;
    private String userServicePath;
    private AuthorizationHelper authHelper;
    @Override
    public void beforeHostStart(VerificationHost host) {
        host.setAuthorizationService(new AuthorizationContextService());
        host.setAuthorizationEnabled(true);
        CommandLineArgumentParser.parseFromProperties(this);
    }
    @Before
    public void enableTracing() throws Throwable {
        this.host.toggleOperationTracing(this.host.getUri(), true);
    }
    @After
    public void disableTracing() throws Throwable {
        this.host.toggleOperationTracing(this.host.getUri(), false);
    }
    @Before
    public void setupRoles() throws Throwable {
        this.host.setSystemAuthorizationContext();
        this.authHelper = new AuthorizationHelper(this.host);
        this.userServicePath = this.authHelper.createUserService(this.host, "jane@doe.com");
        this.authHelper.createRoles(this.host, "jane@doe.com");
        this.host.resetAuthorizationContext();
    }
    @Test
    public void factoryGetWithOData() {
        URI exampleFactoryUriWithOData = UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK,
                "$limit=10");
        TestRequestSender sender = this.host.getTestRequestSender();
        FailureResponse rsp = sender.sendAndWaitFailure(Operation.createGet(exampleFactoryUriWithOData));
        ServiceErrorResponse errorRsp = rsp.op.getErrorResponseBody();
        assertTrue(errorRsp.message.toLowerCase().contains("forbidden"));
        assertTrue(errorRsp.message.contains(UriUtils.URI_PARAM_ODATA_TENANTLINKS));
        exampleFactoryUriWithOData = UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK,
                "$filter=name eq someone");
        rsp = sender.sendAndWaitFailure(Operation.createGet(exampleFactoryUriWithOData));
        errorRsp = rsp.op.getErrorResponseBody();
        assertTrue(errorRsp.message.toLowerCase().contains("forbidden"));
        assertTrue(errorRsp.message.contains(UriUtils.URI_PARAM_ODATA_TENANTLINKS));
        URI exampleFactoryUri = UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK);
        Operation rspOp = sender.sendAndWait(Operation.createGet(exampleFactoryUri));
        ServiceDocumentQueryResult queryRsp = rspOp.getBody(ServiceDocumentQueryResult.class);
        assertEquals(0L, (long) queryRsp.documentCount);
    }
    @Test
    public void statelessServiceAuthorization() throws Throwable {
        this.host.setSystemAuthorizationContext();
        String serviceLink = UUID.randomUUID().toString();
        String resourceGroupLink = this.authHelper.createResourceGroup(this.host,
                "stateless-service-group", Builder.create()
                        .addFieldClause(
                                ServiceDocument.FIELD_NAME_SELF_LINK,
                                UriUtils.URI_PATH_CHAR + serviceLink)
                        .build());
        this.authHelper.createRole(this.host, this.authHelper.getUserGroupLink(),
                resourceGroupLink,
                new HashSet<>(Arrays.asList(Action.GET, Action.POST, Action.PATCH, Action.DELETE)));
        this.host.resetAuthorizationContext();
        CompletionHandler ch = (o, e) -> {
            if (e == null || o.getStatusCode() != Operation.STATUS_CODE_FORBIDDEN) {
                this.host.failIteration(new IllegalStateException(
                        "Operation did not fail with proper status code"));
                return;
            }
            this.host.completeIteration();
        };
        this.host.assumeIdentity(this.userServicePath);
        Operation post = Operation.createPost(UriUtils.buildUri(this.host, serviceLink));
        this.host.testStart(1);
        post.setCompletion(this.host.getCompletion());
        this.host.startService(post, new AuthzStatelessService());
        this.host.testWait();
        this.host.testStart(1);
        Operation delete = Operation.createDelete(post.getUri())
                .setCompletion(this.host.getCompletion());
        this.host.send(delete);
        this.host.testWait();
        this.host.resetAuthorizationContext();
        this.host.testStart(1);
        post = Operation.createPost(UriUtils.buildUri(this.host, serviceLink));
        post.setCompletion(ch);
        this.host.startService(post, new AuthzStatelessService());
        this.host.testWait();
        this.host.assumeIdentity(this.userServicePath);
        post = Operation.createPost(UriUtils.buildUri(this.host, serviceLink));
        this.host.testStart(1);
        post.setCompletion(this.host.getCompletion());
        this.host.startService(post, new AuthzStatelessService());
        this.host.testWait();
        Operation patch = Operation.createPatch(UriUtils.buildUri(this.host, serviceLink));
        patch.setBody(new ServiceDocument());
        this.host.testStart(1);
        patch.setCompletion(this.host.getCompletion());
        this.host.send(patch);
        this.host.testWait();
        this.host.resetAuthorizationContext();
        patch = Operation.createPatch(UriUtils.buildUri(this.host, serviceLink));
        patch.setBody(new ServiceDocument());
        this.host.testStart(1);
        patch.setCompletion(ch);
        this.host.send(patch);
        this.host.testWait();
    }
    @Test
    public void queryTasksDirectAndContinuous() throws Throwable {
        this.host.assumeIdentity(this.userServicePath);
        createExampleServices("jane");
        this.host.createAndWaitSimpleDirectQuery(ServiceDocument.FIELD_NAME_AUTH_PRINCIPAL_LINK,
                this.userServicePath, this.serviceCount, this.serviceCount);
        QueryTask qt = QueryTask.Builder.create().setResultLimit(this.serviceCount / 2)
                .build();
        qt.querySpec.query = Query.Builder.create()
                .addFieldClause(ServiceDocument.FIELD_NAME_AUTH_PRINCIPAL_LINK,
                        this.userServicePath)
                .build();
        URI taskUri = this.host.createQueryTaskService(qt);
        this.host.waitFor("task not finished in time", () -> {
            QueryTask r = this.host.getServiceState(null, QueryTask.class, taskUri);
            if (TaskState.isFailed(r.taskInfo)) {
                throw new IllegalStateException("task failed");
            }
            if (TaskState.isFinished(r.taskInfo)) {
                qt.taskInfo = r.taskInfo;
                qt.results = r.results;
                return true;
            }
            return false;
        });
        TestContext ctx = this.host.testCreate(1);
        Operation get = Operation.createGet(UriUtils.buildUri(this.host, qt.results.nextPageLink))
                .setCompletion(ctx.getCompletion());
        this.host.send(get);
        ctx.await();
        TestContext kryoCtx = this.host.testCreate(1);
        Operation patchOp = Operation.createPatch(this.host, ExampleService.FACTORY_LINK + "/foo")
                .setBody(new ServiceDocument())
                .setContentType(Operation.MEDIA_TYPE_APPLICATION_KRYO_OCTET_STREAM)
                .setCompletion((o, e) -> {
                    if (e != null && o.getStatusCode() == Operation.STATUS_CODE_UNAUTHORIZED) {
                        kryoCtx.completeIteration();
                        return;
                    }
                    kryoCtx.failIteration(new IllegalStateException("expected a failure"));
                });
        this.host.send(patchOp);
        kryoCtx.await();
        int requestCount = this.serviceCount;
        TestContext notifyCtx = this.testCreate(requestCount);
        Consumer<Operation> notify = (o) -> {
            o.complete();
            String subject = o.getAuthorizationContext().getClaims().getSubject();
            if (!this.userServicePath.equals(subject)) {
                notifyCtx.fail(new IllegalStateException(
                        "Invalid auth subject in notification: " + subject));
                return;
            }
            this.host.log("Received authorized notification for index patch: %s", o.toString());
            notifyCtx.complete();
        };
        Query q = Query.Builder.create()
                .addKindFieldClause(ExampleServiceState.class)
                .build();
        QueryTask cqt = QueryTask.Builder.create().setQuery(q).build();
        URI notifyURI = QueryTestUtils.startAndSubscribeToContinuousQuery(
                this.host.getTestRequestSender(), this.host, cqt,
                notify);
        this.host.setSystemAuthorizationContext();
        createExampleServices("jane");
        this.host.log("Waiting on continiuous query task notifications (%d)", requestCount);
        notifyCtx.await();
        this.host.resetSystemAuthorizationContext();
        this.host.assumeIdentity(this.userServicePath);
        QueryTestUtils.stopContinuousQuerySubscription(
                this.host.getTestRequestSender(), this.host, notifyURI,
                cqt);
    }
    @Test
    public void validateKryoOctetStreamRequests() throws Throwable {
        Consumer<Boolean> validate = (expectUnauthorizedResponse) -> {
            TestContext kryoCtx = this.host.testCreate(1);
            Operation patchOp = Operation.createPatch(this.host, ExampleService.FACTORY_LINK + "/foo")
                    .setBody(new ServiceDocument())
                    .setContentType(Operation.MEDIA_TYPE_APPLICATION_KRYO_OCTET_STREAM)
                    .setCompletion((o, e) -> {
                        boolean isUnauthorizedResponse = o.getStatusCode() == Operation.STATUS_CODE_UNAUTHORIZED;
                        if (expectUnauthorizedResponse == isUnauthorizedResponse) {
                            kryoCtx.completeIteration();
                            return;
                        }
                        kryoCtx.failIteration(new IllegalStateException("Response did not match expectation"));
                    });
            this.host.send(patchOp);
            kryoCtx.await();
        };
        this.host.resetAuthorizationContext();
        validate.accept(true);
        this.host.assumeIdentity(this.userServicePath);
        validate.accept(true);
        this.host.assumeIdentity(SystemUserService.SELF_LINK);
        validate.accept(false);
    }
    @Test
    public void contextPropagationOnScheduleAndRunContext() throws Throwable {
        this.host.assumeIdentity(this.userServicePath);
        AuthorizationContext callerAuthContext = OperationContext.getAuthorizationContext();
        Runnable task = () -> {
            if (OperationContext.getAuthorizationContext().equals(callerAuthContext)) {
                this.host.completeIteration();
                return;
            }
            this.host.failIteration(new IllegalStateException("Incorrect auth context obtained"));
        };
        this.host.testStart(1);
        this.host.schedule(task, 1, TimeUnit.MILLISECONDS);
        this.host.testWait();
        this.host.testStart(1);
        this.host.run(task);
        this.host.testWait();
    }
    @Test
    public void guestAuthorization() throws Throwable {
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        String userGroupLink =
                this.authHelper.createUserGroup(this.host, "guest-user-group", Builder.create()
                        .addFieldClause(
                                ServiceDocument.FIELD_NAME_SELF_LINK,
                                GuestUserService.SELF_LINK)
                        .build());
        String exampleServiceResourceGroupLink =
                this.authHelper.createResourceGroup(this.host, "guest-resource-group", Builder.create()
                        .addFieldClause(
                                ExampleServiceState.FIELD_NAME_KIND,
                                Utils.buildKind(ExampleServiceState.class))
                        .addFieldClause(
                                ExampleServiceState.FIELD_NAME_NAME,
                                "guest")
                        .build());
        this.authHelper.createRole(this.host, userGroupLink, exampleServiceResourceGroupLink,
                new HashSet<>(Arrays.asList(Action.GET, Action.POST, Action.PATCH)));
        Map<URI, ExampleServiceState> exampleServices = new HashMap<>();
        exampleServices.putAll(createExampleServices("jane"));
        exampleServices.putAll(createExampleServices("guest"));
        OperationContext.setAuthorizationContext(null);
        TestRequestSender sender = this.host.getTestRequestSender();
        Operation responseOp = sender.sendAndWait(Operation.createGet(this.host, ExampleService.FACTORY_LINK));
        ServiceDocumentQueryResult getResult = responseOp.getBody(ServiceDocumentQueryResult.class);
        assertAuthorizedServicesInResult("guest", exampleServices, getResult);
        String guestLink = getResult.documentLinks.iterator().next();
        ExampleServiceState state = new ExampleServiceState();
        state.counter = 2L;
        responseOp = sender.sendAndWait(Operation.createPatch(this.host, guestLink).setBody(state));
        assertEquals(Operation.STATUS_CODE_OK, responseOp.getStatusCode());
        state.counter = 3L;
        FailureResponse failureResponse = sender.sendAndWaitFailure(
                Operation.createPatch(this.host, guestLink)
                        .setContentType(Operation.MEDIA_TYPE_APPLICATION_KRYO_OCTET_STREAM)
                        .setBody(state));
        assertEquals(Operation.STATUS_CODE_UNAUTHORIZED, failureResponse.op.getStatusCode());
    }
    @Test
    public void testInvalidUserAndResourceGroup() throws Throwable {
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        AuthorizationHelper authsetupHelper = new AuthorizationHelper(this.host);
        String email = "foo@foo.com";
        String userLink = authsetupHelper.createUserService(this.host, email);
        Query userGroupQuery = Query.Builder.create().addFieldClause(UserState.FIELD_NAME_EMAIL, email).build();
        String userGroupLink = authsetupHelper.createUserGroup(this.host, email, userGroupQuery);
        authsetupHelper.createRole(this.host, userGroupLink, "foo", EnumSet.allOf(Action.class));
        this.host.assumeIdentity(userLink);
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK)));
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        UserState patchUserState = new UserState();
        patchUserState.userGroupLinks = Collections.singleton("foo");
        this.host.sendAndWaitExpectSuccess(
                Operation.createPatch(UriUtils.buildUri(this.host, userLink)).setBody(patchUserState));
        this.host.assumeIdentity(userLink);
        this.host.sendAndWaitExpectSuccess(
                Operation.createGet(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK)));
    }
    @Test
    public void actionBasedAuthorization() throws Throwable {
        this.host.assumeIdentity(this.userServicePath);
        Map<URI, ExampleServiceState> exampleServices = createExampleServices("jane");
        final ServiceDocumentQueryResult[] factoryGetResult = new ServiceDocumentQueryResult[1];
        Operation getFactory = Operation.createGet(
                UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                .setCompletion((o, e) -> {
                    if (e != null) {
                        this.host.failIteration(e);
                        return;
                    }
                    factoryGetResult[0] = o.getBody(ServiceDocumentQueryResult.class);
                    this.host.completeIteration();
                });
        this.host.testStart(1);
        this.host.send(getFactory);
        this.host.testWait();
        Set<String> selfLinks = new HashSet<>(factoryGetResult[0].documentLinks);
        for (String selfLink : selfLinks) {
            Operation deleteOperation =
                    Operation.createDelete(UriUtils.buildUri(this.host, selfLink))
                            .setCompletion((o, e) -> {
                                if (o.getStatusCode() != Operation.STATUS_CODE_FORBIDDEN) {
                                    String message = String.format("Expected %d, got %s",
                                            Operation.STATUS_CODE_FORBIDDEN,
                                            o.getStatusCode());
                                    this.host.failIteration(new IllegalStateException(message));
                                    return;
                                }
                                this.host.completeIteration();
                            });
            this.host.testStart(1);
            this.host.send(deleteOperation);
            this.host.testWait();
        }
        for (String selfLink : selfLinks) {
            Operation patchOperation =
                    Operation.createPatch(UriUtils.buildUri(this.host, selfLink))
                        .setBody(exampleServices.get(selfLink))
                        .setCompletion((o, e) -> {
                            if (o.getStatusCode() != Operation.STATUS_CODE_OK) {
                                String message = String.format("Expected %d, got %s",
                                        Operation.STATUS_CODE_OK,
                                        o.getStatusCode());
                                this.host.failIteration(new IllegalStateException(message));
                                return;
                            }
                            this.host.completeIteration();
                        });
            this.host.testStart(1);
            this.host.send(patchOperation);
            this.host.testWait();
        }
    }
    @Test
    public void testAllowAndDenyRoles() throws Exception {
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        ExampleServiceState state = createExampleServiceState("testExampleOK", 1L);
        Operation response = this.host.waitForResponse(
                Operation.createPost(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                        .setBody(state));
        assertEquals(Operation.STATUS_CODE_OK, response.getStatusCode());
        state = response.getBody(ExampleServiceState.class);
        assertAccess(Policy.DENY);
        buildRole("AllowRole", Policy.ALLOW);
        assertAccess(Policy.ALLOW);
        buildRole("DenyRole", Policy.DENY);
        assertAccess(Policy.DENY);
        buildRole("AnotherAllowRole", Policy.ALLOW);
        assertAccess(Policy.DENY);
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        response = this.host.waitForResponse(Operation.createDelete(
                UriUtils.buildUri(this.host,
                        UriUtils.buildUriPath(RoleService.FACTORY_LINK, "DenyRole"))));
        assertEquals(Operation.STATUS_CODE_OK, response.getStatusCode());
        assertAccess(Policy.ALLOW);
    }
    private void buildRole(String roleName, Policy policy) {
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        TestContext ctx = this.host.testCreate(1);
        AuthorizationSetupHelper.create().setHost(this.host)
                .setRoleName(roleName)
                .setUserGroupQuery(Query.Builder.create()
                        .addCollectionItemClause(UserState.FIELD_NAME_EMAIL, "jane@doe.com")
                        .build())
                .setResourceQuery(Query.Builder.create()
                        .addFieldClause(ServiceDocument.FIELD_NAME_SELF_LINK,
                                ExampleService.FACTORY_LINK,
                                MatchType.PREFIX)
                        .build())
                .setVerbs(EnumSet.of(Action.POST, Action.PUT, Action.PATCH, Action.GET,
                        Action.DELETE))
                .setPolicy(policy)
                .setCompletion((authEx) -> {
                    if (authEx != null) {
                        ctx.failIteration(authEx);
                        return;
                    }
                    ctx.completeIteration();
                }).setupRole();
        this.host.testWait(ctx);
    }
    private void assertAccess(Policy policy) throws Exception {
        this.host.assumeIdentity(this.userServicePath);
        Operation response = this.host.waitForResponse(
                Operation.createPost(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                        .setBody(createExampleServiceState("testExampleDeny", 2L)));
        if (policy == Policy.DENY) {
            assertEquals(Operation.STATUS_CODE_FORBIDDEN, response.getStatusCode());
        } else {
            assertEquals(Operation.STATUS_CODE_OK, response.getStatusCode());
        }
        response = this.host.waitForResponse(
                Operation.createGet(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK)));
        assertEquals(Operation.STATUS_CODE_OK, response.getStatusCode());
        ServiceDocumentQueryResult result = response.getBody(ServiceDocumentQueryResult.class);
        if (policy == Policy.DENY) {
            assertEquals(Long.valueOf(0L), result.documentCount);
        } else {
            assertNotNull(result.documentCount);
            assertNotEquals(Long.valueOf(0L), result.documentCount);
        }
    }
    @Test
    public void statefulServiceAuthorization() throws Throwable {
        OperationContext.setAuthorizationContext(this.host.getSystemAuthorizationContext());
        Map<URI, ExampleServiceState> exampleServices = createExampleServices("john");
        OperationContext.setAuthorizationContext(null);
        ExampleServiceState state = createExampleServiceState("jane", new Long("100"));
        TestContext ctx1 = this.host.testCreate(1);
        this.host.send(
                Operation.createPost(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                        .setBody(state)
                        .setCompletion((o, e) -> {
                            if (o.getStatusCode() != Operation.STATUS_CODE_FORBIDDEN) {
                                String message = String.format("Expected %d, got %s",
                                        Operation.STATUS_CODE_FORBIDDEN,
                                        o.getStatusCode());
                                ctx1.failIteration(new IllegalStateException(message));
                                return;
                            }
                            ctx1.completeIteration();
                        }));
        this.host.testWait(ctx1);
        TestContext ctx2 = this.host.testCreate(1);
        this.host.send(
                Operation.createGet(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                .setCompletion((o, e) -> {
                    if (e != null) {
                        ctx2.failIteration(new IllegalStateException(e));
                        return;
                    }
                    ServiceDocumentQueryResult res = o
                            .getBody(ServiceDocumentQueryResult.class);
                    if (!res.documentLinks.isEmpty()) {
                        String message = String.format("Expected 0 results; Got %d",
                                res.documentLinks.size());
                        ctx2.failIteration(new IllegalStateException(message));
                        return;
                    }
                    ctx2.completeIteration();
                }));
        this.host.testWait(ctx2);
        this.host.assumeIdentity(this.userServicePath);
        exampleServices.putAll(createExampleServices("jane"));
        verifyJaneAccess(exampleServices, null);
        TestContext ctx3 = this.host.testCreate(1);
        final ServiceDocumentQueryResult[] factoryGetResult = new ServiceDocumentQueryResult[1];
        Operation getFactory = Operation.createGet(
                UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                .setCompletion((o, e) -> {
                    if (e != null) {
                        ctx3.failIteration(e);
                        return;
                    }
                    factoryGetResult[0] = o.getBody(ServiceDocumentQueryResult.class);
                    ctx3.completeIteration();
                });
        this.host.send(getFactory);
        this.host.testWait(ctx3);
        assertAuthorizedServicesInResult("jane", exampleServices, factoryGetResult[0]);
        QueryTask.QuerySpecification q = new QueryTask.QuerySpecification();
        q.query.setTermPropertyName(ServiceDocument.FIELD_NAME_KIND)
                .setTermMatchValue(Utils.buildKind(ExampleServiceState.class));
        URI u = this.host.createQueryTaskService(QueryTask.create(q));
        QueryTask task = this.host.waitForQueryTaskCompletion(q, 1, 1, u, false, true, false);
        assertEquals(TaskState.TaskStage.FINISHED, task.taskInfo.stage);
        assertAuthorizedServicesInResult("jane", exampleServices, task.results);
        OperationContext.setAuthorizationContext(null);
        String authToken = generateAuthToken(this.userServicePath);
        verifyJaneAccess(exampleServices, authToken);
        this.host.setSystemAuthorizationContext();
        AuthzStatefulService s = new AuthzStatefulService();
        this.host.addPrivilegedService(AuthzStatefulService.class);
        AuthzState body = new AuthzState();
        body.userLink = this.userServicePath;
        this.host.startServiceAndWait(s, UUID.randomUUID().toString(), body);
        this.host.resetSystemAuthorizationContext();
    }
    private AuthorizationContext assumeIdentityAndGetContext(String userLink,
            Service privilegedService, boolean populateCache) throws Throwable {
        AuthorizationContext authContext = this.host.assumeIdentity(userLink);
        if (populateCache) {
            this.host.sendAndWaitExpectSuccess(
                    Operation.createGet(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK)));
        }
        return this.host.getAuthorizationContext(privilegedService, authContext.getToken());
    }
    @Test
    public void authCacheClearToken() throws Throwable {
        this.host.setSystemAuthorizationContext();
        AuthorizationHelper authHelperForFoo = new AuthorizationHelper(this.host);
        String email = "foo@foo.com";
        String fooUserLink = authHelperForFoo.createUserService(this.host, email);
        MinimalTestService s = new MinimalTestService();
        this.host.addPrivilegedService(MinimalTestService.class);
        this.host.startServiceAndWait(s, UUID.randomUUID().toString(), null);
        this.host.resetSystemAuthorizationContext();
        AuthorizationContext authContext1 = assumeIdentityAndGetContext(fooUserLink, s, true);
        AuthorizationContext authContext2 = assumeIdentityAndGetContext(fooUserLink, s, true);
        assertNotNull(authContext1);
        assertNotNull(authContext2);
        this.host.setSystemAuthorizationContext();
        Operation clearAuthOp = new Operation();
        clearAuthOp.setUri(UriUtils.buildUri(this.host, fooUserLink));
        TestContext ctx = this.host.testCreate(1);
        clearAuthOp.setCompletion(ctx.getCompletion());
        AuthorizationCacheUtils.clearAuthzCacheForUser(s, clearAuthOp);
        clearAuthOp.complete();
        this.host.testWait(ctx);
        this.host.resetSystemAuthorizationContext();
        assertNull(this.host.getAuthorizationContext(s, authContext1.getToken()));
        assertNull(this.host.getAuthorizationContext(s, authContext2.getToken()));
    }
    @Test
    public void updateAuthzCache() throws Throwable {
        ExecutorService executor = null;
        try {
            this.host.setSystemAuthorizationContext();
            AuthorizationHelper authsetupHelper = new AuthorizationHelper(this.host);
            String email = "foo@foo.com";
            String userLink = authsetupHelper.createUserService(this.host, email);
            Query userGroupQuery = Query.Builder.create().addFieldClause(UserState.FIELD_NAME_EMAIL, email).build();
            String userGroupLink = authsetupHelper.createUserGroup(this.host, email, userGroupQuery);
            UserState patchState = new UserState();
            patchState.userGroupLinks = Collections.singleton(userGroupLink);
            this.host.sendAndWaitExpectSuccess(
                    Operation.createPatch(UriUtils.buildUri(this.host, userLink))
                    .setBody(patchState));
            TestContext ctx = this.host.testCreate(this.serviceCount);
            Service s = this.host.startServiceAndWait(MinimalTestService.class, UUID.randomUUID()
                    .toString());
            executor = this.host.allocateExecutor(s);
            this.host.resetSystemAuthorizationContext();
            for (int i = 0; i < this.serviceCount; i++) {
                this.host.run(executor, () -> {
                    String serviceName = UUID.randomUUID().toString();
                    try {
                        this.host.setSystemAuthorizationContext();
                        Query resourceQuery = Query.Builder.create().addFieldClause(ExampleServiceState.FIELD_NAME_NAME,
                                serviceName).build();
                        String resourceGroupLink = authsetupHelper.createResourceGroup(this.host, serviceName, resourceQuery);
                        authsetupHelper.createRole(this.host, userGroupLink, resourceGroupLink, EnumSet.allOf(Action.class));
                        this.host.resetSystemAuthorizationContext();
                        this.host.assumeIdentity(userLink);
                        ExampleServiceState exampleState = new ExampleServiceState();
                        exampleState.name = serviceName;
                        exampleState.documentSelfLink = serviceName;
                        for (int retryCounter = 0; retryCounter < 3; retryCounter++) {
                            try {
                                this.host.sendAndWaitExpectSuccess(
                                        Operation.createPost(UriUtils.buildUri(this.host, ExampleService.FACTORY_LINK))
                                        .setBody(exampleState));
                                break;
                            } catch (Throwable t) {
                                this.host.log(Level.WARNING, "Error creating example service: " + t.getMessage());
                                if (retryCounter == 2) {
                                    ctx.fail(new IllegalStateException("Example service creation failed thrice"));
                                    return;
                                }
                            }
                        }
                        this.host.sendAndWaitExpectSuccess(
                                Operation.createDelete(UriUtils.buildUri(this.host,
                                        UriUtils.buildUriPath(ExampleService.FACTORY_LINK, serviceName))));
                        ctx.complete();
                    } catch (Throwable e) {
                        this.host.log(Level.WARNING, e.getMessage());
                        ctx.fail(e);
                    }
                });
            }
            this.host.testWait(ctx);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }
    @Test
    public void testAuthzUtils() throws Throwable {
        this.host.setSystemAuthorizationContext();
        AuthorizationHelper authHelperForFoo = new AuthorizationHelper(this.host);
        String email = "foo@foo.com";
        String fooUserLink = authHelperForFoo.createUserService(this.host, email);
        UserState patchState = new UserState();
        patchState.userGroupLinks = new HashSet<String>();
        patchState.userGroupLinks.add(UriUtils.buildUriPath(
                UserGroupService.FACTORY_LINK, authHelperForFoo.getUserGroupName(email)));
        authHelperForFoo.patchUserService(this.host, fooUserLink, patchState);
        authHelperForFoo.createRoles(this.host, email);
        MinimalTestService s = new MinimalTestService();
        this.host.addPrivilegedService(MinimalTestService.class);
        this.host.startServiceAndWait(s, UUID.randomUUID().toString(), null);
        this.host.resetSystemAuthorizationContext();
        String userGroupLink = authHelperForFoo.getUserGroupLink();
        String resourceGroupLink = authHelperForFoo.getResourceGroupLink();
        String roleLink = authHelperForFoo.getRoleLink();
        assertNotNull(assumeIdentityAndGetContext(fooUserLink, s, true));
        this.host.setSystemAuthorizationContext();
        Operation getUserGroupStateOp =
                Operation.createGet(UriUtils.buildUri(this.host, userGroupLink));
        Operation resultOp = this.host.waitForResponse(getUserGroupStateOp);
        UserGroupState userGroupState = resultOp.getBody(UserGroupState.class);
        Operation clearAuthOp = new Operation();
        TestContext ctx = this.host.testCreate(1);
        clearAuthOp.setCompletion(ctx.getCompletion());
        AuthorizationCacheUtils.clearAuthzCacheForUserGroup(s, clearAuthOp, userGroupState);
        clearAuthOp.complete();
        this.host.testWait(ctx);
        this.host.resetSystemAuthorizationContext();
        assertNull(assumeIdentityAndGetContext(fooUserLink, s, false));
        assertNotNull(assumeIdentityAndGetContext(fooUserLink, s, true));
        this.host.setSystemAuthorizationContext();
        clearAuthOp = new Operation();
        ctx = this.host.testCreate(1);
        clearAuthOp.setCompletion(ctx.getCompletion());
        clearAuthOp.setUri(UriUtils.buildUri(this.host, resourceGroupLink));
        AuthorizationCacheUtils.clearAuthzCacheForResourceGroup(s, clearAuthOp);
        clearAuthOp.complete();
        this.host.testWait(ctx);
        this.host.resetSystemAuthorizationContext();
        assertNull(assumeIdentityAndGetContext(fooUserLink, s, false));
        assertNotNull(assumeIdentityAndGetContext(fooUserLink, s, true));
        this.host.setSystemAuthorizationContext();
        Operation getRoleStateOp =
                Operation.createGet(UriUtils.buildUri(this.host, roleLink));
        resultOp = this.host.waitForResponse(getRoleStateOp);
        RoleState roleState = resultOp.getBody(RoleState.class);
        clearAuthOp = new Operation();
        ctx = this.host.testCreate(1);
        clearAuthOp.setCompletion(ctx.getCompletion());
        AuthorizationCacheUtils.clearAuthzCacheForRole(s, clearAuthOp, roleState);
        clearAuthOp.complete();
        this.host.testWait(ctx);
        this.host.resetSystemAuthorizationContext();
        assertNull(assumeIdentityAndGetContext(fooUserLink, s, false));
        assertNotNull(assumeIdentityAndGetContext(fooUserLink, s, true));
        this.host.setSystemAuthorizationContext();
        clearAuthOp = new Operation();
        clearAuthOp.setUri(UriUtils.buildUri(this.host, fooUserLink));
        ctx = this.host.testCreate(1);
        clearAuthOp.setCompletion(ctx.getCompletion());
        AuthorizationCacheUtils.clearAuthzCacheForUser(s, clearAuthOp);
        clearAuthOp.complete();
        this.host.testWait(ctx);
        this.host.resetSystemAuthorizationContext();
        assertNull(assumeIdentityAndGetContext(fooUserLink, s, false));
    }
    private void verifyJaneAccess(Map<URI, ExampleServiceState> exampleServices, String authToken) throws Throwable {
        this.host.testStart(exampleServices.size());
        for (Entry<URI, ExampleServiceState> entry : exampleServices.entrySet()) {
            Operation get = Operation.createGet(entry.getKey());
            if (authToken != null) {
                get.forceRemote();
                get.getRequestHeaders().put(Operation.REQUEST_AUTH_TOKEN_HEADER, authToken);
            }
            if (entry.getValue().name.equals("jane")) {
                get.setCompletion((o, e) -> {
                    if (o.getStatusCode() != Operation.STATUS_CODE_OK) {
                        String message = String.format("Expected %d, got %s",
                                Operation.STATUS_CODE_OK,
                                o.getStatusCode());
                        this.host.failIteration(new IllegalStateException(message));
                        return;
                    }
                    ExampleServiceState body = o.getBody(ExampleServiceState.class);
                    if (!body.documentAuthPrincipalLink.equals(this.userServicePath)) {
                        String message = String.format("Expected %s, got %s",
                                this.userServicePath, body.documentAuthPrincipalLink);
                        this.host.failIteration(new IllegalStateException(message));
                        return;
                    }
                    this.host.completeIteration();
                });
            } else {
                get.setCompletion((o, e) -> {
                    if (o.getStatusCode() != Operation.STATUS_CODE_FORBIDDEN) {
                        String message = String.format("Expected %d, got %s",
                                Operation.STATUS_CODE_FORBIDDEN,
                                o.getStatusCode());
                        this.host.failIteration(new IllegalStateException(message));
                        return;
                    }
                    this.host.completeIteration();
                });
            }
            this.host.send(get);
        }
        this.host.testWait();
    }
    private void assertAuthorizedServicesInResult(String name,
            Map<URI, ExampleServiceState> exampleServices,
            ServiceDocumentQueryResult result) {
        Set<String> selfLinks = new HashSet<>(result.documentLinks);
        for (Entry<URI, ExampleServiceState> entry : exampleServices.entrySet()) {
            String selfLink = entry.getKey().getPath();
            if (entry.getValue().name.equals(name)) {
                assertTrue(selfLinks.contains(selfLink));
            } else {
                assertFalse(selfLinks.contains(selfLink));
            }
        }
    }
    private String generateAuthToken(String userServicePath) throws GeneralSecurityException {
        Claims.Builder builder = new Claims.Builder();
        builder.setSubject(userServicePath);
        Claims claims = builder.getResult();
        return this.host.getTokenSigner().sign(claims);
    }
    private ExampleServiceState createExampleServiceState(String name, Long counter) {
        ExampleServiceState state = new ExampleServiceState();
        state.name = name;
        state.counter = counter;
        state.documentAuthPrincipalLink = "stringtooverwrite";
        return state;
    }
    private Map<URI, ExampleServiceState> createExampleServices(String userName) throws Throwable {
        Collection<ExampleServiceState> bodies = new LinkedList<>();
        for (int i = 0; i < this.serviceCount; i++) {
            bodies.add(createExampleServiceState(userName, 1L));
        }
        Iterator<ExampleServiceState> it = bodies.iterator();
        Consumer<Operation> bodySetter = (o) -> {
            o.setBody(it.next());
        };
        Map<URI, ExampleServiceState> states = this.host.doFactoryChildServiceStart(
                null,
                bodies.size(),
                ExampleServiceState.class,
                bodySetter,
                UriUtils.buildFactoryUri(this.host, ExampleService.class));
        return states;
    }
}
