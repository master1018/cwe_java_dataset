
package org.jivesoftware.smack.tcp;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.AlreadyConnectedException;
import org.jivesoftware.smack.SmackException.AlreadyLoggedInException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.SecurityRequiredByClientException;
import org.jivesoftware.smack.SmackException.ConnectionException;
import org.jivesoftware.smack.SmackException.SecurityRequiredByServerException;
import org.jivesoftware.smack.SynchronizationPoint;
import org.jivesoftware.smack.XMPPException.StreamErrorException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.compress.packet.Compressed;
import org.jivesoftware.smack.compression.XMPPInputOutputStream;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.compress.packet.Compress;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StreamOpen;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StartTls;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.Challenge;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.SASLFailure;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.Success;
import org.jivesoftware.smack.sm.SMUtils;
import org.jivesoftware.smack.sm.StreamManagementException;
import org.jivesoftware.smack.sm.StreamManagementException.StreamIdDoesNotMatchException;
import org.jivesoftware.smack.sm.StreamManagementException.StreamManagementCounterError;
import org.jivesoftware.smack.sm.StreamManagementException.StreamManagementNotEnabledException;
import org.jivesoftware.smack.sm.packet.StreamManagement;
import org.jivesoftware.smack.sm.packet.StreamManagement.AckAnswer;
import org.jivesoftware.smack.sm.packet.StreamManagement.AckRequest;
import org.jivesoftware.smack.sm.packet.StreamManagement.Enable;
import org.jivesoftware.smack.sm.packet.StreamManagement.Enabled;
import org.jivesoftware.smack.sm.packet.StreamManagement.Failed;
import org.jivesoftware.smack.sm.packet.StreamManagement.Resume;
import org.jivesoftware.smack.sm.packet.StreamManagement.Resumed;
import org.jivesoftware.smack.sm.packet.StreamManagement.StreamManagementFeature;
import org.jivesoftware.smack.sm.predicates.Predicate;
import org.jivesoftware.smack.sm.provider.ParseStreamManagement;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.ArrayBlockingQueueWithShutdown;
import org.jivesoftware.smack.util.Async;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smack.util.dns.HostAddress;
import org.jxmpp.util.XmppStringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.PasswordCallback;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
public class XMPPTCPConnection extends AbstractXMPPConnection {
    private static final int QUEUE_SIZE = 500;
    private static final Logger LOGGER = Logger.getLogger(XMPPTCPConnection.class.getName());
    private Socket socket;
    private boolean disconnectedButResumeable = false;
    private volatile boolean socketClosed = false;
    private boolean usingTLS = false;
    protected PacketWriter packetWriter;
    protected PacketReader packetReader;
    private final SynchronizationPoint<Exception> initalOpenStreamSend = new SynchronizationPoint<Exception>(this);
    private final SynchronizationPoint<XMPPException> maybeCompressFeaturesReceived = new SynchronizationPoint<XMPPException>(
                    this);
    private final SynchronizationPoint<XMPPException> compressSyncPoint = new SynchronizationPoint<XMPPException>(
                    this);
    private static BundleAndDeferCallback defaultBundleAndDeferCallback;
    private BundleAndDeferCallback bundleAndDeferCallback = defaultBundleAndDeferCallback;
    private static boolean useSmDefault = false;
    private static boolean useSmResumptionDefault = true;
    private String smSessionId;
    private final SynchronizationPoint<XMPPException> smResumedSyncPoint = new SynchronizationPoint<XMPPException>(
                    this);
    private final SynchronizationPoint<XMPPException> smEnabledSyncPoint = new SynchronizationPoint<XMPPException>(
                    this);
    private int smClientMaxResumptionTime = -1;
    private int smServerMaxResumptimTime = -1;
    private boolean useSm = useSmDefault;
    private boolean useSmResumption = useSmResumptionDefault;
    private long serverHandledStanzasCount = 0;
    private long clientHandledStanzasCount = 0;
    private BlockingQueue<Stanza> unacknowledgedStanzas;
    private boolean smWasEnabledAtLeastOnce = false;
    private final Collection<StanzaListener> stanzaAcknowledgedListeners = new ConcurrentLinkedQueue<StanzaListener>();
    private final Map<String, StanzaListener> stanzaIdAcknowledgedListeners = new ConcurrentHashMap<String, StanzaListener>();
    private final Set<StanzaFilter> requestAckPredicates = new LinkedHashSet<StanzaFilter>();
    private final XMPPTCPConnectionConfiguration config;
    public XMPPTCPConnection(XMPPTCPConnectionConfiguration config) {
        super(config);
        this.config = config;
        addConnectionListener(new AbstractConnectionListener() {
            @Override
            public void connectionClosedOnError(Exception e) {
                if (e instanceof XMPPException.StreamErrorException) {
                    dropSmState();
                }
            }
        });
    }
    public XMPPTCPConnection(CharSequence jid, String password) {
        this(XmppStringUtils.parseLocalpart(jid.toString()), password, XmppStringUtils.parseDomain(jid.toString()));
    }
    public XMPPTCPConnection(CharSequence username, String password, String serviceName) {
        this(XMPPTCPConnectionConfiguration.builder().setUsernameAndPassword(username, password).setServiceName(
                                        serviceName).build());
    }
    @Override
    protected void throwNotConnectedExceptionIfAppropriate() throws NotConnectedException {
        if (packetWriter == null) {
            throw new NotConnectedException();
        }
        packetWriter.throwNotConnectedExceptionIfDoneAndResumptionNotPossible();
    }
    @Override
    protected void throwAlreadyConnectedExceptionIfAppropriate() throws AlreadyConnectedException {
        if (isConnected() && !disconnectedButResumeable) {
            throw new AlreadyConnectedException();
        }
    }
    @Override
    protected void throwAlreadyLoggedInExceptionIfAppropriate() throws AlreadyLoggedInException {
        if (isAuthenticated() && !disconnectedButResumeable) {
            throw new AlreadyLoggedInException();
        }
    }
    @Override
    protected void afterSuccessfulLogin(final boolean resumed) throws NotConnectedException {
        disconnectedButResumeable = false;
        super.afterSuccessfulLogin(resumed);
    }
    @Override
    protected synchronized void loginNonAnonymously(String username, String password, String resource) throws XMPPException, SmackException, IOException {
        if (saslAuthentication.hasNonAnonymousAuthentication()) {
            if (password != null) {
                saslAuthentication.authenticate(username, password, resource);
            }
            else {
                saslAuthentication.authenticate(resource, config.getCallbackHandler());
            }
        } else {
            throw new SmackException("No non-anonymous SASL authentication mechanism available");
        }
        if (config.isCompressionEnabled()) {
            useCompression();
        }
        if (isSmResumptionPossible()) {
            smResumedSyncPoint.sendAndWaitForResponse(new Resume(clientHandledStanzasCount, smSessionId));
            if (smResumedSyncPoint.wasSuccessful()) {
                afterSuccessfulLogin(true);
                return;
            }
            LOGGER.fine("Stream resumption failed, continuing with normal stream establishment process");
        }
        List<Stanza> previouslyUnackedStanzas = new LinkedList<Stanza>();
        if (unacknowledgedStanzas != null) {
            unacknowledgedStanzas.drainTo(previouslyUnackedStanzas);
            dropSmState();
        }
        bindResourceAndEstablishSession(resource);
        if (isSmAvailable() && useSm) {
            serverHandledStanzasCount = 0;
            smEnabledSyncPoint.sendAndWaitForResponseOrThrow(new Enable(useSmResumption, smClientMaxResumptionTime));
            synchronized (requestAckPredicates) {
                if (requestAckPredicates.isEmpty()) {
                    requestAckPredicates.add(Predicate.forMessagesOrAfter5Stanzas());
                }
            }
        }
        for (Stanza stanza : previouslyUnackedStanzas) {
            sendStanzaInternal(stanza);
        }
        afterSuccessfulLogin(false);
    }
    @Override
    public synchronized void loginAnonymously() throws XMPPException, SmackException, IOException {
        saslFeatureReceived.checkIfSuccessOrWaitOrThrow();
        if (saslAuthentication.hasAnonymousAuthentication()) {
            saslAuthentication.authenticateAnonymously();
        }
        else {
            throw new SmackException("No anonymous SASL authentication mechanism available");
        }
        if (config.isCompressionEnabled()) {
            useCompression();
        }
        bindResourceAndEstablishSession(null);
        afterSuccessfulLogin(false);
    }
    @Override
    public boolean isSecureConnection() {
        return usingTLS;
    }
    public boolean isSocketClosed() {
        return socketClosed;
    }
    @Override
    protected void shutdown() {
        if (isSmEnabled()) {
            try {
                sendSmAcknowledgementInternal();
            } catch (NotConnectedException e) {
                LOGGER.log(Level.FINE, "Can not send final SM ack as connection is not connected", e);
            }
        }
        shutdown(false);
    }
    public synchronized void instantShutdown() {
        shutdown(true);
    }
    private void shutdown(boolean instant) {
        if (disconnectedButResumeable) {
            return;
        }
        if (packetReader != null) {
                packetReader.shutdown();
        }
        if (packetWriter != null) {
                packetWriter.shutdown(instant);
        }
        socketClosed = true;
        try {
                socket.close();
        } catch (Exception e) {
                LOGGER.log(Level.WARNING, "shutdown", e);
        }
        setWasAuthenticated();
        if (isSmResumptionPossible() && instant) {
            disconnectedButResumeable = true;
        } else {
            disconnectedButResumeable = false;
            smSessionId = null;
        }
        authenticated = false;
        connected = false;
        usingTLS = false;
        reader = null;
        writer = null;
        maybeCompressFeaturesReceived.init();
        compressSyncPoint.init();
        smResumedSyncPoint.init();
        smEnabledSyncPoint.init();
        initalOpenStreamSend.init();
    }
    @Override
    public void send(PlainStreamElement element) throws NotConnectedException {
        packetWriter.sendStreamElement(element);
    }
    @Override
    protected void sendStanzaInternal(Stanza packet) throws NotConnectedException {
        packetWriter.sendStreamElement(packet);
        if (isSmEnabled()) {
            for (StanzaFilter requestAckPredicate : requestAckPredicates) {
                if (requestAckPredicate.accept(packet)) {
                    requestSmAcknowledgementInternal();
                    break;
                }
            }
        }
    }
    private void connectUsingConfiguration() throws IOException, ConnectionException {
        List<HostAddress> failedAddresses = populateHostAddresses();
        SocketFactory socketFactory = config.getSocketFactory();
        if (socketFactory == null) {
            socketFactory = SocketFactory.getDefault();
        }
        for (HostAddress hostAddress : hostAddresses) {
            String host = hostAddress.getFQDN();
            int port = hostAddress.getPort();
            socket = socketFactory.createSocket();
            try {
                Iterator<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(host)).iterator();
                if (!inetAddresses.hasNext()) {
                    LOGGER.warning("InetAddress.getAllByName() returned empty result array.");
                    throw new UnknownHostException(host);
                }
                innerloop: while (inetAddresses.hasNext()) {
                    socket = socketFactory.createSocket();
                    final InetAddress inetAddress = inetAddresses.next();
                    final String inetAddressAndPort = inetAddress + " at port " + port;
                    LOGGER.finer("Trying to establish TCP connection to " + inetAddressAndPort);
                    try {
                        socket.connect(new InetSocketAddress(inetAddress, port), config.getConnectTimeout());
                    } catch (Exception e) {
                        if (inetAddresses.hasNext()) {
                            continue innerloop;
                        } else {
                            throw e;
                        }
                    }
                    LOGGER.finer("Established TCP connection to " + inetAddressAndPort);
                    this.host = host;
                    this.port = port;
                    return;
                }
            }
            catch (Exception e) {
                hostAddress.setException(e);
                failedAddresses.add(hostAddress);
            }
        }
        throw ConnectionException.from(failedAddresses);
    }
    private void initConnection() throws IOException {
        boolean isFirstInitialization = packetReader == null || packetWriter == null;
        compressionHandler = null;
        initReaderAndWriter();
        if (isFirstInitialization) {
            packetWriter = new PacketWriter();
            packetReader = new PacketReader();
            if (config.isDebuggerEnabled()) {
                addAsyncStanzaListener(debugger.getReaderListener(), null);
                if (debugger.getWriterListener() != null) {
                    addPacketSendingListener(debugger.getWriterListener(), null);
                }
            }
        }
        packetWriter.init();
        packetReader.init();
        if (isFirstInitialization) {
            for (ConnectionCreationListener listener : getConnectionCreationListeners()) {
                listener.connectionCreated(this);
            }
        }
    }
    private void initReaderAndWriter() throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        if (compressionHandler != null) {
            is = compressionHandler.getInputStream(is);
            os = compressionHandler.getOutputStream(os);
        }
        writer = new OutputStreamWriter(os, "UTF-8");
        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        initDebugger();
    }
    private void proceedTLSReceived() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, NoSuchProviderException, UnrecoverableKeyException, KeyManagementException, SmackException {
        SSLContext context = this.config.getCustomSSLContext();
        KeyStore ks = null;
        KeyManager[] kms = null;
        PasswordCallback pcb = null;
        if(config.getCallbackHandler() == null) {
           ks = null;
        } else if (context == null) {
            if(config.getKeystoreType().equals("NONE")) {
                ks = null;
                pcb = null;
            }
            else if(config.getKeystoreType().equals("PKCS11")) {
                try {
                    Constructor<?> c = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(InputStream.class);
                    String pkcs11Config = "name = SmartCard\nlibrary = "+config.getPKCS11Library();
                    ByteArrayInputStream config = new ByteArrayInputStream(pkcs11Config.getBytes());
                    Provider p = (Provider)c.newInstance(config);
                    Security.addProvider(p);
                    ks = KeyStore.getInstance("PKCS11",p);
                    pcb = new PasswordCallback("PKCS11 Password: ",false);
                    this.config.getCallbackHandler().handle(new Callback[]{pcb});
                    ks.load(null,pcb.getPassword());
                }
                catch (Exception e) {
                    ks = null;
                    pcb = null;
                }
            }
            else if(config.getKeystoreType().equals("Apple")) {
                ks = KeyStore.getInstance("KeychainStore","Apple");
                ks.load(null,null);
            }
            else {
                ks = KeyStore.getInstance(config.getKeystoreType());
                try {
                    pcb = new PasswordCallback("Keystore Password: ",false);
                    config.getCallbackHandler().handle(new Callback[]{pcb});
                    ks.load(new FileInputStream(config.getKeystorePath()), pcb.getPassword());
                }
                catch(Exception e) {
                    ks = null;
                    pcb = null;
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            try {
                if(pcb == null) {
                    kmf.init(ks,null);
                } else {
                    kmf.init(ks,pcb.getPassword());
                    pcb.clearPassword();
                }
                kms = kmf.getKeyManagers();
            } catch (NullPointerException npe) {
                kms = null;
            }
        }
        if (context == null) {
            context = SSLContext.getInstance("TLS");
            context.init(kms, null, new java.security.SecureRandom());
        }
        Socket plain = socket;
        socket = context.getSocketFactory().createSocket(plain,
                host, plain.getPort(), true);
        final SSLSocket sslSocket = (SSLSocket) socket;
        TLSUtils.setEnabledProtocolsAndCiphers(sslSocket, config.getEnabledSSLProtocols(), config.getEnabledSSLCiphers());
        initReaderAndWriter();
        sslSocket.startHandshake();
        final HostnameVerifier verifier = getConfiguration().getHostnameVerifier();
        if (verifier == null) {
                throw new IllegalStateException("No HostnameVerifier set. Use connectionConfiguration.setHostnameVerifier() to configure.");
        } else if (!verifier.verify(getServiceName(), sslSocket.getSession())) {
            throw new CertificateException("Hostname verification of certificate failed. Certificate does not authenticate " + getServiceName());
        }
        usingTLS = true;
    }
    private XMPPInputOutputStream maybeGetCompressionHandler() {
        Compress.Feature compression = getFeature(Compress.Feature.ELEMENT, Compress.NAMESPACE);
        if (compression == null) {
            return null;
        }
        for (XMPPInputOutputStream handler : SmackConfiguration.getCompresionHandlers()) {
                String method = handler.getCompressionMethod();
                if (compression.getMethods().contains(method))
                    return handler;
        }
        return null;
    }
    @Override
    public boolean isUsingCompression() {
        return compressionHandler != null && compressSyncPoint.wasSuccessful();
    }
    private void useCompression() throws NotConnectedException, NoResponseException, XMPPException {
        maybeCompressFeaturesReceived.checkIfSuccessOrWait();
        if ((compressionHandler = maybeGetCompressionHandler()) != null) {
            compressSyncPoint.sendAndWaitForResponseOrThrow(new Compress(compressionHandler.getCompressionMethod()));
        } else {
            LOGGER.warning("Could not enable compression because no matching handler/method pair was found");
        }
    }
    @Override
    protected void connectInternal() throws SmackException, IOException, XMPPException {
        connectUsingConfiguration();
        socketClosed = false;
        initConnection();
        saslFeatureReceived.checkIfSuccessOrWaitOrThrow();
        if (!isSecureConnection() && getConfiguration().getSecurityMode() == SecurityMode.required) {
            shutdown();
            throw new SecurityRequiredByClientException();
        }
        connected = true;
        callConnectionConnectedListener();
        if (wasAuthenticated) {
            login();
            notifyReconnection();
        }
    }
    private synchronized void notifyConnectionError(Exception e) {
        if ((packetReader == null || packetReader.done) &&
                (packetWriter == null || packetWriter.done())) return;
        instantShutdown();
        callConnectionClosedOnErrorListener(e);
    }
    protected void setWriter(Writer writer) {
        this.writer = writer;
    }
    @Override
    protected void afterFeaturesReceived() throws NotConnectedException {
        StartTls startTlsFeature = getFeature(StartTls.ELEMENT, StartTls.NAMESPACE);
        if (startTlsFeature != null) {
            if (startTlsFeature.required() && config.getSecurityMode() == SecurityMode.disabled) {
                notifyConnectionError(new SecurityRequiredByServerException());
                return;
            }
            if (config.getSecurityMode() != ConnectionConfiguration.SecurityMode.disabled) {
                send(new StartTls());
            }
        }
        if (getSASLAuthentication().authenticationSuccessful()) {
            maybeCompressFeaturesReceived.reportSuccess();
        }
    }
    void openStream() throws SmackException {
        CharSequence to = getServiceName();
        CharSequence from = null;
        CharSequence localpart = config.getUsername();
        if (localpart != null) {
            from = XmppStringUtils.completeJidFrom(localpart, to);
        }
        String id = getStreamId();
        send(new StreamOpen(to, from, id));
        try {
            packetReader.parser = PacketParserUtils.newXmppParser(reader);
        }
        catch (XmlPullParserException e) {
            throw new SmackException(e);
        }
    }
    protected class PacketReader {
        XmlPullParser parser;
        private volatile boolean done;
        void init() {
            done = false;
            Async.go(new Runnable() {
                public void run() {
                    parsePackets();
                }
            }, "Smack Packet Reader (" + getConnectionCounter() + ")");
         }
        void shutdown() {
            done = true;
        }
        private void parsePackets() {
            try {
                initalOpenStreamSend.checkIfSuccessOrWait();
                int eventType = parser.getEventType();
                while (!done) {
                    switch (eventType) {
                    case XmlPullParser.START_TAG:
                        final String name = parser.getName();
                        switch (name) {
                        case Message.ELEMENT:
                        case IQ.IQ_ELEMENT:
                        case Presence.ELEMENT:
                            try {
                                parseAndProcessStanza(parser);
                            } finally {
                                clientHandledStanzasCount = SMUtils.incrementHeight(clientHandledStanzasCount);
                            }
                            break;
                        case "stream":
                            if ("jabber:client".equals(parser.getNamespace(null))) {
                                streamId = parser.getAttributeValue("", "id");
                                String reportedServiceName = parser.getAttributeValue("", "from");
                                assert(reportedServiceName.equals(config.getServiceName()));
                            }
                            break;
                        case "error":
                            throw new StreamErrorException(PacketParserUtils.parseStreamError(parser));
                        case "features":
                            parseFeatures(parser);
                            break;
                        case "proceed":
                            try {
                                proceedTLSReceived();
                                openStream();
                            }
                            catch (Exception e) {
                                saslFeatureReceived.reportFailure(new SmackException(e));
                                throw e;
                            }
                            break;
                        case "failure":
                            String namespace = parser.getNamespace(null);
                            switch (namespace) {
                            case "urn:ietf:params:xml:ns:xmpp-tls":
                                throw new XMPPErrorException("TLS negotiation has failed", null);
                            case "http:
                                compressSyncPoint.reportFailure(new XMPPErrorException(
                                                "Could not establish compression", null));
                                break;
                            case SaslStreamElements.NAMESPACE:
                                final SASLFailure failure = PacketParserUtils.parseSASLFailure(parser);
                                getSASLAuthentication().authenticationFailed(failure);
                                break;
                            }
                            break;
                        case Challenge.ELEMENT:
                            String challengeData = parser.nextText();
                            getSASLAuthentication().challengeReceived(challengeData);
                            break;
                        case Success.ELEMENT:
                            Success success = new Success(parser.nextText());
                            openStream();
                            getSASLAuthentication().authenticated(success);
                            break;
                        case Compressed.ELEMENT:
                            initReaderAndWriter();
                            openStream();
                            compressSyncPoint.reportSuccess();
                            break;
                        case Enabled.ELEMENT:
                            Enabled enabled = ParseStreamManagement.enabled(parser);
                            if (enabled.isResumeSet()) {
                                smSessionId = enabled.getId();
                                if (StringUtils.isNullOrEmpty(smSessionId)) {
                                    XMPPErrorException xmppException = new XMPPErrorException(
                                                    "Stream Management 'enabled' element with resume attribute but without session id received",
                                                    new XMPPError(
                                                                    XMPPError.Condition.bad_request));
                                    smEnabledSyncPoint.reportFailure(xmppException);
                                    throw xmppException;
                                }
                                smServerMaxResumptimTime = enabled.getMaxResumptionTime();
                            } else {
                                smSessionId = null;
                            }
                            clientHandledStanzasCount = 0;
                            smWasEnabledAtLeastOnce = true;
                            smEnabledSyncPoint.reportSuccess();
                            LOGGER.fine("Stream Management (XEP-198): succesfully enabled");
                            break;
                        case Failed.ELEMENT:
                            Failed failed = ParseStreamManagement.failed(parser);
                            XMPPError xmppError = new XMPPError(failed.getXMPPErrorCondition());
                            XMPPException xmppException = new XMPPErrorException("Stream Management failed", xmppError);
                            if (smResumedSyncPoint.requestSent()) {
                                smResumedSyncPoint.reportFailure(xmppException);
                            }
                            else {
                                if (!smEnabledSyncPoint.requestSent()) {
                                    throw new IllegalStateException("Failed element received but SM was not previously enabled");
                                }
                                smEnabledSyncPoint.reportFailure(xmppException);
                                lastFeaturesReceived.reportSuccess();
                            }
                            break;
                        case Resumed.ELEMENT:
                            Resumed resumed = ParseStreamManagement.resumed(parser);
                            if (!smSessionId.equals(resumed.getPrevId())) {
                                throw new StreamIdDoesNotMatchException(smSessionId, resumed.getPrevId());
                            }
                            smResumedSyncPoint.reportSuccess();
                            smEnabledSyncPoint.reportSuccess();
                            processHandledCount(resumed.getHandledCount());
                            List<Stanza> stanzasToResend = new ArrayList<>(unacknowledgedStanzas.size());
                            unacknowledgedStanzas.drainTo(stanzasToResend);
                            for (Stanza stanza : stanzasToResend) {
                                sendStanzaInternal(stanza);
                            }
                            if (!stanzasToResend.isEmpty()) {
                                requestSmAcknowledgementInternal();
                            }
                            LOGGER.fine("Stream Management (XEP-198): Stream resumed");
                            break;
                        case AckAnswer.ELEMENT:
                            AckAnswer ackAnswer = ParseStreamManagement.ackAnswer(parser);
                            processHandledCount(ackAnswer.getHandledCount());
                            break;
                        case AckRequest.ELEMENT:
                            ParseStreamManagement.ackRequest(parser);
                            if (smEnabledSyncPoint.wasSuccessful()) {
                                sendSmAcknowledgementInternal();
                            } else {
                                LOGGER.warning("SM Ack Request received while SM is not enabled");
                            }
                            break;
                         default:
                             LOGGER.warning("Unknown top level stream element: " + name);
                             break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("stream")) {
                            disconnect();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        throw new SmackException(
                                        "Parser got END_DOCUMENT event. This could happen e.g. if the server closed the connection without sending a closing stream element");
                    }
                    eventType = parser.next();
                }
            }
            catch (Exception e) {
                if (!(done || isSocketClosed())) {
                    notifyConnectionError(e);
                }
            }
        }
    }
    protected class PacketWriter {
        public static final int QUEUE_SIZE = XMPPTCPConnection.QUEUE_SIZE;
        private final ArrayBlockingQueueWithShutdown<Element> queue = new ArrayBlockingQueueWithShutdown<Element>(
                        QUEUE_SIZE, true);
        protected SynchronizationPoint<NoResponseException> shutdownDone = new SynchronizationPoint<NoResponseException>(
                        XMPPTCPConnection.this);
        protected volatile Long shutdownTimestamp = null;
        private volatile boolean instantShutdown;
        private boolean shouldBundleAndDefer;
        void init() {
            shutdownDone.init();
            shutdownTimestamp = null;
            if (unacknowledgedStanzas != null) {
                drainWriterQueueToUnacknowledgedStanzas();
            }
            queue.start();
            Async.go(new Runnable() {
                @Override
                public void run() {
                    writePackets();
                }
            }, "Smack Packet Writer (" + getConnectionCounter() + ")");
        }
        private boolean done() {
            return shutdownTimestamp != null;
        }
        protected void throwNotConnectedExceptionIfDoneAndResumptionNotPossible() throws NotConnectedException {
            if (done() && !isSmResumptionPossible()) {
                throw new NotConnectedException();
            }
        }
        protected void sendStreamElement(Element element) throws NotConnectedException {
            throwNotConnectedExceptionIfDoneAndResumptionNotPossible();
            boolean enqueued = false;
            while (!enqueued) {
                try {
                    queue.put(element);
                    enqueued = true;
                }
                catch (InterruptedException e) {
                    throwNotConnectedExceptionIfDoneAndResumptionNotPossible();
                    LOGGER.log(Level.WARNING, "Sending thread was interrupted", e);
                }
            }
        }
        void shutdown(boolean instant) {
            instantShutdown = instant;
            shutdownTimestamp = System.currentTimeMillis();
            queue.shutdown();
            try {
                shutdownDone.checkIfSuccessOrWait();
            }
            catch (NoResponseException e) {
                LOGGER.log(Level.WARNING, "shutdownDone was not marked as successful by the writer thread", e);
            }
        }
        private Element nextStreamElement() {
            if (queue.isEmpty()) {
                shouldBundleAndDefer = true;
            }
            Element packet = null;
            try {
                packet = queue.take();
            }
            catch (InterruptedException e) {
                if (!queue.isShutdown()) {
                    LOGGER.log(Level.WARNING, "Packet writer thread was interrupted. Don't do that. Use disconnect() instead.", e);
                }
            }
            return packet;
        }
        private void writePackets() {
            try {
                openStream();
                initalOpenStreamSend.reportSuccess();
                while (!done()) {
                    Element element = nextStreamElement();
                    if (element == null) {
                        continue;
                    }
                    final BundleAndDeferCallback localBundleAndDeferCallback = bundleAndDeferCallback;
                    if (localBundleAndDeferCallback != null && isAuthenticated() && shouldBundleAndDefer) {
                        shouldBundleAndDefer = false;
                        final AtomicBoolean bundlingAndDeferringStopped = new AtomicBoolean();
                        final int bundleAndDeferMillis = localBundleAndDeferCallback.getBundleAndDeferMillis(new BundleAndDefer(
                                        bundlingAndDeferringStopped));
                        if (bundleAndDeferMillis > 0) {
                            long remainingWait = bundleAndDeferMillis;
                            final long waitStart = System.currentTimeMillis();
                            synchronized (bundlingAndDeferringStopped) {
                                while (!bundlingAndDeferringStopped.get() && remainingWait > 0) {
                                    bundlingAndDeferringStopped.wait(remainingWait);
                                    remainingWait = bundleAndDeferMillis
                                                    - (System.currentTimeMillis() - waitStart);
                                }
                            }
                        }
                    }
                    Stanza packet = null;
                    if (element instanceof Stanza) {
                        packet = (Stanza) element;
                    }
                    else if (element instanceof Enable) {
                        unacknowledgedStanzas = new ArrayBlockingQueue<>(QUEUE_SIZE);
                    }
                    if (unacknowledgedStanzas != null && packet != null) {
                        if (unacknowledgedStanzas.size() == 0.8 * XMPPTCPConnection.QUEUE_SIZE) {
                            writer.write(AckRequest.INSTANCE.toXML().toString());
                            writer.flush();
                        }
                        try {
                            unacknowledgedStanzas.put(packet);
                        }
                        catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                    writer.write(element.toXML().toString());
                    if (queue.isEmpty()) {
                        writer.flush();
                    }
                    if (packet != null) {
                        firePacketSendingListeners(packet);
                    }
                }
                if (!instantShutdown) {
                    try {
                        while (!queue.isEmpty()) {
                            Element packet = queue.remove();
                            writer.write(packet.toXML().toString());
                        }
                        writer.flush();
                    }
                    catch (Exception e) {
                        LOGGER.log(Level.WARNING,
                                        "Exception flushing queue during shutdown, ignore and continue",
                                        e);
                    }
                    try {
                        writer.write("</stream:stream>");
                        writer.flush();
                    }
                    catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Exception writing closing stream element", e);
                    }
                    queue.clear();
                } else if (instantShutdown && isSmEnabled()) {
                    drainWriterQueueToUnacknowledgedStanzas();
                }
                try {
                    writer.close();
                }
                catch (Exception e) {
                }
            }
            catch (Exception e) {
                if (!(done() || isSocketClosed())) {
                    notifyConnectionError(e);
                } else {
                    LOGGER.log(Level.FINE, "Ignoring Exception in writePackets()", e);
                }
            } finally {
                LOGGER.fine("Reporting shutdownDone success in writer thread");
                shutdownDone.reportSuccess();
            }
        }
        private void drainWriterQueueToUnacknowledgedStanzas() {
            List<Element> elements = new ArrayList<Element>(queue.size());
            queue.drainTo(elements);
            for (Element element : elements) {
                if (element instanceof Stanza) {
                    unacknowledgedStanzas.add((Stanza) element);
                }
            }
        }
    }
    public static void setUseStreamManagementDefault(boolean useSmDefault) {
        XMPPTCPConnection.useSmDefault = useSmDefault;
    }
    @Deprecated
    public static void setUseStreamManagementResumptiodDefault(boolean useSmResumptionDefault) {
        setUseStreamManagementResumptionDefault(useSmResumptionDefault);
    }
    public static void setUseStreamManagementResumptionDefault(boolean useSmResumptionDefault) {
        if (useSmResumptionDefault) {
            setUseStreamManagementDefault(useSmResumptionDefault);
        }
        XMPPTCPConnection.useSmResumptionDefault = useSmResumptionDefault;
    }
    public void setUseStreamManagement(boolean useSm) {
        this.useSm = useSm;
    }
    public void setUseStreamManagementResumption(boolean useSmResumption) {
        if (useSmResumption) {
            setUseStreamManagement(useSmResumption);
        }
        this.useSmResumption = useSmResumption;
    }
    public void setPreferredResumptionTime(int resumptionTime) {
        smClientMaxResumptionTime = resumptionTime;
    }
    public boolean addRequestAckPredicate(StanzaFilter predicate) {
        synchronized (requestAckPredicates) {
            return requestAckPredicates.add(predicate);
        }
    }
    public boolean removeRequestAckPredicate(StanzaFilter predicate) {
        synchronized (requestAckPredicates) {
            return requestAckPredicates.remove(predicate);
        }
    }
    public void removeAllRequestAckPredicates() {
        synchronized (requestAckPredicates) {
            requestAckPredicates.clear();
        }
    }
    public void requestSmAcknowledgement() throws StreamManagementNotEnabledException, NotConnectedException {
        if (!isSmEnabled()) {
            throw new StreamManagementException.StreamManagementNotEnabledException();
        }
        requestSmAcknowledgementInternal();
    }
    private void requestSmAcknowledgementInternal() throws NotConnectedException {
        packetWriter.sendStreamElement(AckRequest.INSTANCE);
    }
    public void sendSmAcknowledgement() throws StreamManagementNotEnabledException, NotConnectedException {
        if (!isSmEnabled()) {
            throw new StreamManagementException.StreamManagementNotEnabledException();
        }
        sendSmAcknowledgementInternal();
    }
    private void sendSmAcknowledgementInternal() throws NotConnectedException {
        packetWriter.sendStreamElement(new AckAnswer(clientHandledStanzasCount));
    }
    public void addStanzaAcknowledgedListener(StanzaListener listener) {
        stanzaAcknowledgedListeners.add(listener);
    }
    public boolean removeStanzaAcknowledgedListener(StanzaListener listener) {
        return stanzaAcknowledgedListeners.remove(listener);
    }
    public void removeAllStanzaAcknowledgedListeners() {
        stanzaAcknowledgedListeners.clear();
    }
    public StanzaListener addStanzaIdAcknowledgedListener(final String id, StanzaListener listener) throws StreamManagementNotEnabledException {
        if (!smWasEnabledAtLeastOnce) {
            throw new StreamManagementException.StreamManagementNotEnabledException();
        }
        final int removeAfterSeconds = Math.min(getMaxSmResumptionTime(), 12 * 60 * 60);
        schedule(new Runnable() {
            @Override
            public void run() {
                stanzaIdAcknowledgedListeners.remove(id);
            }
        }, removeAfterSeconds, TimeUnit.SECONDS);
        return stanzaIdAcknowledgedListeners.put(id, listener);
    }
    public StanzaListener removeStanzaIdAcknowledgedListener(String id) {
        return stanzaIdAcknowledgedListeners.remove(id);
    }
    public void removeAllStanzaIdAcknowledgedListeners() {
        stanzaIdAcknowledgedListeners.clear();
    }
    public boolean isSmAvailable() {
        return hasFeature(StreamManagementFeature.ELEMENT, StreamManagement.NAMESPACE);
    }
    public boolean isSmEnabled() {
        return smEnabledSyncPoint.wasSuccessful();
    }
    public boolean streamWasResumed() {
        return smResumedSyncPoint.wasSuccessful();
    }
    public boolean isDisconnectedButSmResumptionPossible() {
        return disconnectedButResumeable && isSmResumptionPossible();
    }
    public boolean isSmResumptionPossible() {
        if (smSessionId == null)
            return false;
        final Long shutdownTimestamp = packetWriter.shutdownTimestamp;
        if (shutdownTimestamp == null) {
            return true;
        }
        long current = System.currentTimeMillis();
        long maxResumptionMillies = ((long) getMaxSmResumptionTime()) * 1000;
        if (current > shutdownTimestamp + maxResumptionMillies) {
            return false;
        } else {
            return true;
        }
    }
    private void dropSmState() {
        smSessionId = null;
        unacknowledgedStanzas = null;
    }
    public int getMaxSmResumptionTime() {
        int clientResumptionTime = smClientMaxResumptionTime > 0 ? smClientMaxResumptionTime : Integer.MAX_VALUE;
        int serverResumptionTime = smServerMaxResumptimTime > 0 ? smServerMaxResumptimTime : Integer.MAX_VALUE;
        return Math.min(clientResumptionTime, serverResumptionTime);
    }
    private void processHandledCount(long handledCount) throws StreamManagementCounterError {
        long ackedStanzasCount = SMUtils.calculateDelta(handledCount, serverHandledStanzasCount);
        final List<Stanza> ackedStanzas = new ArrayList<Stanza>(
                        ackedStanzasCount <= Integer.MAX_VALUE ? (int) ackedStanzasCount
                                        : Integer.MAX_VALUE);
        for (long i = 0; i < ackedStanzasCount; i++) {
            Stanza ackedStanza = unacknowledgedStanzas.poll();
            if (ackedStanza == null) {
                throw new StreamManagementCounterError(handledCount, serverHandledStanzasCount,
                                ackedStanzasCount, ackedStanzas);
            }
            ackedStanzas.add(ackedStanza);
        }
        boolean atLeastOneStanzaAcknowledgedListener = false;
        if (!stanzaAcknowledgedListeners.isEmpty()) {
            atLeastOneStanzaAcknowledgedListener = true;
        }
        else {
            for (Stanza ackedStanza : ackedStanzas) {
                String id = ackedStanza.getStanzaId();
                if (id != null && stanzaIdAcknowledgedListeners.containsKey(id)) {
                    atLeastOneStanzaAcknowledgedListener = true;
                    break;
                }
            }
        }
        if (atLeastOneStanzaAcknowledgedListener) {
            asyncGo(new Runnable() {
                @Override
                public void run() {
                    for (Stanza ackedStanza : ackedStanzas) {
                        for (StanzaListener listener : stanzaAcknowledgedListeners) {
                            try {
                                listener.processPacket(ackedStanza);
                            }
                            catch (NotConnectedException e) {
                                LOGGER.log(Level.FINER, "Received not connected exception", e);
                            }
                        }
                        String id = ackedStanza.getStanzaId();
                        if (StringUtils.isNullOrEmpty(id)) {
                            continue;
                        }
                        StanzaListener listener = stanzaIdAcknowledgedListeners.remove(id);
                        if (listener != null) {
                            try {
                                listener.processPacket(ackedStanza);
                            }
                            catch (NotConnectedException e) {
                                LOGGER.log(Level.FINER, "Received not connected exception", e);
                            }
                        }
                    }
                }
            });
        }
        serverHandledStanzasCount = handledCount;
    }
    public static void setDefaultBundleAndDeferCallback(BundleAndDeferCallback defaultBundleAndDeferCallback) {
        XMPPTCPConnection.defaultBundleAndDeferCallback = defaultBundleAndDeferCallback;
    }
    public void setBundleandDeferCallback(BundleAndDeferCallback bundleAndDeferCallback) {
        this.bundleAndDeferCallback = bundleAndDeferCallback;
    }
}
