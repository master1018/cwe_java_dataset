
package org.apache.activemq.broker;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.transaction.xa.XAResource;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.broker.region.ConnectionStatistics;
import org.apache.activemq.broker.region.RegionBroker;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.BrokerInfo;
import org.apache.activemq.command.Command;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConnectionControl;
import org.apache.activemq.command.ConnectionError;
import org.apache.activemq.command.ConnectionId;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerControl;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.ControlCommand;
import org.apache.activemq.command.DataArrayResponse;
import org.apache.activemq.command.DestinationInfo;
import org.apache.activemq.command.ExceptionResponse;
import org.apache.activemq.command.FlushCommand;
import org.apache.activemq.command.IntegerResponse;
import org.apache.activemq.command.KeepAliveInfo;
import org.apache.activemq.command.Message;
import org.apache.activemq.command.MessageAck;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.MessageDispatchNotification;
import org.apache.activemq.command.MessagePull;
import org.apache.activemq.command.ProducerAck;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.command.ProducerInfo;
import org.apache.activemq.command.RemoveSubscriptionInfo;
import org.apache.activemq.command.Response;
import org.apache.activemq.command.SessionId;
import org.apache.activemq.command.SessionInfo;
import org.apache.activemq.command.ShutdownInfo;
import org.apache.activemq.command.TransactionId;
import org.apache.activemq.command.TransactionInfo;
import org.apache.activemq.command.WireFormatInfo;
import org.apache.activemq.network.DemandForwardingBridge;
import org.apache.activemq.network.MBeanNetworkListener;
import org.apache.activemq.network.NetworkBridgeConfiguration;
import org.apache.activemq.network.NetworkBridgeFactory;
import org.apache.activemq.security.MessageAuthorizationPolicy;
import org.apache.activemq.state.CommandVisitor;
import org.apache.activemq.state.ConnectionState;
import org.apache.activemq.state.ConsumerState;
import org.apache.activemq.state.ProducerState;
import org.apache.activemq.state.SessionState;
import org.apache.activemq.state.TransactionState;
import org.apache.activemq.thread.Task;
import org.apache.activemq.thread.TaskRunner;
import org.apache.activemq.thread.TaskRunnerFactory;
import org.apache.activemq.transaction.Transaction;
import org.apache.activemq.transport.DefaultTransportListener;
import org.apache.activemq.transport.ResponseCorrelator;
import org.apache.activemq.transport.TransmitCallback;
import org.apache.activemq.transport.Transport;
import org.apache.activemq.transport.TransportDisposedIOException;
import org.apache.activemq.util.IntrospectionSupport;
import org.apache.activemq.util.MarshallingSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
public class TransportConnection implements Connection, Task, CommandVisitor {
    private static final Logger LOG = LoggerFactory.getLogger(TransportConnection.class);
    private static final Logger TRANSPORTLOG = LoggerFactory.getLogger(TransportConnection.class.getName() + ".Transport");
    private static final Logger SERVICELOG = LoggerFactory.getLogger(TransportConnection.class.getName() + ".Service");
    protected final Broker broker;
    protected final TransportConnector connector;
    protected final Map<ConnectionId, ConnectionState> brokerConnectionStates;
    protected BrokerInfo brokerInfo;
    protected final List<Command> dispatchQueue = new LinkedList<Command>();
    protected TaskRunner taskRunner;
    protected final AtomicReference<IOException> transportException = new AtomicReference<IOException>();
    protected AtomicBoolean dispatchStopped = new AtomicBoolean(false);
    private final Transport transport;
    private MessageAuthorizationPolicy messageAuthorizationPolicy;
    private WireFormatInfo wireFormatInfo;
    private boolean inServiceException;
    private final ConnectionStatistics statistics = new ConnectionStatistics();
    private boolean manageable;
    private boolean slow;
    private boolean markedCandidate;
    private boolean blockedCandidate;
    private boolean blocked;
    private boolean connected;
    private boolean active;
    private boolean starting;
    private boolean pendingStop;
    private long timeStamp;
    private final AtomicBoolean stopping = new AtomicBoolean(false);
    private final CountDownLatch stopped = new CountDownLatch(1);
    private final AtomicBoolean asyncException = new AtomicBoolean(false);
    private final Map<ProducerId, ProducerBrokerExchange> producerExchanges = new HashMap<ProducerId, ProducerBrokerExchange>();
    private final Map<ConsumerId, ConsumerBrokerExchange> consumerExchanges = new HashMap<ConsumerId, ConsumerBrokerExchange>();
    private final CountDownLatch dispatchStoppedLatch = new CountDownLatch(1);
    private ConnectionContext context;
    private boolean networkConnection;
    private boolean faultTolerantConnection;
    private final AtomicInteger protocolVersion = new AtomicInteger(CommandTypes.PROTOCOL_VERSION);
    private DemandForwardingBridge duplexBridge;
    private final TaskRunnerFactory taskRunnerFactory;
    private final TaskRunnerFactory stopTaskRunnerFactory;
    private TransportConnectionStateRegister connectionStateRegister = new SingleTransportConnectionStateRegister();
    private final ReentrantReadWriteLock serviceLock = new ReentrantReadWriteLock();
    private String duplexNetworkConnectorId;
    private Throwable stopError = null;
    public TransportConnection(TransportConnector connector, final Transport transport, Broker broker,
                               TaskRunnerFactory taskRunnerFactory, TaskRunnerFactory stopTaskRunnerFactory) {
        this.connector = connector;
        this.broker = broker;
        RegionBroker rb = (RegionBroker) broker.getAdaptor(RegionBroker.class);
        brokerConnectionStates = rb.getConnectionStates();
        if (connector != null) {
            this.statistics.setParent(connector.getStatistics());
            this.messageAuthorizationPolicy = connector.getMessageAuthorizationPolicy();
        }
        this.taskRunnerFactory = taskRunnerFactory;
        this.stopTaskRunnerFactory = stopTaskRunnerFactory;
        this.transport = transport;
        final BrokerService brokerService = this.broker.getBrokerService();
        if( this.transport instanceof BrokerServiceAware ) {
            ((BrokerServiceAware)this.transport).setBrokerService(brokerService);
        }
        this.transport.setTransportListener(new DefaultTransportListener() {
            @Override
            public void onCommand(Object o) {
                serviceLock.readLock().lock();
                try {
                    if (!(o instanceof Command)) {
                        throw new RuntimeException("Protocol violation - Command corrupted: " + o.toString());
                    }
                    Command command = (Command) o;
                    if (!brokerService.isStopping()) {
                        Response response = service(command);
                        if (response != null && !brokerService.isStopping()) {
                            dispatchSync(response);
                        }
                    } else {
                        throw new BrokerStoppedException("Broker " + brokerService + " is being stopped");
                    }
                } finally {
                    serviceLock.readLock().unlock();
                }
            }
            @Override
            public void onException(IOException exception) {
                serviceLock.readLock().lock();
                try {
                    serviceTransportException(exception);
                } finally {
                    serviceLock.readLock().unlock();
                }
            }
        });
        connected = true;
    }
    @Override
    public int getDispatchQueueSize() {
        synchronized (dispatchQueue) {
            return dispatchQueue.size();
        }
    }
    public void serviceTransportException(IOException e) {
        BrokerService bService = connector.getBrokerService();
        if (bService.isShutdownOnSlaveFailure()) {
            if (brokerInfo != null) {
                if (brokerInfo.isSlaveBroker()) {
                    LOG.error("Slave has exception: {} shutting down master now.", e.getMessage(), e);
                    try {
                        doStop();
                        bService.stop();
                    } catch (Exception ex) {
                        LOG.warn("Failed to stop the master", ex);
                    }
                }
            }
        }
        if (!stopping.get() && !pendingStop) {
            transportException.set(e);
            if (TRANSPORTLOG.isDebugEnabled()) {
                TRANSPORTLOG.debug(this + " failed: " + e, e);
            } else if (TRANSPORTLOG.isWarnEnabled() && !expected(e)) {
                TRANSPORTLOG.warn(this + " failed: " + e);
            }
            stopAsync();
        }
    }
    private boolean expected(IOException e) {
        return isStomp() && ((e instanceof SocketException && e.getMessage().indexOf("reset") != -1) || e instanceof EOFException);
    }
    private boolean isStomp() {
        URI uri = connector.getUri();
        return uri != null && uri.getScheme() != null && uri.getScheme().indexOf("stomp") != -1;
    }
    @Override
    public void serviceExceptionAsync(final IOException e) {
        if (asyncException.compareAndSet(false, true)) {
            new Thread("Async Exception Handler") {
                @Override
                public void run() {
                    serviceException(e);
                }
            }.start();
        }
    }
    @Override
    public void serviceException(Throwable e) {
        if (e instanceof IOException) {
            serviceTransportException((IOException) e);
        } else if (e.getClass() == BrokerStoppedException.class) {
            if (!stopping.get()) {
                SERVICELOG.debug("Broker has been stopped.  Notifying client and closing his connection.");
                ConnectionError ce = new ConnectionError();
                ce.setException(e);
                dispatchSync(ce);
                this.stopError = e;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                stopAsync();
            }
        } else if (!stopping.get() && !inServiceException) {
            inServiceException = true;
            try {
                SERVICELOG.warn("Async error occurred: ", e);
                ConnectionError ce = new ConnectionError();
                ce.setException(e);
                if (pendingStop) {
                    dispatchSync(ce);
                } else {
                    dispatchAsync(ce);
                }
            } finally {
                inServiceException = false;
            }
        }
    }
    @Override
    public Response service(Command command) {
        MDC.put("activemq.connector", connector.getUri().toString());
        Response response = null;
        boolean responseRequired = command.isResponseRequired();
        int commandId = command.getCommandId();
        try {
            if (!pendingStop) {
                response = command.visit(this);
            } else {
                response = new ExceptionResponse(this.stopError);
            }
        } catch (Throwable e) {
            if (SERVICELOG.isDebugEnabled() && e.getClass() != BrokerStoppedException.class) {
                SERVICELOG.debug("Error occured while processing " + (responseRequired ? "sync" : "async")
                        + " command: " + command + ", exception: " + e, e);
            }
            if (e instanceof SuppressReplyException || (e.getCause() instanceof SuppressReplyException)) {
                LOG.info("Suppressing reply to: " + command + " on: " + e + ", cause: " + e.getCause());
                responseRequired = false;
            }
            if (responseRequired) {
                if (e instanceof SecurityException || e.getCause() instanceof SecurityException) {
                    SERVICELOG.warn("Security Error occurred: {}", e.getMessage());
                }
                response = new ExceptionResponse(e);
            } else {
                serviceException(e);
            }
        }
        if (responseRequired) {
            if (response == null) {
                response = new Response();
            }
            response.setCorrelationId(commandId);
        }
        if (context != null) {
            if (context.isDontSendReponse()) {
                context.setDontSendReponse(false);
                response = null;
            }
            context = null;
        }
        MDC.remove("activemq.connector");
        return response;
    }
    @Override
    public Response processKeepAlive(KeepAliveInfo info) throws Exception {
        return null;
    }
    @Override
    public Response processRemoveSubscription(RemoveSubscriptionInfo info) throws Exception {
        broker.removeSubscription(lookupConnectionState(info.getConnectionId()).getContext(), info);
        return null;
    }
    @Override
    public Response processWireFormat(WireFormatInfo info) throws Exception {
        wireFormatInfo = info;
        protocolVersion.set(info.getVersion());
        return null;
    }
    @Override
    public Response processShutdown(ShutdownInfo info) throws Exception {
        stopAsync();
        return null;
    }
    @Override
    public Response processFlush(FlushCommand command) throws Exception {
        return null;
    }
    @Override
    public Response processBeginTransaction(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = null;
        if (cs != null) {
            context = cs.getContext();
        }
        if (cs == null) {
            throw new NullPointerException("Context is null");
        }
        if (cs.getTransactionState(info.getTransactionId()) == null) {
            cs.addTransactionState(info.getTransactionId());
            broker.beginTransaction(context, info.getTransactionId());
        }
        return null;
    }
    @Override
    public int getActiveTransactionCount() {
        int rc = 0;
        for (TransportConnectionState cs : connectionStateRegister.listConnectionStates()) {
            Collection<TransactionState> transactions = cs.getTransactionStates();
            for (TransactionState transaction : transactions) {
                rc++;
            }
        }
        return rc;
    }
    @Override
    public Long getOldestActiveTransactionDuration() {
        TransactionState oldestTX = null;
        for (TransportConnectionState cs : connectionStateRegister.listConnectionStates()) {
            Collection<TransactionState> transactions = cs.getTransactionStates();
            for (TransactionState transaction : transactions) {
                if( oldestTX ==null || oldestTX.getCreatedAt() < transaction.getCreatedAt() ) {
                    oldestTX = transaction;
                }
            }
        }
        if( oldestTX == null ) {
            return null;
        }
        return System.currentTimeMillis() - oldestTX.getCreatedAt();
    }
    @Override
    public Response processEndTransaction(TransactionInfo info) throws Exception {
        return null;
    }
    @Override
    public Response processPrepareTransaction(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = null;
        if (cs != null) {
            context = cs.getContext();
        }
        if (cs == null) {
            throw new NullPointerException("Context is null");
        }
        TransactionState transactionState = cs.getTransactionState(info.getTransactionId());
        if (transactionState == null) {
            throw new IllegalStateException("Cannot prepare a transaction that had not been started or previously returned XA_RDONLY: "
                    + info.getTransactionId());
        }
        if (!transactionState.isPrepared()) {
            transactionState.setPrepared(true);
            int result = broker.prepareTransaction(context, info.getTransactionId());
            transactionState.setPreparedResult(result);
            if (result == XAResource.XA_RDONLY) {
                cs.removeTransactionState(info.getTransactionId());
            }
            IntegerResponse response = new IntegerResponse(result);
            return response;
        } else {
            IntegerResponse response = new IntegerResponse(transactionState.getPreparedResult());
            return response;
        }
    }
    @Override
    public Response processCommitTransactionOnePhase(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = cs.getContext();
        cs.removeTransactionState(info.getTransactionId());
        broker.commitTransaction(context, info.getTransactionId(), true);
        return null;
    }
    @Override
    public Response processCommitTransactionTwoPhase(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = cs.getContext();
        cs.removeTransactionState(info.getTransactionId());
        broker.commitTransaction(context, info.getTransactionId(), false);
        return null;
    }
    @Override
    public Response processRollbackTransaction(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = cs.getContext();
        cs.removeTransactionState(info.getTransactionId());
        broker.rollbackTransaction(context, info.getTransactionId());
        return null;
    }
    @Override
    public Response processForgetTransaction(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = cs.getContext();
        broker.forgetTransaction(context, info.getTransactionId());
        return null;
    }
    @Override
    public Response processRecoverTransactions(TransactionInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        context = cs.getContext();
        TransactionId[] preparedTransactions = broker.getPreparedTransactions(context);
        return new DataArrayResponse(preparedTransactions);
    }
    @Override
    public Response processMessage(Message messageSend) throws Exception {
        ProducerId producerId = messageSend.getProducerId();
        ProducerBrokerExchange producerExchange = getProducerBrokerExchange(producerId);
        if (producerExchange.canDispatch(messageSend)) {
            broker.send(producerExchange, messageSend);
        }
        return null;
    }
    @Override
    public Response processMessageAck(MessageAck ack) throws Exception {
        ConsumerBrokerExchange consumerExchange = getConsumerBrokerExchange(ack.getConsumerId());
        if (consumerExchange != null) {
            broker.acknowledge(consumerExchange, ack);
        } else if (ack.isInTransaction()) {
            LOG.warn("no matching consumer, ignoring ack {}", consumerExchange, ack);
        }
        return null;
    }
    @Override
    public Response processMessagePull(MessagePull pull) throws Exception {
        return broker.messagePull(lookupConnectionState(pull.getConsumerId()).getContext(), pull);
    }
    @Override
    public Response processMessageDispatchNotification(MessageDispatchNotification notification) throws Exception {
        broker.processDispatchNotification(notification);
        return null;
    }
    @Override
    public Response processAddDestination(DestinationInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        broker.addDestinationInfo(cs.getContext(), info);
        if (info.getDestination().isTemporary()) {
            cs.addTempDestination(info);
        }
        return null;
    }
    @Override
    public Response processRemoveDestination(DestinationInfo info) throws Exception {
        TransportConnectionState cs = lookupConnectionState(info.getConnectionId());
        broker.removeDestinationInfo(cs.getContext(), info);
        if (info.getDestination().isTemporary()) {
            cs.removeTempDestination(info.getDestination());
        }
        return null;
    }
    @Override
    public Response processAddProducer(ProducerInfo info) throws Exception {
        SessionId sessionId = info.getProducerId().getParentId();
        ConnectionId connectionId = sessionId.getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs == null) {
            throw new IllegalStateException("Cannot add a producer to a connection that had not been registered: "
                    + connectionId);
        }
        SessionState ss = cs.getSessionState(sessionId);
        if (ss == null) {
            throw new IllegalStateException("Cannot add a producer to a session that had not been registered: "
                    + sessionId);
        }
        if (!ss.getProducerIds().contains(info.getProducerId())) {
            ActiveMQDestination destination = info.getDestination();
            if (destination != null && !AdvisorySupport.isAdvisoryTopic(destination)) {
                if (getProducerCount(connectionId) >= connector.getMaximumProducersAllowedPerConnection()){
                    throw new IllegalStateException("Can't add producer on connection " + connectionId + ": at maximum limit: " + connector.getMaximumProducersAllowedPerConnection());
                }
            }
            broker.addProducer(cs.getContext(), info);
            try {
                ss.addProducer(info);
            } catch (IllegalStateException e) {
                broker.removeProducer(cs.getContext(), info);
            }
        }
        return null;
    }
    @Override
    public Response processRemoveProducer(ProducerId id) throws Exception {
        SessionId sessionId = id.getParentId();
        ConnectionId connectionId = sessionId.getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        SessionState ss = cs.getSessionState(sessionId);
        if (ss == null) {
            throw new IllegalStateException("Cannot remove a producer from a session that had not been registered: "
                    + sessionId);
        }
        ProducerState ps = ss.removeProducer(id);
        if (ps == null) {
            throw new IllegalStateException("Cannot remove a producer that had not been registered: " + id);
        }
        removeProducerBrokerExchange(id);
        broker.removeProducer(cs.getContext(), ps.getInfo());
        return null;
    }
    @Override
    public Response processAddConsumer(ConsumerInfo info) throws Exception {
        SessionId sessionId = info.getConsumerId().getParentId();
        ConnectionId connectionId = sessionId.getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs == null) {
            throw new IllegalStateException("Cannot add a consumer to a connection that had not been registered: "
                    + connectionId);
        }
        SessionState ss = cs.getSessionState(sessionId);
        if (ss == null) {
            throw new IllegalStateException(broker.getBrokerName()
                    + " Cannot add a consumer to a session that had not been registered: " + sessionId);
        }
        if (!ss.getConsumerIds().contains(info.getConsumerId())) {
            ActiveMQDestination destination = info.getDestination();
            if (destination != null && !AdvisorySupport.isAdvisoryTopic(destination)) {
                if (getConsumerCount(connectionId) >= connector.getMaximumConsumersAllowedPerConnection()){
                    throw new IllegalStateException("Can't add consumer on connection " + connectionId + ": at maximum limit: " + connector.getMaximumConsumersAllowedPerConnection());
                }
            }
            broker.addConsumer(cs.getContext(), info);
            try {
                ss.addConsumer(info);
                addConsumerBrokerExchange(info.getConsumerId());
            } catch (IllegalStateException e) {
                broker.removeConsumer(cs.getContext(), info);
            }
        }
        return null;
    }
    @Override
    public Response processRemoveConsumer(ConsumerId id, long lastDeliveredSequenceId) throws Exception {
        SessionId sessionId = id.getParentId();
        ConnectionId connectionId = sessionId.getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs == null) {
            throw new IllegalStateException("Cannot remove a consumer from a connection that had not been registered: "
                    + connectionId);
        }
        SessionState ss = cs.getSessionState(sessionId);
        if (ss == null) {
            throw new IllegalStateException("Cannot remove a consumer from a session that had not been registered: "
                    + sessionId);
        }
        ConsumerState consumerState = ss.removeConsumer(id);
        if (consumerState == null) {
            throw new IllegalStateException("Cannot remove a consumer that had not been registered: " + id);
        }
        ConsumerInfo info = consumerState.getInfo();
        info.setLastDeliveredSequenceId(lastDeliveredSequenceId);
        broker.removeConsumer(cs.getContext(), consumerState.getInfo());
        removeConsumerBrokerExchange(id);
        return null;
    }
    @Override
    public Response processAddSession(SessionInfo info) throws Exception {
        ConnectionId connectionId = info.getSessionId().getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs != null && !cs.getSessionIds().contains(info.getSessionId())) {
            broker.addSession(cs.getContext(), info);
            try {
                cs.addSession(info);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                broker.removeSession(cs.getContext(), info);
            }
        }
        return null;
    }
    @Override
    public Response processRemoveSession(SessionId id, long lastDeliveredSequenceId) throws Exception {
        ConnectionId connectionId = id.getParentId();
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs == null) {
            throw new IllegalStateException("Cannot remove session from connection that had not been registered: " + connectionId);
        }
        SessionState session = cs.getSessionState(id);
        if (session == null) {
            throw new IllegalStateException("Cannot remove session that had not been registered: " + id);
        }
        session.shutdown();
        for (ConsumerId consumerId : session.getConsumerIds()) {
            try {
                processRemoveConsumer(consumerId, lastDeliveredSequenceId);
            } catch (Throwable e) {
                LOG.warn("Failed to remove consumer: {}", consumerId, e);
            }
        }
        for (ProducerId producerId : session.getProducerIds()) {
            try {
                processRemoveProducer(producerId);
            } catch (Throwable e) {
                LOG.warn("Failed to remove producer: {}", producerId, e);
            }
        }
        cs.removeSession(id);
        broker.removeSession(cs.getContext(), session.getInfo());
        return null;
    }
    @Override
    public Response processAddConnection(ConnectionInfo info) throws Exception {
        if (wireFormatInfo != null && wireFormatInfo.getVersion() <= 2) {
            info.setClientMaster(true);
        }
        TransportConnectionState state;
        synchronized (brokerConnectionStates) {
            state = (TransportConnectionState) brokerConnectionStates.get(info.getConnectionId());
            if (state == null) {
                state = new TransportConnectionState(info, this);
                brokerConnectionStates.put(info.getConnectionId(), state);
            }
            state.incrementReference();
        }
        synchronized (state.getConnectionMutex()) {
            if (state.getConnection() != this) {
                LOG.debug("Killing previous stale connection: {}", state.getConnection().getRemoteAddress());
                state.getConnection().stop();
                LOG.debug("Connection {} taking over previous connection: {}", getRemoteAddress(), state.getConnection().getRemoteAddress());
                state.setConnection(this);
                state.reset(info);
            }
        }
        registerConnectionState(info.getConnectionId(), state);
        LOG.debug("Setting up new connection id: {}, address: {}, info: {}", new Object[]{ info.getConnectionId(), getRemoteAddress(), info });
        this.faultTolerantConnection = info.isFaultTolerant();
        String clientId = info.getClientId();
        context = new ConnectionContext();
        context.setBroker(broker);
        context.setClientId(clientId);
        context.setClientMaster(info.isClientMaster());
        context.setConnection(this);
        context.setConnectionId(info.getConnectionId());
        context.setConnector(connector);
        context.setMessageAuthorizationPolicy(getMessageAuthorizationPolicy());
        context.setNetworkConnection(networkConnection);
        context.setFaultTolerant(faultTolerantConnection);
        context.setTransactions(new ConcurrentHashMap<TransactionId, Transaction>());
        context.setUserName(info.getUserName());
        context.setWireFormatInfo(wireFormatInfo);
        context.setReconnect(info.isFailoverReconnect());
        this.manageable = info.isManageable();
        context.setConnectionState(state);
        state.setContext(context);
        state.setConnection(this);
        if (info.getClientIp() == null) {
            info.setClientIp(getRemoteAddress());
        }
        try {
            broker.addConnection(context, info);
        } catch (Exception e) {
            synchronized (brokerConnectionStates) {
                brokerConnectionStates.remove(info.getConnectionId());
            }
            unregisterConnectionState(info.getConnectionId());
            LOG.warn("Failed to add Connection {}", info.getConnectionId(), e);
            if (e instanceof SecurityException) {
                delayedStop(2000, "Failed with SecurityException: " + e.getLocalizedMessage(), e);
            }
            throw e;
        }
        if (info.isManageable()) {
            ConnectionControl command = this.connector.getConnectionControl();
            command.setFaultTolerant(broker.isFaultTolerantConfiguration());
            if (info.isFailoverReconnect()) {
                command.setRebalanceConnection(false);
            }
            dispatchAsync(command);
        }
        return null;
    }
    @Override
    public synchronized Response processRemoveConnection(ConnectionId id, long lastDeliveredSequenceId)
            throws InterruptedException {
        LOG.debug("remove connection id: {}", id);
        TransportConnectionState cs = lookupConnectionState(id);
        if (cs != null) {
            cs.shutdown();
            for (SessionId sessionId : cs.getSessionIds()) {
                try {
                    processRemoveSession(sessionId, lastDeliveredSequenceId);
                } catch (Throwable e) {
                    SERVICELOG.warn("Failed to remove session {}", sessionId, e);
                }
            }
            for (Iterator<DestinationInfo> iter = cs.getTempDestinations().iterator(); iter.hasNext(); ) {
                DestinationInfo di = iter.next();
                try {
                    broker.removeDestination(cs.getContext(), di.getDestination(), 0);
                } catch (Throwable e) {
                    SERVICELOG.warn("Failed to remove tmp destination {}", di.getDestination(), e);
                }
                iter.remove();
            }
            try {
                broker.removeConnection(cs.getContext(), cs.getInfo(), null);
            } catch (Throwable e) {
                SERVICELOG.warn("Failed to remove connection {}", cs.getInfo(), e);
            }
            TransportConnectionState state = unregisterConnectionState(id);
            if (state != null) {
                synchronized (brokerConnectionStates) {
                    if (state.decrementReference() == 0) {
                        brokerConnectionStates.remove(id);
                    }
                }
            }
        }
        return null;
    }
    @Override
    public Response processProducerAck(ProducerAck ack) throws Exception {
        return null;
    }
    @Override
    public Connector getConnector() {
        return connector;
    }
    @Override
    public void dispatchSync(Command message) {
        try {
            processDispatch(message);
        } catch (IOException e) {
            serviceExceptionAsync(e);
        }
    }
    @Override
    public void dispatchAsync(Command message) {
        if (!stopping.get()) {
            if (taskRunner == null) {
                dispatchSync(message);
            } else {
                synchronized (dispatchQueue) {
                    dispatchQueue.add(message);
                }
                try {
                    taskRunner.wakeup();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            if (message.isMessageDispatch()) {
                MessageDispatch md = (MessageDispatch) message;
                TransmitCallback sub = md.getTransmitCallback();
                broker.postProcessDispatch(md);
                if (sub != null) {
                    sub.onFailure();
                }
            }
        }
    }
    protected void processDispatch(Command command) throws IOException {
        MessageDispatch messageDispatch = (MessageDispatch) (command.isMessageDispatch() ? command : null);
        try {
            if (!stopping.get()) {
                if (messageDispatch != null) {
                    broker.preProcessDispatch(messageDispatch);
                }
                dispatch(command);
            }
        } catch (IOException e) {
            if (messageDispatch != null) {
                TransmitCallback sub = messageDispatch.getTransmitCallback();
                broker.postProcessDispatch(messageDispatch);
                if (sub != null) {
                    sub.onFailure();
                }
                messageDispatch = null;
                throw e;
            }
        } finally {
            if (messageDispatch != null) {
                TransmitCallback sub = messageDispatch.getTransmitCallback();
                broker.postProcessDispatch(messageDispatch);
                if (sub != null) {
                    sub.onSuccess();
                }
            }
        }
    }
    @Override
    public boolean iterate() {
        try {
            if (pendingStop || stopping.get()) {
                if (dispatchStopped.compareAndSet(false, true)) {
                    if (transportException.get() == null) {
                        try {
                            dispatch(new ShutdownInfo());
                        } catch (Throwable ignore) {
                        }
                    }
                    dispatchStoppedLatch.countDown();
                }
                return false;
            }
            if (!dispatchStopped.get()) {
                Command command = null;
                synchronized (dispatchQueue) {
                    if (dispatchQueue.isEmpty()) {
                        return false;
                    }
                    command = dispatchQueue.remove(0);
                }
                processDispatch(command);
                return true;
            }
            return false;
        } catch (IOException e) {
            if (dispatchStopped.compareAndSet(false, true)) {
                dispatchStoppedLatch.countDown();
            }
            serviceExceptionAsync(e);
            return false;
        }
    }
    @Override
    public ConnectionStatistics getStatistics() {
        return statistics;
    }
    public MessageAuthorizationPolicy getMessageAuthorizationPolicy() {
        return messageAuthorizationPolicy;
    }
    public void setMessageAuthorizationPolicy(MessageAuthorizationPolicy messageAuthorizationPolicy) {
        this.messageAuthorizationPolicy = messageAuthorizationPolicy;
    }
    @Override
    public boolean isManageable() {
        return manageable;
    }
    @Override
    public void start() throws Exception {
        try {
            synchronized (this) {
                starting = true;
                if (taskRunnerFactory != null) {
                    taskRunner = taskRunnerFactory.createTaskRunner(this, "ActiveMQ Connection Dispatcher: "
                            + getRemoteAddress());
                } else {
                    taskRunner = null;
                }
                transport.start();
                active = true;
                BrokerInfo info = connector.getBrokerInfo().copy();
                if (connector.isUpdateClusterClients()) {
                    info.setPeerBrokerInfos(this.broker.getPeerBrokerInfos());
                } else {
                    info.setPeerBrokerInfos(null);
                }
                dispatchAsync(info);
                connector.onStarted(this);
            }
        } catch (Exception e) {
            pendingStop = true;
            throw e;
        } finally {
            setStarting(false);
            if (isPendingStop()) {
                LOG.debug("Calling the delayed stop() after start() {}", this);
                stop();
            }
        }
    }
    @Override
    public void stop() throws Exception {
        stopAsync();
        while (!stopped.await(5, TimeUnit.SECONDS)) {
            LOG.info("The connection to '{}' is taking a long time to shutdown.", transport.getRemoteAddress());
        }
    }
    public void delayedStop(final int waitTime, final String reason, Throwable cause) {
        if (waitTime > 0) {
            synchronized (this) {
                pendingStop = true;
                stopError = cause;
            }
            try {
                stopTaskRunnerFactory.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(waitTime);
                            stopAsync();
                            LOG.info("Stopping {} because {}", transport.getRemoteAddress(), reason);
                        } catch (InterruptedException e) {
                        }
                    }
                });
            } catch (Throwable t) {
                LOG.warn("Cannot create stopAsync. This exception will be ignored.", t);
            }
        }
    }
    public void stopAsync() {
        synchronized (this) {
            pendingStop = true;
            if (starting) {
                LOG.debug("stopAsync() called in the middle of start(). Delaying till start completes..");
                return;
            }
        }
        if (stopping.compareAndSet(false, true)) {
            List<TransportConnectionState> connectionStates = listConnectionStates();
            for (TransportConnectionState cs : connectionStates) {
                ConnectionContext connectionContext = cs.getContext();
                if (connectionContext != null) {
                    connectionContext.getStopping().set(true);
                }
            }
            try {
                stopTaskRunnerFactory.execute(new Runnable() {
                    @Override
                    public void run() {
                        serviceLock.writeLock().lock();
                        try {
                            doStop();
                        } catch (Throwable e) {
                            LOG.debug("Error occurred while shutting down a connection {}", this, e);
                        } finally {
                            stopped.countDown();
                            serviceLock.writeLock().unlock();
                        }
                    }
                });
            } catch (Throwable t) {
                LOG.warn("Cannot create async transport stopper thread. This exception is ignored. Not waiting for stop to complete", t);
                stopped.countDown();
            }
        }
    }
    @Override
    public String toString() {
        return "Transport Connection to: " + transport.getRemoteAddress();
    }
    protected void doStop() throws Exception {
        LOG.debug("Stopping connection: {}", transport.getRemoteAddress());
        connector.onStopped(this);
        try {
            synchronized (this) {
                if (duplexBridge != null) {
                    duplexBridge.stop();
                }
            }
        } catch (Exception ignore) {
            LOG.trace("Exception caught stopping. This exception is ignored.", ignore);
        }
        try {
            transport.stop();
            LOG.debug("Stopped transport: {}", transport.getRemoteAddress());
        } catch (Exception e) {
            LOG.debug("Could not stop transport to {}. This exception is ignored.", transport.getRemoteAddress(), e);
        }
        if (taskRunner != null) {
            taskRunner.shutdown(1);
            taskRunner = null;
        }
        active = false;
        synchronized (dispatchQueue) {
            for (Iterator<Command> iter = dispatchQueue.iterator(); iter.hasNext(); ) {
                Command command = iter.next();
                if (command.isMessageDispatch()) {
                    MessageDispatch md = (MessageDispatch) command;
                    TransmitCallback sub = md.getTransmitCallback();
                    broker.postProcessDispatch(md);
                    if (sub != null) {
                        sub.onFailure();
                    }
                }
            }
            dispatchQueue.clear();
        }
        if (!broker.isStopped()) {
            List<TransportConnectionState> connectionStates = listConnectionStates();
            connectionStates = listConnectionStates();
            for (TransportConnectionState cs : connectionStates) {
                cs.getContext().getStopping().set(true);
                try {
                    LOG.debug("Cleaning up connection resources: {}", getRemoteAddress());
                    processRemoveConnection(cs.getInfo().getConnectionId(), 0l);
                } catch (Throwable ignore) {
                    ignore.printStackTrace();
                }
            }
        }
        LOG.debug("Connection Stopped: {}", getRemoteAddress());
    }
    public boolean isBlockedCandidate() {
        return blockedCandidate;
    }
    public void setBlockedCandidate(boolean blockedCandidate) {
        this.blockedCandidate = blockedCandidate;
    }
    public boolean isMarkedCandidate() {
        return markedCandidate;
    }
    public void setMarkedCandidate(boolean markedCandidate) {
        this.markedCandidate = markedCandidate;
        if (!markedCandidate) {
            timeStamp = 0;
            blockedCandidate = false;
        }
    }
    public void setSlow(boolean slow) {
        this.slow = slow;
    }
    @Override
    public boolean isSlow() {
        return slow;
    }
    public boolean isMarkedBlockedCandidate() {
        return markedCandidate;
    }
    public void doMark() {
        if (timeStamp == 0) {
            timeStamp = System.currentTimeMillis();
        }
    }
    @Override
    public boolean isBlocked() {
        return blocked;
    }
    @Override
    public boolean isConnected() {
        return connected;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    @Override
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public synchronized boolean isStarting() {
        return starting;
    }
    @Override
    public synchronized boolean isNetworkConnection() {
        return networkConnection;
    }
    @Override
    public boolean isFaultTolerantConnection() {
        return this.faultTolerantConnection;
    }
    protected synchronized void setStarting(boolean starting) {
        this.starting = starting;
    }
    public synchronized boolean isPendingStop() {
        return pendingStop;
    }
    protected synchronized void setPendingStop(boolean pendingStop) {
        this.pendingStop = pendingStop;
    }
    @Override
    public Response processBrokerInfo(BrokerInfo info) {
        if (info.isSlaveBroker()) {
            LOG.error(" Slave Brokers are no longer supported - slave trying to attach is: {}", info.getBrokerName());
        } else if (info.isNetworkConnection() && info.isDuplexConnection()) {
            try {
                Properties properties = MarshallingSupport.stringToProperties(info.getNetworkProperties());
                Map<String, String> props = createMap(properties);
                NetworkBridgeConfiguration config = new NetworkBridgeConfiguration();
                IntrospectionSupport.setProperties(config, props, "");
                config.setBrokerName(broker.getBrokerName());
                String duplexNetworkConnectorId = config.getName() + "@" + info.getBrokerId();
                CopyOnWriteArrayList<TransportConnection> connections = this.connector.getConnections();
                synchronized (connections) {
                    for (Iterator<TransportConnection> iter = connections.iterator(); iter.hasNext(); ) {
                        TransportConnection c = iter.next();
                        if ((c != this) && (duplexNetworkConnectorId.equals(c.getDuplexNetworkConnectorId()))) {
                            LOG.warn("Stopping an existing active duplex connection [{}] for network connector ({}).", c, duplexNetworkConnectorId);
                            c.stopAsync();
                            c.getStopped().await(1, TimeUnit.SECONDS);
                        }
                    }
                    setDuplexNetworkConnectorId(duplexNetworkConnectorId);
                }
                Transport localTransport = NetworkBridgeFactory.createLocalTransport(broker);
                Transport remoteBridgeTransport = transport;
                if (! (remoteBridgeTransport instanceof ResponseCorrelator)) {
                    remoteBridgeTransport = new ResponseCorrelator(remoteBridgeTransport);
                }
                String duplexName = localTransport.toString();
                if (duplexName.contains("#")) {
                    duplexName = duplexName.substring(duplexName.lastIndexOf("#"));
                }
                MBeanNetworkListener listener = new MBeanNetworkListener(broker.getBrokerService(), config, broker.getBrokerService().createDuplexNetworkConnectorObjectName(duplexName));
                listener.setCreatedByDuplex(true);
                duplexBridge = NetworkBridgeFactory.createBridge(config, localTransport, remoteBridgeTransport, listener);
                duplexBridge.setBrokerService(broker.getBrokerService());
                info.setDuplexConnection(false);
                duplexBridge.setCreatedByDuplex(true);
                duplexBridge.duplexStart(this, brokerInfo, info);
                LOG.info("Started responder end of duplex bridge {}", duplexNetworkConnectorId);
                return null;
            } catch (TransportDisposedIOException e) {
                LOG.warn("Duplex bridge {} was stopped before it was correctly started.", duplexNetworkConnectorId);
                return null;
            } catch (Exception e) {
                LOG.error("Failed to create responder end of duplex network bridge {}", duplexNetworkConnectorId, e);
                return null;
            }
        }
        if (this.brokerInfo != null) {
            LOG.warn("Unexpected extra broker info command received: {}", info);
        }
        this.brokerInfo = info;
        networkConnection = true;
        List<TransportConnectionState> connectionStates = listConnectionStates();
        for (TransportConnectionState cs : connectionStates) {
            cs.getContext().setNetworkConnection(true);
        }
        return null;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private HashMap<String, String> createMap(Properties properties) {
        return new HashMap(properties);
    }
    protected void dispatch(Command command) throws IOException {
        try {
            setMarkedCandidate(true);
            transport.oneway(command);
        } finally {
            setMarkedCandidate(false);
        }
    }
    @Override
    public String getRemoteAddress() {
        return transport.getRemoteAddress();
    }
    public Transport getTransport() {
        return transport;
    }
    @Override
    public String getConnectionId() {
        List<TransportConnectionState> connectionStates = listConnectionStates();
        for (TransportConnectionState cs : connectionStates) {
            if (cs.getInfo().getClientId() != null) {
                return cs.getInfo().getClientId();
            }
            return cs.getInfo().getConnectionId().toString();
        }
        return null;
    }
    @Override
    public void updateClient(ConnectionControl control) {
        if (isActive() && isBlocked() == false && isFaultTolerantConnection() && this.wireFormatInfo != null
                && this.wireFormatInfo.getVersion() >= 6) {
            dispatchAsync(control);
        }
    }
    public ProducerBrokerExchange getProducerBrokerExchangeIfExists(ProducerInfo producerInfo){
        ProducerBrokerExchange result = null;
        if (producerInfo != null && producerInfo.getProducerId() != null){
            synchronized (producerExchanges){
                result = producerExchanges.get(producerInfo.getProducerId());
            }
        }
        return result;
    }
    private ProducerBrokerExchange getProducerBrokerExchange(ProducerId id) throws IOException {
        ProducerBrokerExchange result = producerExchanges.get(id);
        if (result == null) {
            synchronized (producerExchanges) {
                result = new ProducerBrokerExchange();
                TransportConnectionState state = lookupConnectionState(id);
                context = state.getContext();
                result.setConnectionContext(context);
                if (context.isReconnect() || (context.isNetworkConnection() && connector.isAuditNetworkProducers())) {
                    result.setLastStoredSequenceId(broker.getBrokerService().getPersistenceAdapter().getLastProducerSequenceId(id));
                }
                SessionState ss = state.getSessionState(id.getParentId());
                if (ss != null) {
                    result.setProducerState(ss.getProducerState(id));
                    ProducerState producerState = ss.getProducerState(id);
                    if (producerState != null && producerState.getInfo() != null) {
                        ProducerInfo info = producerState.getInfo();
                        result.setMutable(info.getDestination() == null || info.getDestination().isComposite());
                    }
                }
                producerExchanges.put(id, result);
            }
        } else {
            context = result.getConnectionContext();
        }
        return result;
    }
    private void removeProducerBrokerExchange(ProducerId id) {
        synchronized (producerExchanges) {
            producerExchanges.remove(id);
        }
    }
    private ConsumerBrokerExchange getConsumerBrokerExchange(ConsumerId id) {
        ConsumerBrokerExchange result = consumerExchanges.get(id);
        return result;
    }
    private ConsumerBrokerExchange addConsumerBrokerExchange(ConsumerId id) {
        ConsumerBrokerExchange result = consumerExchanges.get(id);
        if (result == null) {
            synchronized (consumerExchanges) {
                result = new ConsumerBrokerExchange();
                TransportConnectionState state = lookupConnectionState(id);
                context = state.getContext();
                result.setConnectionContext(context);
                SessionState ss = state.getSessionState(id.getParentId());
                if (ss != null) {
                    ConsumerState cs = ss.getConsumerState(id);
                    if (cs != null) {
                        ConsumerInfo info = cs.getInfo();
                        if (info != null) {
                            if (info.getDestination() != null && info.getDestination().isPattern()) {
                                result.setWildcard(true);
                            }
                        }
                    }
                }
                consumerExchanges.put(id, result);
            }
        }
        return result;
    }
    private void removeConsumerBrokerExchange(ConsumerId id) {
        synchronized (consumerExchanges) {
            consumerExchanges.remove(id);
        }
    }
    public int getProtocolVersion() {
        return protocolVersion.get();
    }
    @Override
    public Response processControlCommand(ControlCommand command) throws Exception {
        String control = command.getCommand();
        if (control != null && control.equals("shutdown")) {
            System.exit(0);
        }
        return null;
    }
    @Override
    public Response processMessageDispatch(MessageDispatch dispatch) throws Exception {
        return null;
    }
    @Override
    public Response processConnectionControl(ConnectionControl control) throws Exception {
        if (control != null) {
            faultTolerantConnection = control.isFaultTolerant();
        }
        return null;
    }
    @Override
    public Response processConnectionError(ConnectionError error) throws Exception {
        return null;
    }
    @Override
    public Response processConsumerControl(ConsumerControl control) throws Exception {
        ConsumerBrokerExchange consumerExchange = getConsumerBrokerExchange(control.getConsumerId());
        broker.processConsumerControl(consumerExchange, control);
        return null;
    }
    protected synchronized TransportConnectionState registerConnectionState(ConnectionId connectionId,
                                                                            TransportConnectionState state) {
        TransportConnectionState cs = null;
        if (!connectionStateRegister.isEmpty() && !connectionStateRegister.doesHandleMultipleConnectionStates()) {
            TransportConnectionStateRegister newRegister = new MapTransportConnectionStateRegister();
            newRegister.intialize(connectionStateRegister);
            connectionStateRegister = newRegister;
        }
        cs = connectionStateRegister.registerConnectionState(connectionId, state);
        return cs;
    }
    protected synchronized TransportConnectionState unregisterConnectionState(ConnectionId connectionId) {
        return connectionStateRegister.unregisterConnectionState(connectionId);
    }
    protected synchronized List<TransportConnectionState> listConnectionStates() {
        return connectionStateRegister.listConnectionStates();
    }
    protected synchronized TransportConnectionState lookupConnectionState(String connectionId) {
        return connectionStateRegister.lookupConnectionState(connectionId);
    }
    protected synchronized TransportConnectionState lookupConnectionState(ConsumerId id) {
        return connectionStateRegister.lookupConnectionState(id);
    }
    protected synchronized TransportConnectionState lookupConnectionState(ProducerId id) {
        return connectionStateRegister.lookupConnectionState(id);
    }
    protected synchronized TransportConnectionState lookupConnectionState(SessionId id) {
        return connectionStateRegister.lookupConnectionState(id);
    }
    public synchronized TransportConnectionState lookupConnectionState(ConnectionId connectionId) {
        return connectionStateRegister.lookupConnectionState(connectionId);
    }
    protected synchronized void setDuplexNetworkConnectorId(String duplexNetworkConnectorId) {
        this.duplexNetworkConnectorId = duplexNetworkConnectorId;
    }
    protected synchronized String getDuplexNetworkConnectorId() {
        return this.duplexNetworkConnectorId;
    }
    public boolean isStopping() {
        return stopping.get();
    }
    protected CountDownLatch getStopped() {
        return stopped;
    }
    private int getProducerCount(ConnectionId connectionId) {
        int result = 0;
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs != null) {
            for (SessionId sessionId : cs.getSessionIds()) {
                SessionState sessionState = cs.getSessionState(sessionId);
                if (sessionState != null) {
                    result += sessionState.getProducerIds().size();
                }
            }
        }
        return result;
    }
    private int getConsumerCount(ConnectionId connectionId) {
        int result = 0;
        TransportConnectionState cs = lookupConnectionState(connectionId);
        if (cs != null) {
            for (SessionId sessionId : cs.getSessionIds()) {
                SessionState sessionState = cs.getSessionState(sessionId);
                if (sessionState != null) {
                    result += sessionState.getConsumerIds().size();
                }
            }
        }
        return result;
    }
    public WireFormatInfo getRemoteWireFormatInfo() {
        return wireFormatInfo;
    }
}
