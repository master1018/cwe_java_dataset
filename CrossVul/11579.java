
package org.jivesoftware.smack;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.AlreadyConnectedException;
import org.jivesoftware.smack.SmackException.AlreadyLoggedInException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.ResourceBindingNotOfferedException;
import org.jivesoftware.smack.SmackException.SecurityRequiredByClientException;
import org.jivesoftware.smack.SmackException.SecurityRequiredException;
import org.jivesoftware.smack.XMPPException.StreamErrorException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.compress.packet.Compress;
import org.jivesoftware.smack.compression.XMPPInputOutputStream;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.ErrorIQ;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Mechanisms;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.packet.StartTls;
import org.jivesoftware.smack.packet.Nonza;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.parsing.ParsingExceptionCallback;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.sasl.core.SASLAnonymous;
import org.jivesoftware.smack.util.BoundedThreadPoolExecutor;
import org.jivesoftware.smack.util.DNSUtil;
import org.jivesoftware.smack.util.Objects;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smack.util.ParserUtils;
import org.jivesoftware.smack.util.SmackExecutorThreadFactory;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.util.XmppStringUtils;
import org.xmlpull.v1.XmlPullParser;
public abstract class AbstractXMPPConnection implements XMPPConnection {
    private static final Logger LOGGER = Logger.getLogger(AbstractXMPPConnection.class.getName());
    private final static AtomicInteger connectionCounter = new AtomicInteger(0);
    static {
        SmackConfiguration.getVersion();
    }
    protected final Set<ConnectionListener> connectionListeners =
            new CopyOnWriteArraySet<ConnectionListener>();
    private final Collection<PacketCollector> collectors = new ConcurrentLinkedQueue<PacketCollector>();
    private final Map<StanzaListener, ListenerWrapper> syncRecvListeners = new LinkedHashMap<>();
    private final Map<StanzaListener, ListenerWrapper> asyncRecvListeners = new LinkedHashMap<>();
    private final Map<StanzaListener, ListenerWrapper> sendListeners =
            new HashMap<StanzaListener, ListenerWrapper>();
    private final Map<StanzaListener, InterceptorWrapper> interceptors =
            new HashMap<StanzaListener, InterceptorWrapper>();
    protected final Lock connectionLock = new ReentrantLock();
    protected final Map<String, ExtensionElement> streamFeatures = new HashMap<String, ExtensionElement>();
    protected EntityFullJid user;
    protected boolean connected = false;
    protected String streamId;
    private long packetReplyTimeout = SmackConfiguration.getDefaultPacketReplyTimeout();
    protected SmackDebugger debugger = null;
    protected Reader reader;
    protected Writer writer;
    protected final SynchronizationPoint<Exception> lastFeaturesReceived = new SynchronizationPoint<Exception>(
                    AbstractXMPPConnection.this, "last stream features received from server");
    protected final SynchronizationPoint<SmackException> saslFeatureReceived = new SynchronizationPoint<SmackException>(
                    AbstractXMPPConnection.this, "SASL mechanisms stream feature from server");
    protected final SASLAuthentication saslAuthentication;
    protected final int connectionCounterValue = connectionCounter.getAndIncrement();
    protected final ConnectionConfiguration config;
    private FromMode fromMode = FromMode.OMITTED;
    protected XMPPInputOutputStream compressionHandler;
    private ParsingExceptionCallback parsingExceptionCallback = SmackConfiguration.getDefaultParsingExceptionCallback();
    private final BoundedThreadPoolExecutor executorService = new BoundedThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                    100, new SmackExecutorThreadFactory(this, "Incoming Processor"));
    private final ScheduledExecutorService removeCallbacksService = Executors.newSingleThreadScheduledExecutor(
                    new SmackExecutorThreadFactory(this, "Remove Callbacks"));
    private final ExecutorService cachedExecutorService = Executors.newCachedThreadPool(
                    new SmackExecutorThreadFactory(    
                                    this,
                                    "Cached Executor"
                                    )
                    );
    private final ExecutorService singleThreadedExecutorService = Executors.newSingleThreadExecutor(new SmackExecutorThreadFactory(
                    this, "Single Threaded Executor"));
    protected String host;
    protected int port;
    protected boolean authenticated = false;
    protected boolean wasAuthenticated = false;
    private final Map<String, IQRequestHandler> setIqRequestHandler = new HashMap<>();
    private final Map<String, IQRequestHandler> getIqRequestHandler = new HashMap<>();
    protected AbstractXMPPConnection(ConnectionConfiguration configuration) {
        saslAuthentication = new SASLAuthentication(this, configuration);
        config = configuration;
        for (ConnectionCreationListener listener : XMPPConnectionRegistry.getConnectionCreationListeners()) {
            listener.connectionCreated(this);
        }
    }
    public ConnectionConfiguration getConfiguration() {
        return config;
    }
    @SuppressWarnings("deprecation")
    @Override
    public DomainBareJid getServiceName() {
        return getXMPPServiceDomain();
    }
    @Override
    public DomainBareJid getXMPPServiceDomain() {
        if (xmppServiceDomain != null) {
            return xmppServiceDomain;
        }
        return config.getXMPPServiceDomain();
    }
    @Override
    public String getHost() {
        return host;
    }
    @Override
    public int getPort() {
        return port;
    }
    @Override
    public abstract boolean isSecureConnection();
    protected abstract void sendStanzaInternal(Stanza packet) throws NotConnectedException, InterruptedException;
    @Override
    public abstract void sendNonza(Nonza element) throws NotConnectedException, InterruptedException;
    @Override
    public abstract boolean isUsingCompression();
    public synchronized AbstractXMPPConnection connect() throws SmackException, IOException, XMPPException, InterruptedException {
        throwAlreadyConnectedExceptionIfAppropriate();
        saslAuthentication.init();
        saslFeatureReceived.init();
        lastFeaturesReceived.init();
        streamId = null;
        connectInternal();
        saslFeatureReceived.checkIfSuccessOrWaitOrThrow();
        if (!isSecureConnection() && getConfiguration().getSecurityMode() == SecurityMode.required) {
            throw new SecurityRequiredByClientException();
        }
        connected = true;
        callConnectionConnectedListener();
        return this;
    }
    protected abstract void connectInternal() throws SmackException, IOException, XMPPException, InterruptedException;
    private String usedUsername, usedPassword;
    private Resourcepart usedResource;
    public synchronized void login() throws XMPPException, SmackException, IOException, InterruptedException {
        CharSequence username = usedUsername != null ? usedUsername : config.getUsername();
        String password = usedPassword != null ? usedPassword : config.getPassword();
        Resourcepart resource = usedResource != null ? usedResource : config.getResource();
        login(username, password, resource);
    }
    public synchronized void login(CharSequence username, String password) throws XMPPException, SmackException,
                    IOException, InterruptedException {
        login(username, password, config.getResource());
    }
    public synchronized void login(CharSequence username, String password, Resourcepart resource) throws XMPPException,
                    SmackException, IOException, InterruptedException {
        if (!config.allowNullOrEmptyUsername) {
            StringUtils.requireNotNullOrEmpty(username, "Username must not be null or empty");
        }
        throwNotConnectedExceptionIfAppropriate("Did you call connect() before login()?");
        throwAlreadyLoggedInExceptionIfAppropriate();
        usedUsername = username != null ? username.toString() : null;
        usedPassword = password;
        usedResource = resource;
        loginInternal(usedUsername, usedPassword, usedResource);
    }
    protected abstract void loginInternal(String username, String password, Resourcepart resource)
                    throws XMPPException, SmackException, IOException, InterruptedException;
    @Override
    public final boolean isConnected() {
        return connected;
    }
    @Override
    public final boolean isAuthenticated() {
        return authenticated;
    }
    @Override
    public final EntityFullJid getUser() {
        return user;
    }
    @Override
    public String getStreamId() {
        if (!isConnected()) {
            return null;
        }
        return streamId;
    }
    protected void bindResourceAndEstablishSession(Resourcepart resource) throws XMPPErrorException,
                    SmackException, InterruptedException {
        LOGGER.finer("Waiting for last features to be received before continuing with resource binding");
        lastFeaturesReceived.checkIfSuccessOrWait();
        if (!hasFeature(Bind.ELEMENT, Bind.NAMESPACE)) {
            throw new ResourceBindingNotOfferedException();
        }
        Bind bindResource = Bind.newSet(resource);
        PacketCollector packetCollector = createPacketCollectorAndSend(new StanzaIdFilter(bindResource), bindResource);
        Bind response = packetCollector.nextResultOrThrow();
        user = response.getJid();
        xmppServiceDomain = user.asDomainBareJid();
        Session.Feature sessionFeature = getFeature(Session.ELEMENT, Session.NAMESPACE);
        @SuppressWarnings("deprecation")
        boolean legacySessionDisabled = getConfiguration().isLegacySessionDisabled();
        if (sessionFeature != null && !sessionFeature.isOptional() && !legacySessionDisabled) {
            Session session = new Session();
            packetCollector = createPacketCollectorAndSend(new StanzaIdFilter(session), session);
            packetCollector.nextResultOrThrow();
        }
    }
    protected void afterSuccessfulLogin(final boolean resumed) throws NotConnectedException, InterruptedException {
        this.authenticated = true;
        if (config.isDebuggerEnabled() && debugger != null) {
            debugger.userHasLogged(user);
        }
        callConnectionAuthenticatedListener(resumed);
        if (config.isSendPresence() && !resumed) {
            sendStanza(new Presence(Presence.Type.available));
        }
    }
    @Override
    public final boolean isAnonymous() {
        return isAuthenticated() && SASLAnonymous.NAME.equals(getUsedSaslMechansism());
    }
    public final String getUsedSaslMechansism() {
        return saslAuthentication.getNameOfLastUsedSaslMechansism();
    }
    private DomainBareJid xmppServiceDomain;
    protected List<HostAddress> hostAddresses;
    protected List<HostAddress> populateHostAddresses() {
        List<HostAddress> failedAddresses = new LinkedList<>();
        if (config.host != null) {
            hostAddresses = new ArrayList<HostAddress>(1);
            HostAddress hostAddress = DNSUtil.getDNSResolver().lookupHostAddress(config.host, failedAddresses, config.getDnssecMode());
            hostAddresses.add(hostAddress);
        } else {
            hostAddresses = DNSUtil.resolveXMPPServiceDomain(config.getXMPPServiceDomain().toString(), failedAddresses, config.getDnssecMode());
        }
        assert(!hostAddresses.isEmpty());
        return failedAddresses;
    }
    protected Lock getConnectionLock() {
        return connectionLock;
    }
    protected void throwNotConnectedExceptionIfAppropriate() throws NotConnectedException {
        throwNotConnectedExceptionIfAppropriate(null);
    }
    protected void throwNotConnectedExceptionIfAppropriate(String optionalHint) throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException(optionalHint);
        }
    }
    protected void throwAlreadyConnectedExceptionIfAppropriate() throws AlreadyConnectedException {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }
    }
    protected void throwAlreadyLoggedInExceptionIfAppropriate() throws AlreadyLoggedInException {
        if (isAuthenticated()) {
            throw new AlreadyLoggedInException();
        }
    }
    @Deprecated
    @Override
    public void sendPacket(Stanza packet) throws NotConnectedException, InterruptedException {
        sendStanza(packet);
    }
    @Override
    public void sendStanza(Stanza stanza) throws NotConnectedException, InterruptedException {
        Objects.requireNonNull(stanza, "Stanza must not be null");
        assert(stanza instanceof Message || stanza instanceof Presence || stanza instanceof IQ);
        throwNotConnectedExceptionIfAppropriate();
        switch (fromMode) {
        case OMITTED:
            stanza.setFrom((Jid) null);
            break;
        case USER:
            stanza.setFrom(getUser());
            break;
        case UNCHANGED:
        default:
            break;
        }
        firePacketInterceptors(stanza);
        sendStanzaInternal(stanza);
    }
    protected SASLAuthentication getSASLAuthentication() {
        return saslAuthentication;
    }
    public void disconnect() {
        try {
            disconnect(new Presence(Presence.Type.unavailable));
        }
        catch (NotConnectedException e) {
            LOGGER.log(Level.FINEST, "Connection is already disconnected", e);
        }
    }
    public synchronized void disconnect(Presence unavailablePresence) throws NotConnectedException {
        try {
            sendStanza(unavailablePresence);
        }
        catch (InterruptedException e) {
            LOGGER.log(Level.FINE, "Was interrupted while sending unavailable presence. Continuing to disconnect the connection", e);
        }
        shutdown();
        callConnectionClosedListener();
    }
    protected abstract void shutdown();
    @Override
    public void addConnectionListener(ConnectionListener connectionListener) {
        if (connectionListener == null) {
            return;
        }
        connectionListeners.add(connectionListener);
    }
    @Override
    public void removeConnectionListener(ConnectionListener connectionListener) {
        connectionListeners.remove(connectionListener);
    }
    @Override
    public PacketCollector createPacketCollectorAndSend(IQ packet) throws NotConnectedException, InterruptedException {
        StanzaFilter packetFilter = new IQReplyFilter(packet, this);
        PacketCollector packetCollector = createPacketCollectorAndSend(packetFilter, packet);
        return packetCollector;
    }
    @Override
    public PacketCollector createPacketCollectorAndSend(StanzaFilter packetFilter, Stanza packet)
                    throws NotConnectedException, InterruptedException {
        PacketCollector packetCollector = createPacketCollector(packetFilter);
        try {
            sendStanza(packet);
        }
        catch (InterruptedException | NotConnectedException | RuntimeException e) {
            packetCollector.cancel();
            throw e;
        }
        return packetCollector;
    }
    @Override
    public PacketCollector createPacketCollector(StanzaFilter packetFilter) {
        PacketCollector.Configuration configuration = PacketCollector.newConfiguration().setStanzaFilter(packetFilter);
        return createPacketCollector(configuration);
    }
    @Override
    public PacketCollector createPacketCollector(PacketCollector.Configuration configuration) {
        PacketCollector collector = new PacketCollector(this, configuration);
        collectors.add(collector);
        return collector;
    }
    @Override
    public void removePacketCollector(PacketCollector collector) {
        collectors.remove(collector);
    }
    @Override
    @Deprecated
    public void addPacketListener(StanzaListener packetListener, StanzaFilter packetFilter) {
        addAsyncStanzaListener(packetListener, packetFilter);
    }
    @Override
    @Deprecated
    public boolean removePacketListener(StanzaListener packetListener) {
        return removeAsyncStanzaListener(packetListener);
    }
    @Override
    public void addSyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
        synchronized (syncRecvListeners) {
            syncRecvListeners.put(packetListener, wrapper);
        }
    }
    @Override
    public boolean removeSyncStanzaListener(StanzaListener packetListener) {
        synchronized (syncRecvListeners) {
            return syncRecvListeners.remove(packetListener) != null;
        }
    }
    @Override
    public void addAsyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
        synchronized (asyncRecvListeners) {
            asyncRecvListeners.put(packetListener, wrapper);
        }
    }
    @Override
    public boolean removeAsyncStanzaListener(StanzaListener packetListener) {
        synchronized (asyncRecvListeners) {
            return asyncRecvListeners.remove(packetListener) != null;
        }
    }
    @Override
    public void addPacketSendingListener(StanzaListener packetListener, StanzaFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        ListenerWrapper wrapper = new ListenerWrapper(packetListener, packetFilter);
        synchronized (sendListeners) {
            sendListeners.put(packetListener, wrapper);
        }
    }
    @Override
    public void removePacketSendingListener(StanzaListener packetListener) {
        synchronized (sendListeners) {
            sendListeners.remove(packetListener);
        }
    }
    @SuppressWarnings("javadoc")
    protected void firePacketSendingListeners(final Stanza packet) {
        final List<StanzaListener> listenersToNotify = new LinkedList<StanzaListener>();
        synchronized (sendListeners) {
            for (ListenerWrapper listenerWrapper : sendListeners.values()) {
                if (listenerWrapper.filterMatches(packet)) {
                    listenersToNotify.add(listenerWrapper.getListener());
                }
            }
        }
        if (listenersToNotify.isEmpty()) {
            return;
        }
        asyncGo(new Runnable() {
            @Override
            public void run() {
                for (StanzaListener listener : listenersToNotify) {
                    try {
                        listener.processPacket(packet);
                    }
                    catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Sending listener threw exception", e);
                        continue;
                    }
                }
            }});
    }
    @Override
    public void addPacketInterceptor(StanzaListener packetInterceptor,
            StanzaFilter packetFilter) {
        if (packetInterceptor == null) {
            throw new NullPointerException("Packet interceptor is null.");
        }
        InterceptorWrapper interceptorWrapper = new InterceptorWrapper(packetInterceptor, packetFilter);
        synchronized (interceptors) {
            interceptors.put(packetInterceptor, interceptorWrapper);
        }
    }
    @Override
    public void removePacketInterceptor(StanzaListener packetInterceptor) {
        synchronized (interceptors) {
            interceptors.remove(packetInterceptor);
        }
    }
    private void firePacketInterceptors(Stanza packet) {
        List<StanzaListener> interceptorsToInvoke = new LinkedList<StanzaListener>();
        synchronized (interceptors) {
            for (InterceptorWrapper interceptorWrapper : interceptors.values()) {
                if (interceptorWrapper.filterMatches(packet)) {
                    interceptorsToInvoke.add(interceptorWrapper.getInterceptor());
                }
            }
        }
        for (StanzaListener interceptor : interceptorsToInvoke) {
            try {
                interceptor.processPacket(packet);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Packet interceptor threw exception", e);
            }
        }
    }
    protected void initDebugger() {
        if (reader == null || writer == null) {
            throw new NullPointerException("Reader or writer isn't initialized.");
        }
        if (config.isDebuggerEnabled()) {
            if (debugger == null) {
                debugger = SmackConfiguration.createDebugger(this, writer, reader);
            }
            if (debugger == null) {
                LOGGER.severe("Debugging enabled but could not find debugger class");
            } else {
                reader = debugger.newConnectionReader(reader);
                writer = debugger.newConnectionWriter(writer);
            }
        }
    }
    @Override
    public long getPacketReplyTimeout() {
        return packetReplyTimeout;
    }
    @Override
    public void setPacketReplyTimeout(long timeout) {
        packetReplyTimeout = timeout;
    }
    private static boolean replyToUnknownIqDefault = true;
    public static void setReplyToUnknownIqDefault(boolean replyToUnkownIqDefault) {
        AbstractXMPPConnection.replyToUnknownIqDefault = replyToUnkownIqDefault;
    }
    private boolean replyToUnkownIq = replyToUnknownIqDefault;
    public void setReplyToUnknownIq(boolean replyToUnknownIq) {
        this.replyToUnkownIq = replyToUnknownIq;
    }
    protected void parseAndProcessStanza(XmlPullParser parser) throws Exception {
        ParserUtils.assertAtStartTag(parser);
        int parserDepth = parser.getDepth();
        Stanza stanza = null;
        try {
            stanza = PacketParserUtils.parseStanza(parser);
        }
        catch (Exception e) {
            CharSequence content = PacketParserUtils.parseContentDepth(parser,
                            parserDepth);
            UnparseableStanza message = new UnparseableStanza(content, e);
            ParsingExceptionCallback callback = getParsingExceptionCallback();
            if (callback != null) {
                callback.handleUnparsableStanza(message);
            }
        }
        ParserUtils.assertAtEndTag(parser);
        if (stanza != null) {
            processStanza(stanza);
        }
    }
    protected void processStanza(final Stanza stanza) throws InterruptedException {
        assert(stanza != null);
        lastStanzaReceived = System.currentTimeMillis();
        executorService.executeBlocking(new Runnable() {
            @Override
            public void run() {
                invokePacketCollectorsAndNotifyRecvListeners(stanza);
            }
        });
    }
    protected void invokePacketCollectorsAndNotifyRecvListeners(final Stanza packet) {
        if (packet instanceof IQ) {
            final IQ iq = (IQ) packet;
            final IQ.Type type = iq.getType();
            switch (type) {
            case set:
            case get:
                final String key = XmppStringUtils.generateKey(iq.getChildElementName(), iq.getChildElementNamespace());
                IQRequestHandler iqRequestHandler = null;
                switch (type) {
                case set:
                    synchronized (setIqRequestHandler) {
                        iqRequestHandler = setIqRequestHandler.get(key);
                    }
                    break;
                case get:
                    synchronized (getIqRequestHandler) {
                        iqRequestHandler = getIqRequestHandler.get(key);
                    }
                    break;
                default:
                    throw new IllegalStateException("Should only encounter IQ type 'get' or 'set'");
                }
                if (iqRequestHandler == null) {
                    if (!replyToUnkownIq) {
                        return;
                    }
                    ErrorIQ errorIQ = IQ.createErrorResponse(iq, XMPPError.getBuilder((
                                    XMPPError.Condition.service_unavailable)));
                    try {
                        sendStanza(errorIQ);
                    }
                    catch (InterruptedException | NotConnectedException e) {
                        LOGGER.log(Level.WARNING, "Exception while sending error IQ to unkown IQ request", e);
                    }
                } else {
                    ExecutorService executorService = null;
                    switch (iqRequestHandler.getMode()) {
                    case sync:
                        executorService = singleThreadedExecutorService;
                        break;
                    case async:
                        executorService = cachedExecutorService;
                        break;
                    }
                    final IQRequestHandler finalIqRequestHandler = iqRequestHandler;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            IQ response = finalIqRequestHandler.handleIQRequest(iq);
                            if (response == null) {
                                return;
                            }
                            try {
                                sendStanza(response);
                            }
                            catch (InterruptedException | NotConnectedException e) {
                                LOGGER.log(Level.WARNING, "Exception while sending response to IQ request", e);
                            }
                        }
                    });
                    return;
                }
                break;
            default:
                break;
            }
        }
        final Collection<StanzaListener> listenersToNotify = new LinkedList<StanzaListener>();
        synchronized (asyncRecvListeners) {
            for (ListenerWrapper listenerWrapper : asyncRecvListeners.values()) {
                if (listenerWrapper.filterMatches(packet)) {
                    listenersToNotify.add(listenerWrapper.getListener());
                }
            }
        }
        for (final StanzaListener listener : listenersToNotify) {
            asyncGo(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.processPacket(packet);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Exception in async packet listener", e);
                    }
                }
            });
        }
        for (PacketCollector collector: collectors) {
            collector.processPacket(packet);
        }
        listenersToNotify.clear();
        synchronized (syncRecvListeners) {
            for (ListenerWrapper listenerWrapper : syncRecvListeners.values()) {
                if (listenerWrapper.filterMatches(packet)) {
                    listenersToNotify.add(listenerWrapper.getListener());
                }
            }
        }
        singleThreadedExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                for (StanzaListener listener : listenersToNotify) {
                    try {
                        listener.processPacket(packet);
                    } catch(NotConnectedException e) {
                        LOGGER.log(Level.WARNING, "Got not connected exception, aborting", e);
                        break;
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Exception in packet listener", e);
                    }
                }
            }
        });
    }
    protected void setWasAuthenticated() {
        if (!wasAuthenticated) {
            wasAuthenticated = authenticated;
        }
    }
    protected void callConnectionConnectedListener() {
        for (ConnectionListener listener : connectionListeners) {
            listener.connected(this);
        }
    }
    protected void callConnectionAuthenticatedListener(boolean resumed) {
        for (ConnectionListener listener : connectionListeners) {
            try {
                listener.authenticated(this, resumed);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Exception in authenticated listener", e);
            }
        }
    }
    void callConnectionClosedListener() {
        for (ConnectionListener listener : connectionListeners) {
            try {
                listener.connectionClosed();
            }
            catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e);
            }
        }
    }
    protected void callConnectionClosedOnErrorListener(Exception e) {
        boolean logWarning = true;
        if (e instanceof StreamErrorException) {
            StreamErrorException see = (StreamErrorException) e;
            if (see.getStreamError().getCondition() == StreamError.Condition.not_authorized
                            && wasAuthenticated) {
                logWarning = false;
                LOGGER.log(Level.FINE,
                                "Connection closed with not-authorized stream error after it was already authenticated. The account was likely deleted/unregistered on the server");
            }
        }
        if (logWarning) {
            LOGGER.log(Level.WARNING, "Connection " + this + " closed with error", e);
        }
        for (ConnectionListener listener : connectionListeners) {
            try {
                listener.connectionClosedOnError(e);
            }
            catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e2);
            }
        }
    }
    protected void notifyReconnection() {
        for (ConnectionListener listener : connectionListeners) {
            try {
                listener.reconnectionSuccessful();
            }
            catch (Exception e) {
                LOGGER.log(Level.WARNING, "notifyReconnection()", e);
            }
        }
    }
    protected static class ListenerWrapper {
        private final StanzaListener packetListener;
        private final StanzaFilter packetFilter;
        public ListenerWrapper(StanzaListener packetListener, StanzaFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }
        public boolean filterMatches(Stanza packet) {
            return packetFilter == null || packetFilter.accept(packet);
        }
        public StanzaListener getListener() {
            return packetListener;
        }
    }
    protected static class InterceptorWrapper {
        private final StanzaListener packetInterceptor;
        private final StanzaFilter packetFilter;
        public InterceptorWrapper(StanzaListener packetInterceptor, StanzaFilter packetFilter) {
            this.packetInterceptor = packetInterceptor;
            this.packetFilter = packetFilter;
        }
        public boolean filterMatches(Stanza packet) {
            return packetFilter == null || packetFilter.accept(packet);
        }
        public StanzaListener getInterceptor() {
            return packetInterceptor;
        }
    }
    @Override
    public int getConnectionCounter() {
        return connectionCounterValue;
    }
    @Override
    public void setFromMode(FromMode fromMode) {
        this.fromMode = fromMode;
    }
    @Override
    public FromMode getFromMode() {
        return this.fromMode;
    }
    @Override
    protected void finalize() throws Throwable {
        LOGGER.fine("finalizing " + this + ": Shutting down executor services");
        try {
            executorService.shutdownNow();
            cachedExecutorService.shutdown();
            removeCallbacksService.shutdownNow();
            singleThreadedExecutorService.shutdownNow();
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "finalize() threw trhowable", t);
        }
        finally {
            super.finalize();
        }
    }
    protected final void parseFeatures(XmlPullParser parser) throws Exception {
        streamFeatures.clear();
        final int initialDepth = parser.getDepth();
        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG && parser.getDepth() == initialDepth + 1) {
                ExtensionElement streamFeature = null;
                String name = parser.getName();
                String namespace = parser.getNamespace();
                switch (name) {
                case StartTls.ELEMENT:
                    streamFeature = PacketParserUtils.parseStartTlsFeature(parser);
                    break;
                case Mechanisms.ELEMENT:
                    streamFeature = new Mechanisms(PacketParserUtils.parseMechanisms(parser));
                    break;
                case Bind.ELEMENT:
                    streamFeature = Bind.Feature.INSTANCE;
                    break;
                case Session.ELEMENT:
                    streamFeature = PacketParserUtils.parseSessionFeature(parser);
                    break;
                case Compress.Feature.ELEMENT:
                    streamFeature = PacketParserUtils.parseCompressionFeature(parser);
                    break;
                default:
                    ExtensionElementProvider<ExtensionElement> provider = ProviderManager.getStreamFeatureProvider(name, namespace);
                    if (provider != null) {
                        streamFeature = provider.parse(parser);
                    }
                    break;
                }
                if (streamFeature != null) {
                    addStreamFeature(streamFeature);
                }
            }
            else if (eventType == XmlPullParser.END_TAG && parser.getDepth() == initialDepth) {
                break;
            }
        }
        if (hasFeature(Mechanisms.ELEMENT, Mechanisms.NAMESPACE)) {
            if (!hasFeature(StartTls.ELEMENT, StartTls.NAMESPACE)
                            || config.getSecurityMode() == SecurityMode.disabled) {
                saslFeatureReceived.reportSuccess();
            }
        }
        if (hasFeature(Bind.ELEMENT, Bind.NAMESPACE)) {
            if (!hasFeature(Compress.Feature.ELEMENT, Compress.NAMESPACE)
                            || !config.isCompressionEnabled()) {
                lastFeaturesReceived.reportSuccess();
            }
        }
        afterFeaturesReceived();
    }
    @SuppressWarnings("unused")
    protected void afterFeaturesReceived() throws SecurityRequiredException, NotConnectedException, InterruptedException {
    }
    @SuppressWarnings("unchecked")
    @Override
    public <F extends ExtensionElement> F getFeature(String element, String namespace) {
        return (F) streamFeatures.get(XmppStringUtils.generateKey(element, namespace));
    }
    @Override
    public boolean hasFeature(String element, String namespace) {
        return getFeature(element, namespace) != null;
    }
    protected void addStreamFeature(ExtensionElement feature) {
        String key = XmppStringUtils.generateKey(feature.getElementName(), feature.getNamespace());
        streamFeatures.put(key, feature);
    }
    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter,
                    StanzaListener callback) throws NotConnectedException, InterruptedException {
        sendStanzaWithResponseCallback(stanza, replyFilter, callback, null);
    }
    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter,
                    StanzaListener callback, ExceptionCallback exceptionCallback)
                    throws NotConnectedException, InterruptedException {
        sendStanzaWithResponseCallback(stanza, replyFilter, callback, exceptionCallback,
                        getPacketReplyTimeout());
    }
    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, final StanzaFilter replyFilter,
                    final StanzaListener callback, final ExceptionCallback exceptionCallback,
                    long timeout) throws NotConnectedException, InterruptedException {
        Objects.requireNonNull(stanza, "stanza must not be null");
        Objects.requireNonNull(replyFilter, "replyFilter must not be null");
        Objects.requireNonNull(callback, "callback must not be null");
        final StanzaListener packetListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws NotConnectedException, InterruptedException {
                try {
                    XMPPErrorException.ifHasErrorThenThrow(packet);
                    callback.processPacket(packet);
                }
                catch (XMPPErrorException e) {
                    if (exceptionCallback != null) {
                        exceptionCallback.processException(e);
                    }
                }
                finally {
                    removeAsyncStanzaListener(this);
                }
            }
        };
        removeCallbacksService.schedule(new Runnable() {
            @Override
            public void run() {
                boolean removed = removeAsyncStanzaListener(packetListener);
                if (removed && exceptionCallback != null) {
                    Exception exception;
                    if (!isConnected()) {
                        exception = new NotConnectedException(AbstractXMPPConnection.this, replyFilter);
                    } else {
                        exception = NoResponseException.newWith(AbstractXMPPConnection.this, replyFilter);
                    }
                    exceptionCallback.processException(exception);
                }
            }
        }, timeout, TimeUnit.MILLISECONDS);
        addAsyncStanzaListener(packetListener, replyFilter);
        sendStanza(stanza);
    }
    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback)
                    throws NotConnectedException, InterruptedException {
        sendIqWithResponseCallback(iqRequest, callback, null);
    }
    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback,
                    ExceptionCallback exceptionCallback) throws NotConnectedException, InterruptedException {
        sendIqWithResponseCallback(iqRequest, callback, exceptionCallback, getPacketReplyTimeout());
    }
    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, final StanzaListener callback,
                    final ExceptionCallback exceptionCallback, long timeout)
                    throws NotConnectedException, InterruptedException {
        StanzaFilter replyFilter = new IQReplyFilter(iqRequest, this);
        sendStanzaWithResponseCallback(iqRequest, replyFilter, callback, exceptionCallback, timeout);
    }
    @Override
    public void addOneTimeSyncCallback(final StanzaListener callback, final StanzaFilter packetFilter) {
        final StanzaListener packetListener = new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws NotConnectedException, InterruptedException {
                try {
                    callback.processPacket(packet);
                } finally {
                    removeSyncStanzaListener(this);
                }
            }
        };
        addSyncStanzaListener(packetListener, packetFilter);
        removeCallbacksService.schedule(new Runnable() {
            @Override
            public void run() {
                removeSyncStanzaListener(packetListener);
            }
        }, getPacketReplyTimeout(), TimeUnit.MILLISECONDS);
    }
    @Override
    public IQRequestHandler registerIQRequestHandler(final IQRequestHandler iqRequestHandler) {
        final String key = XmppStringUtils.generateKey(iqRequestHandler.getElement(), iqRequestHandler.getNamespace());
        switch (iqRequestHandler.getType()) {
        case set:
            synchronized (setIqRequestHandler) {
                return setIqRequestHandler.put(key, iqRequestHandler);
            }
        case get:
            synchronized (getIqRequestHandler) {
                return getIqRequestHandler.put(key, iqRequestHandler);
            }
        default:
            throw new IllegalArgumentException("Only IQ type of 'get' and 'set' allowed");
        }
    }
    @Override
    public final IQRequestHandler unregisterIQRequestHandler(IQRequestHandler iqRequestHandler) {
        return unregisterIQRequestHandler(iqRequestHandler.getElement(), iqRequestHandler.getNamespace(),
                        iqRequestHandler.getType());
    }
    @Override
    public IQRequestHandler unregisterIQRequestHandler(String element, String namespace, IQ.Type type) {
        final String key = XmppStringUtils.generateKey(element, namespace);
        switch (type) {
        case set:
            synchronized (setIqRequestHandler) {
                return setIqRequestHandler.remove(key);
            }
        case get:
            synchronized (getIqRequestHandler) {
                return getIqRequestHandler.remove(key);
            }
        default:
            throw new IllegalArgumentException("Only IQ type of 'get' and 'set' allowed");
        }
    }
    private long lastStanzaReceived;
    public long getLastStanzaReceived() {
        return lastStanzaReceived;
    }
    public void setParsingExceptionCallback(ParsingExceptionCallback callback) {
        parsingExceptionCallback = callback;
    }
    public ParsingExceptionCallback getParsingExceptionCallback() {
        return parsingExceptionCallback;
    }
    @Override
    public final String toString() {
        EntityFullJid localEndpoint = getUser();
        String localEndpointString = (localEndpoint == null ?  "not-authenticated" : localEndpoint.toString());
        return getClass().getSimpleName() + '[' + localEndpointString + "] (" + getConnectionCounter() + ')';
    }
    protected final void asyncGo(Runnable runnable) {
        cachedExecutorService.execute(runnable);
    }
    protected final ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit unit) {
        return removeCallbacksService.schedule(runnable, delay, unit);
    }
}
