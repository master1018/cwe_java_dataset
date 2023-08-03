
package org.jboss.netty.handler.ssl;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.DefaultChannelFuture;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.jboss.netty.util.internal.DetectionUtil;
import org.jboss.netty.util.internal.NonReentrantLock;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.regex.Pattern;
import static org.jboss.netty.channel.Channels.*;
public class SslHandler extends FrameDecoder
                        implements ChannelDownstreamHandler {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(SslHandler.class);
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);
    private static final Pattern IGNORABLE_CLASS_IN_STACK = Pattern.compile(
            "^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
    private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile(
            "^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", Pattern.CASE_INSENSITIVE);
    private static SslBufferPool defaultBufferPool;
    public static synchronized SslBufferPool getDefaultBufferPool() {
        if (defaultBufferPool == null) {
            defaultBufferPool = new SslBufferPool();
        }
        return defaultBufferPool;
    }
    private volatile ChannelHandlerContext ctx;
    private final SSLEngine engine;
    private final SslBufferPool bufferPool;
    private final Executor delegatedTaskExecutor;
    private final boolean startTls;
    private volatile boolean enableRenegotiation = true;
    final Object handshakeLock = new Object();
    private boolean handshaking;
    private volatile boolean handshaken;
    private volatile ChannelFuture handshakeFuture;
    @SuppressWarnings("UnusedDeclaration")
    private volatile int sentFirstMessage;
    @SuppressWarnings("UnusedDeclaration")
    private volatile int sentCloseNotify;
    @SuppressWarnings("UnusedDeclaration")
    private volatile int closedOutboundAndChannel;
    private static final AtomicIntegerFieldUpdater<SslHandler> SENT_FIRST_MESSAGE_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(SslHandler.class, "sentFirstMessage");
    private static final AtomicIntegerFieldUpdater<SslHandler> SENT_CLOSE_NOTIFY_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(SslHandler.class, "sentCloseNotify");
    private static final AtomicIntegerFieldUpdater<SslHandler> CLOSED_OUTBOUND_AND_CHANNEL_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(SslHandler.class, "closedOutboundAndChannel");
    int ignoreClosedChannelException;
    final Object ignoreClosedChannelExceptionLock = new Object();
    private final Queue<PendingWrite> pendingUnencryptedWrites = new LinkedList<PendingWrite>();
    private final NonReentrantLock pendingUnencryptedWritesLock = new NonReentrantLock();
    private final Queue<MessageEvent> pendingEncryptedWrites = new ConcurrentLinkedQueue<MessageEvent>();
    private final NonReentrantLock pendingEncryptedWritesLock = new NonReentrantLock();
    private volatile boolean issueHandshake;
    private volatile boolean writeBeforeHandshakeDone;
    private final SSLEngineInboundCloseFuture sslEngineCloseFuture = new SSLEngineInboundCloseFuture();
    private boolean closeOnSslException;
    private int packetLength;
    private final Timer timer;
    private final long handshakeTimeoutInMillis;
    private Timeout handshakeTimeout;
    public SslHandler(SSLEngine engine) {
        this(engine, getDefaultBufferPool(), false, null, 0);
    }
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool) {
        this(engine, bufferPool, false, null, 0);
    }
    public SslHandler(SSLEngine engine, boolean startTls) {
        this(engine, getDefaultBufferPool(), startTls);
    }
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool, boolean startTls) {
        this(engine, bufferPool, startTls, null, 0);
    }
    @SuppressWarnings("deprecation")
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool, boolean startTls,
                      Timer timer, long handshakeTimeoutInMillis) {
        this(engine, bufferPool, startTls, ImmediateExecutor.INSTANCE, timer, handshakeTimeoutInMillis);
    }
    @Deprecated
    public SslHandler(SSLEngine engine, Executor delegatedTaskExecutor) {
        this(engine, getDefaultBufferPool(), delegatedTaskExecutor);
    }
    @Deprecated
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool, Executor delegatedTaskExecutor) {
        this(engine, bufferPool, false, delegatedTaskExecutor);
    }
    @Deprecated
    public SslHandler(SSLEngine engine, boolean startTls, Executor delegatedTaskExecutor) {
        this(engine, getDefaultBufferPool(), startTls, delegatedTaskExecutor);
    }
    @Deprecated
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool, boolean startTls, Executor delegatedTaskExecutor) {
        this(engine, bufferPool, startTls, delegatedTaskExecutor, null, 0);
    }
    @Deprecated
    public SslHandler(SSLEngine engine, SslBufferPool bufferPool, boolean startTls, Executor delegatedTaskExecutor,
                      Timer timer, long handshakeTimeoutInMillis) {
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        if (bufferPool == null) {
            throw new NullPointerException("bufferPool");
        }
        if (delegatedTaskExecutor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        if (timer == null && handshakeTimeoutInMillis > 0) {
            throw new IllegalArgumentException("No Timer was given but a handshakeTimeoutInMillis, need both or none");
        }
        this.engine = engine;
        this.bufferPool = bufferPool;
        this.delegatedTaskExecutor = delegatedTaskExecutor;
        this.startTls = startTls;
        this.timer = timer;
        this.handshakeTimeoutInMillis = handshakeTimeoutInMillis;
    }
    public SSLEngine getEngine() {
        return engine;
    }
    public ChannelFuture handshake() {
        synchronized (handshakeLock) {
            if (handshaken && !isEnableRenegotiation()) {
                throw new IllegalStateException("renegotiation disabled");
            }
            final ChannelHandlerContext ctx = this.ctx;
            final Channel channel = ctx.getChannel();
            ChannelFuture handshakeFuture;
            Exception exception = null;
            if (handshaking) {
                return this.handshakeFuture;
            }
            handshaking = true;
            try {
                engine.beginHandshake();
                runDelegatedTasks();
                handshakeFuture = this.handshakeFuture = future(channel);
                if (handshakeTimeoutInMillis > 0) {
                    handshakeTimeout = timer.newTimeout(new TimerTask() {
                            public void run(Timeout timeout) throws Exception {
                            ChannelFuture future = SslHandler.this.handshakeFuture;
                            if (future != null && future.isDone()) {
                                return;
                            }
                            setHandshakeFailure(channel, new SSLException("Handshake did not complete within " +
                                            handshakeTimeoutInMillis + "ms"));
                        }
                        }, handshakeTimeoutInMillis, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                handshakeFuture = this.handshakeFuture = failedFuture(channel, e);
                exception = e;
            }
            if (exception == null) { 
                try {
                    final ChannelFuture hsFuture = handshakeFuture;
                    wrapNonAppData(ctx, channel).addListener(new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                Throwable cause = future.getCause();
                                hsFuture.setFailure(cause);
                                fireExceptionCaught(ctx, cause);
                                if (closeOnSslException) {
                                    Channels.close(ctx, future(channel));
                                }
                            }
                        }
                    });
                } catch (SSLException e) {
                    handshakeFuture.setFailure(e);
                    fireExceptionCaught(ctx, e);
                    if (closeOnSslException) {
                        Channels.close(ctx, future(channel));
                    }
                }
            } else { 
                fireExceptionCaught(ctx, exception);
                if (closeOnSslException) {
                    Channels.close(ctx, future(channel));
                }
            }
            return handshakeFuture;
        }
    }
    @Deprecated
    public ChannelFuture handshake(@SuppressWarnings("unused") Channel channel) {
        return handshake();
    }
    public ChannelFuture close() {
        ChannelHandlerContext ctx = this.ctx;
        Channel channel = ctx.getChannel();
        try {
            engine.closeOutbound();
            return wrapNonAppData(ctx, channel);
        } catch (SSLException e) {
            fireExceptionCaught(ctx, e);
            if (closeOnSslException) {
                Channels.close(ctx, future(channel));
            }
            return failedFuture(channel, e);
        }
    }
    @Deprecated
    public ChannelFuture close(@SuppressWarnings("unused") Channel channel) {
        return close();
    }
    public boolean isEnableRenegotiation() {
        return enableRenegotiation;
    }
    public void setEnableRenegotiation(boolean enableRenegotiation) {
        this.enableRenegotiation = enableRenegotiation;
    }
    public void setIssueHandshake(boolean issueHandshake) {
        this.issueHandshake = issueHandshake;
    }
    public boolean isIssueHandshake() {
        return issueHandshake;
    }
    public ChannelFuture getSSLEngineInboundCloseFuture() {
        return sslEngineCloseFuture;
    }
    public long getHandshakeTimeout() {
        return handshakeTimeoutInMillis;
    }
    public void setCloseOnSSLException(boolean closeOnSslException) {
        if (ctx != null) {
            throw new IllegalStateException("Can only get changed before attached to ChannelPipeline");
        }
        this.closeOnSslException = closeOnSslException;
    }
    public boolean getCloseOnSSLException() {
        return closeOnSslException;
    }
    public void handleDownstream(
            final ChannelHandlerContext context, final ChannelEvent evt) throws Exception {
        if (evt instanceof ChannelStateEvent) {
            ChannelStateEvent e = (ChannelStateEvent) evt;
            switch (e.getState()) {
            case OPEN:
            case CONNECTED:
            case BOUND:
                if (Boolean.FALSE.equals(e.getValue()) || e.getValue() == null) {
                    closeOutboundAndChannel(context, e);
                    return;
                }
            }
        }
        if (!(evt instanceof MessageEvent)) {
            context.sendDownstream(evt);
            return;
        }
        MessageEvent e = (MessageEvent) evt;
        if (!(e.getMessage() instanceof ChannelBuffer)) {
            context.sendDownstream(evt);
            return;
        }
        if (startTls && SENT_FIRST_MESSAGE_UPDATER.compareAndSet(this, 0, 1)) {
            context.sendDownstream(evt);
            return;
        }
        ChannelBuffer msg = (ChannelBuffer) e.getMessage();
        PendingWrite pendingWrite;
        if (msg.readable()) {
            pendingWrite = new PendingWrite(evt.getFuture(), msg.toByteBuffer(msg.readerIndex(), msg.readableBytes()));
        } else {
            pendingWrite = new PendingWrite(evt.getFuture(), null);
        }
        pendingUnencryptedWritesLock.lock();
        try {
            pendingUnencryptedWrites.add(pendingWrite);
        } finally {
            pendingUnencryptedWritesLock.unlock();
        }
        if (handshakeFuture == null || !handshakeFuture.isDone()) {
            writeBeforeHandshakeDone = true;
        }
        wrap(context, evt.getChannel());
    }
    private void cancelHandshakeTimeout() {
        if (handshakeTimeout != null) {
            handshakeTimeout.cancel();
        }
    }
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        synchronized (handshakeLock) {
            if (handshaking) {
                cancelHandshakeTimeout();
                handshakeFuture.setFailure(new ClosedChannelException());
            }
        }
        try {
            super.channelDisconnected(ctx, e);
        } finally {
            unwrapNonAppData(ctx, e.getChannel());
            closeEngine();
        }
    }
    private void closeEngine() {
        engine.closeOutbound();
        if (sentCloseNotify == 0 && handshaken) {
            try {
                engine.closeInbound();
            } catch (SSLException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to clean up SSLEngine.", ex);
                }
            }
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
            if (cause instanceof ClosedChannelException) {
                synchronized (ignoreClosedChannelExceptionLock) {
                    if (ignoreClosedChannelException > 0) {
                        ignoreClosedChannelException --;
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                    "Swallowing an exception raised while " +
                                    "writing non-app data", cause);
                        }
                        return;
                    }
                }
            } else {
                if (ignoreException(cause)) {
                    return;
                }
            }
        }
        ctx.sendUpstream(e);
    }
    private boolean ignoreException(Throwable t) {
        if (!(t instanceof SSLException) && t instanceof IOException && engine.isOutboundDone()) {
            String message = String.valueOf(t.getMessage()).toLowerCase();
            if (IGNORABLE_ERROR_MESSAGE.matcher(message).matches()) {
                return true;
            }
            StackTraceElement[] elements = t.getStackTrace();
            for (StackTraceElement element: elements) {
                String classname = element.getClassName();
                String methodname = element.getMethodName();
                if (classname.startsWith("org.jboss.netty.")) {
                    continue;
                }
                if (!"read".equals(methodname)) {
                    continue;
                }
                if (IGNORABLE_CLASS_IN_STACK.matcher(classname).matches()) {
                    return true;
                }
                try {
                    Class<?> clazz = getClass().getClassLoader().loadClass(classname);
                    if (SocketChannel.class.isAssignableFrom(clazz)
                            || DatagramChannel.class.isAssignableFrom(clazz)) {
                        return true;
                    }
                    if (DetectionUtil.javaVersion() >= 7
                            && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                        return true;
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }
        return false;
    }
    public static boolean isEncrypted(ChannelBuffer buffer) {
        return getEncryptedPacketLength(buffer, buffer.readerIndex()) != -1;
    }
    private static int getEncryptedPacketLength(ChannelBuffer buffer, int offset) {
        int packetLength = 0;
        boolean tls;
        switch (buffer.getUnsignedByte(offset)) {
            case 20:  
            case 21:  
            case 22:  
            case 23:  
                tls = true;
                break;
            default:
                tls = false;
        }
        if (tls) {
            int majorVersion = buffer.getUnsignedByte(offset + 1);
            if (majorVersion == 3) {
                packetLength = (getShort(buffer, offset + 3) & 0xFFFF) + 5;
                if (packetLength <= 5) {
                    tls = false;
                }
            } else {
                tls = false;
            }
        }
        if (!tls) {
            boolean sslv2 = true;
            int headerLength = (buffer.getUnsignedByte(offset) & 0x80) != 0 ? 2 : 3;
            int majorVersion = buffer.getUnsignedByte(offset + headerLength + 1);
            if (majorVersion == 2 || majorVersion == 3) {
                if (headerLength == 2) {
                    packetLength = (getShort(buffer, offset) & 0x7FFF) + 2;
                } else {
                    packetLength = (getShort(buffer, offset) & 0x3FFF) + 3;
                }
                if (packetLength <= headerLength) {
                    sslv2 = false;
                }
            } else {
                sslv2 = false;
            }
            if (!sslv2) {
                return -1;
            }
        }
        return packetLength;
    }
    @Override
    protected Object decode(
            final ChannelHandlerContext ctx, Channel channel, ChannelBuffer in) throws Exception {
        final int startOffset = in.readerIndex();
        final int endOffset = in.writerIndex();
        int offset = startOffset;
        int totalLength = 0;
        if (packetLength > 0) {
            if (endOffset - startOffset < packetLength) {
                return null;
            } else {
                offset += packetLength;
                totalLength = packetLength;
                packetLength = 0;
            }
        }
        boolean nonSslRecord = false;
        while (totalLength < OpenSslEngine.MAX_ENCRYPTED_PACKET_LENGTH) {
            final int readableBytes = endOffset - offset;
            if (readableBytes < 5) {
                break;
            }
            final int packetLength = getEncryptedPacketLength(in, offset);
            if (packetLength == -1) {
                nonSslRecord = true;
                break;
            }
            assert packetLength > 0;
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                break;
            }
            int newTotalLength = totalLength + packetLength;
            if (newTotalLength > OpenSslEngine.MAX_ENCRYPTED_PACKET_LENGTH) {
                break;
            }
            offset += packetLength;
            totalLength = newTotalLength;
        }
        ChannelBuffer unwrapped = null;
        if (totalLength > 0) {
            final ByteBuffer inNetBuf = in.toByteBuffer(in.readerIndex(), totalLength);
            unwrapped = unwrap(ctx, channel, in, inNetBuf, totalLength);
            assert !inNetBuf.hasRemaining() || engine.isInboundDone();
        }
        if (nonSslRecord) {
            NotSslRecordException e = new NotSslRecordException(
                    "not an SSL/TLS record: " + ChannelBuffers.hexDump(in));
            in.skipBytes(in.readableBytes());
            if (closeOnSslException) {
                fireExceptionCaught(ctx, e);
                Channels.close(ctx, future(channel));
                return null;
            } else {
                throw e;
            }
        }
        return unwrapped;
    }
    private static short getShort(ChannelBuffer buf, int offset) {
        return (short) (buf.getByte(offset) << 8 | buf.getByte(offset + 1) & 0xFF);
    }
    private void wrap(ChannelHandlerContext context, Channel channel) throws SSLException {
        ChannelBuffer msg;
        ByteBuffer outNetBuf = bufferPool.acquireBuffer();
        boolean success = true;
        boolean offered = false;
        boolean needsUnwrap = false;
        PendingWrite pendingWrite = null;
        try {
            loop:
            for (;;) {
                pendingUnencryptedWritesLock.lock();
                try {
                    pendingWrite = pendingUnencryptedWrites.peek();
                    if (pendingWrite == null) {
                        break;
                    }
                    ByteBuffer outAppBuf = pendingWrite.outAppBuf;
                    if (outAppBuf == null) {
                        pendingUnencryptedWrites.remove();
                        offerEncryptedWriteRequest(
                                new DownstreamMessageEvent(
                                        channel, pendingWrite.future,
                                        ChannelBuffers.EMPTY_BUFFER,
                                        channel.getRemoteAddress()));
                        offered = true;
                    } else {
                        synchronized (handshakeLock) {
                            SSLEngineResult result = null;
                            try {
                                result = engine.wrap(outAppBuf, outNetBuf);
                            } finally {
                                if (!outAppBuf.hasRemaining()) {
                                    pendingUnencryptedWrites.remove();
                                }
                            }
                            if (result.bytesProduced() > 0) {
                                outNetBuf.flip();
                                int remaining = outNetBuf.remaining();
                                msg = ctx.getChannel().getConfig().getBufferFactory().getBuffer(remaining);
                                msg.writeBytes(outNetBuf);
                                outNetBuf.clear();
                                ChannelFuture future;
                                if (pendingWrite.outAppBuf.hasRemaining()) {
                                    future = succeededFuture(channel);
                                } else {
                                    future = pendingWrite.future;
                                }
                                MessageEvent encryptedWrite = new DownstreamMessageEvent(
                                        channel, future, msg, channel.getRemoteAddress());
                                offerEncryptedWriteRequest(encryptedWrite);
                                offered = true;
                            } else if (result.getStatus() == Status.CLOSED) {
                                success = false;
                                break;
                            } else {
                                final HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                                handleRenegotiation(handshakeStatus);
                                switch (handshakeStatus) {
                                case NEED_WRAP:
                                    if (outAppBuf.hasRemaining()) {
                                        break;
                                    } else {
                                        break loop;
                                    }
                                case NEED_UNWRAP:
                                    needsUnwrap = true;
                                    break loop;
                                case NEED_TASK:
                                    runDelegatedTasks();
                                    break;
                                case FINISHED:
                                    setHandshakeSuccess(channel);
                                    if (result.getStatus() == Status.CLOSED) {
                                        success = false;
                                    }
                                    break loop;
                                case NOT_HANDSHAKING:
                                    setHandshakeSuccessIfStillHandshaking(channel);
                                    if (result.getStatus() == Status.CLOSED) {
                                        success = false;
                                    }
                                    break loop;
                                default:
                                    throw new IllegalStateException(
                                            "Unknown handshake status: " +
                                            handshakeStatus);
                                }
                            }
                        }
                    }
                } finally {
                    pendingUnencryptedWritesLock.unlock();
                }
            }
        } catch (SSLException e) {
            success = false;
            setHandshakeFailure(channel, e);
            throw e;
        } finally {
            bufferPool.releaseBuffer(outNetBuf);
            if (offered) {
                flushPendingEncryptedWrites(context);
            }
            if (!success) {
                IllegalStateException cause =
                    new IllegalStateException("SSLEngine already closed");
                if (pendingWrite != null) {
                    pendingWrite.future.setFailure(cause);
                }
                for (;;) {
                    pendingUnencryptedWritesLock.lock();
                    try {
                        pendingWrite = pendingUnencryptedWrites.poll();
                        if (pendingWrite == null) {
                            break;
                        }
                    } finally {
                        pendingUnencryptedWritesLock.unlock();
                    }
                    pendingWrite.future.setFailure(cause);
                }
            }
        }
        if (needsUnwrap) {
            unwrapNonAppData(ctx, channel);
        }
    }
    private void offerEncryptedWriteRequest(MessageEvent encryptedWrite) {
        final boolean locked = pendingEncryptedWritesLock.tryLock();
        try {
            pendingEncryptedWrites.add(encryptedWrite);
        } finally {
            if (locked) {
                pendingEncryptedWritesLock.unlock();
            }
        }
    }
    private void flushPendingEncryptedWrites(ChannelHandlerContext ctx) {
        while (!pendingEncryptedWrites.isEmpty()) {
            if (!pendingEncryptedWritesLock.tryLock()) {
                return;
            }
            try {
                MessageEvent e;
                while ((e = pendingEncryptedWrites.poll()) != null) {
                    ctx.sendDownstream(e);
                }
            } finally {
                pendingEncryptedWritesLock.unlock();
            }
        }
    }
    private ChannelFuture wrapNonAppData(ChannelHandlerContext ctx, Channel channel) throws SSLException {
        ChannelFuture future = null;
        ByteBuffer outNetBuf = bufferPool.acquireBuffer();
        SSLEngineResult result;
        try {
            for (;;) {
                synchronized (handshakeLock) {
                    result = engine.wrap(EMPTY_BUFFER, outNetBuf);
                }
                if (result.bytesProduced() > 0) {
                    outNetBuf.flip();
                    ChannelBuffer msg =
                            ctx.getChannel().getConfig().getBufferFactory().getBuffer(outNetBuf.remaining());
                    msg.writeBytes(outNetBuf);
                    outNetBuf.clear();
                    future = future(channel);
                    future.addListener(new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture future)
                                throws Exception {
                            if (future.getCause() instanceof ClosedChannelException) {
                                synchronized (ignoreClosedChannelExceptionLock) {
                                    ignoreClosedChannelException ++;
                                }
                            }
                        }
                    });
                    write(ctx, future, msg);
                }
                final HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                handleRenegotiation(handshakeStatus);
                switch (handshakeStatus) {
                case FINISHED:
                    setHandshakeSuccess(channel);
                    runDelegatedTasks();
                    break;
                case NEED_TASK:
                    runDelegatedTasks();
                    break;
                case NEED_UNWRAP:
                    if (!Thread.holdsLock(handshakeLock)) {
                        unwrapNonAppData(ctx, channel);
                    }
                    break;
                case NOT_HANDSHAKING:
                    if (setHandshakeSuccessIfStillHandshaking(channel)) {
                        runDelegatedTasks();
                    }
                    break;
                case NEED_WRAP:
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected handshake status: " + handshakeStatus);
                }
                if (result.bytesProduced() == 0) {
                    break;
                }
            }
        } catch (SSLException e) {
            setHandshakeFailure(channel, e);
            throw e;
        } finally {
            bufferPool.releaseBuffer(outNetBuf);
        }
        if (future == null) {
            future = succeededFuture(channel);
        }
        return future;
    }
    private void unwrapNonAppData(ChannelHandlerContext ctx, Channel channel) throws SSLException {
        unwrap(ctx, channel, ChannelBuffers.EMPTY_BUFFER, EMPTY_BUFFER, -1);
    }
    private ChannelBuffer unwrap(
            ChannelHandlerContext ctx, Channel channel,
            ChannelBuffer nettyInNetBuf, ByteBuffer nioInNetBuf,
            int initialNettyOutAppBufCapacity) throws SSLException {
        final int nettyInNetBufStartOffset = nettyInNetBuf.readerIndex();
        final int nioInNetBufStartOffset = nioInNetBuf.position();
        final ByteBuffer nioOutAppBuf = bufferPool.acquireBuffer();
        ChannelBuffer nettyOutAppBuf = null;
        try {
            boolean needsWrap = false;
            for (;;) {
                SSLEngineResult result;
                boolean needsHandshake = false;
                synchronized (handshakeLock) {
                    if (!handshaken && !handshaking &&
                        !engine.getUseClientMode() &&
                        !engine.isInboundDone() && !engine.isOutboundDone()) {
                        needsHandshake = true;
                    }
                }
                if (needsHandshake) {
                    handshake();
                }
                synchronized (handshakeLock) {
                    for (;;) {
                        try {
                            result = engine.unwrap(nioInNetBuf, nioOutAppBuf);
                            switch (result.getStatus()) {
                                case CLOSED:
                                    sslEngineCloseFuture.setClosed();
                                    break;
                                case BUFFER_OVERFLOW:
                                    continue;
                            }
                            break;
                        } finally {
                            nioOutAppBuf.flip();
                            nettyInNetBuf.readerIndex(
                                    nettyInNetBufStartOffset + nioInNetBuf.position() - nioInNetBufStartOffset);
                            if (nioOutAppBuf.hasRemaining()) {
                                if (nettyOutAppBuf == null) {
                                    ChannelBufferFactory factory = ctx.getChannel().getConfig().getBufferFactory();
                                    nettyOutAppBuf = factory.getBuffer(initialNettyOutAppBufCapacity);
                                }
                                nettyOutAppBuf.writeBytes(nioOutAppBuf);
                            }
                            nioOutAppBuf.clear();
                        }
                    }
                    final HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                    handleRenegotiation(handshakeStatus);
                    switch (handshakeStatus) {
                    case NEED_UNWRAP:
                        break;
                    case NEED_WRAP:
                        wrapNonAppData(ctx, channel);
                        break;
                    case NEED_TASK:
                        runDelegatedTasks();
                        break;
                    case FINISHED:
                        setHandshakeSuccess(channel);
                        needsWrap = true;
                        continue;
                    case NOT_HANDSHAKING:
                        if (setHandshakeSuccessIfStillHandshaking(channel)) {
                            needsWrap = true;
                            continue;
                        }
                        if (writeBeforeHandshakeDone) {
                            writeBeforeHandshakeDone = false;
                            needsWrap = true;
                        }
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unknown handshake status: " + handshakeStatus);
                    }
                    if (result.getStatus() == Status.BUFFER_UNDERFLOW ||
                        result.bytesConsumed() == 0 && result.bytesProduced() == 0) {
                        break;
                    }
                }
            }
            if (needsWrap) {
                if (!Thread.holdsLock(handshakeLock) && !pendingEncryptedWritesLock.isHeldByCurrentThread()) {
                    wrap(ctx, channel);
                }
            }
        } catch (SSLException e) {
            setHandshakeFailure(channel, e);
            throw e;
        } finally {
            bufferPool.releaseBuffer(nioOutAppBuf);
        }
        if (nettyOutAppBuf != null && nettyOutAppBuf.readable()) {
            return nettyOutAppBuf;
        } else {
            return null;
        }
    }
    private void handleRenegotiation(HandshakeStatus handshakeStatus) {
        synchronized (handshakeLock) {
            if (handshakeStatus == HandshakeStatus.NOT_HANDSHAKING ||
                handshakeStatus == HandshakeStatus.FINISHED) {
                return;
            }
            if (!handshaken) {
                return;
            }
            final boolean renegotiate;
            if (handshaking) {
                return;
            }
            if (engine.isInboundDone() || engine.isOutboundDone()) {
                return;
            }
            if (isEnableRenegotiation()) {
                renegotiate = true;
            } else {
                renegotiate = false;
                handshaking = true;
            }
            if (renegotiate) {
                handshake();
            } else {
                fireExceptionCaught(
                        ctx, new SSLException(
                                "renegotiation attempted by peer; " +
                                "closing the connection"));
                Channels.close(ctx, succeededFuture(ctx.getChannel()));
            }
        }
    }
    private void runDelegatedTasks() {
        if (delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            for (;;) {
                final Runnable task;
                synchronized (handshakeLock) {
                    task = engine.getDelegatedTask();
                }
                if (task == null) {
                    break;
                }
                delegatedTaskExecutor.execute(task);
            }
        } else {
            final List<Runnable> tasks = new ArrayList<Runnable>(2);
            for (;;) {
                final Runnable task;
                synchronized (handshakeLock) {
                    task = engine.getDelegatedTask();
                }
                if (task == null) {
                    break;
                }
                tasks.add(task);
            }
            if (tasks.isEmpty()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            delegatedTaskExecutor.execute(new Runnable() {
                public void run() {
                    try {
                        for (Runnable task: tasks) {
                            task.run();
                        }
                    } catch (Exception e) {
                        fireExceptionCaught(ctx, e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
            boolean interrupted = false;
            while (latch.getCount() != 0) {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    private boolean setHandshakeSuccessIfStillHandshaking(Channel channel) {
        if (handshaking && !handshakeFuture.isDone()) {
            setHandshakeSuccess(channel);
            return true;
        }
        return false;
    }
    private void setHandshakeSuccess(Channel channel) {
        synchronized (handshakeLock) {
            handshaking = false;
            handshaken = true;
            if (handshakeFuture == null) {
                handshakeFuture = future(channel);
            }
            cancelHandshakeTimeout();
        }
        if (logger.isDebugEnabled()) {
            logger.debug(channel + " HANDSHAKEN: " + engine.getSession().getCipherSuite());
        }
        handshakeFuture.setSuccess();
    }
    private void setHandshakeFailure(Channel channel, SSLException cause) {
        synchronized (handshakeLock) {
            if (!handshaking) {
                return;
            }
            handshaking = false;
            handshaken = false;
            if (handshakeFuture == null) {
                handshakeFuture = future(channel);
            }
            cancelHandshakeTimeout();
            engine.closeOutbound();
            try {
                engine.closeInbound();
            } catch (SSLException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "SSLEngine.closeInbound() raised an exception after " +
                            "a handshake failure.", e);
                }
            }
        }
        handshakeFuture.setFailure(cause);
        if (closeOnSslException) {
            Channels.close(ctx, future(channel));
        }
    }
    private void closeOutboundAndChannel(
            final ChannelHandlerContext context, final ChannelStateEvent e) {
        if (!e.getChannel().isConnected()) {
            context.sendDownstream(e);
            return;
        }
        if (!CLOSED_OUTBOUND_AND_CHANNEL_UPDATER.compareAndSet(this, 0, 1)) {
            e.getChannel().getCloseFuture().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    context.sendDownstream(e);
                }
            });
            return;
        }
        boolean passthrough = true;
        try {
            try {
                unwrapNonAppData(ctx, e.getChannel());
            } catch (SSLException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to unwrap before sending a close_notify message", ex);
                }
            }
            if (!engine.isOutboundDone()) {
                if (SENT_CLOSE_NOTIFY_UPDATER.compareAndSet(this, 0, 1)) {
                    engine.closeOutbound();
                    try {
                        ChannelFuture closeNotifyFuture = wrapNonAppData(context, e.getChannel());
                        closeNotifyFuture.addListener(
                                new ClosingChannelFutureListener(context, e));
                        passthrough = false;
                    } catch (SSLException ex) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Failed to encode a close_notify message", ex);
                        }
                    }
                }
            }
        } finally {
            if (passthrough) {
                context.sendDownstream(e);
            }
        }
    }
    private static final class PendingWrite {
        final ChannelFuture future;
        final ByteBuffer outAppBuf;
        PendingWrite(ChannelFuture future, ByteBuffer outAppBuf) {
            this.future = future;
            this.outAppBuf = outAppBuf;
        }
    }
    private static final class ClosingChannelFutureListener implements ChannelFutureListener {
        private final ChannelHandlerContext context;
        private final ChannelStateEvent e;
        ClosingChannelFutureListener(
                ChannelHandlerContext context, ChannelStateEvent e) {
            this.context = context;
            this.e = e;
        }
        public void operationComplete(ChannelFuture closeNotifyFuture) throws Exception {
            if (!(closeNotifyFuture.getCause() instanceof ClosedChannelException)) {
                Channels.close(context, e.getFuture());
            } else {
                e.getFuture().setSuccess();
            }
        }
    }
    @Override
    public void beforeAdd(ChannelHandlerContext ctx) throws Exception {
        super.beforeAdd(ctx);
        this.ctx = ctx;
    }
    @Override
    public void afterRemove(ChannelHandlerContext ctx) throws Exception {
        closeEngine();
        Throwable cause = null;
        for (;;) {
            PendingWrite pw = pendingUnencryptedWrites.poll();
            if (pw == null) {
                break;
            }
            if (cause == null) {
                cause = new IOException("Unable to write data");
            }
            pw.future.setFailure(cause);
        }
        for (;;) {
            MessageEvent ev = pendingEncryptedWrites.poll();
            if (ev == null) {
                break;
            }
            if (cause == null) {
                cause = new IOException("Unable to write data");
            }
            ev.getFuture().setFailure(cause);
        }
        if (cause != null) {
            fireExceptionCaughtLater(ctx, cause);
        }
    }
    @Override
    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        if (issueHandshake) {
            handshake().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        ctx.sendUpstream(e);
                    }
                }
            });
        } else {
            super.channelConnected(ctx, e);
        }
    }
    @Override
    public void channelClosed(final ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.getPipeline().execute(new Runnable() {
            public void run() {
                if (!pendingUnencryptedWritesLock.tryLock()) {
                    return;
                }
                Throwable cause = null;
                try {
                    for (;;) {
                        PendingWrite pw = pendingUnencryptedWrites.poll();
                        if (pw == null) {
                            break;
                        }
                        if (cause == null) {
                            cause = new ClosedChannelException();
                        }
                        pw.future.setFailure(cause);
                    }
                    for (;;) {
                        MessageEvent ev = pendingEncryptedWrites.poll();
                        if (ev == null) {
                            break;
                        }
                        if (cause == null) {
                            cause = new ClosedChannelException();
                        }
                        ev.getFuture().setFailure(cause);
                    }
                } finally {
                    pendingUnencryptedWritesLock.unlock();
                }
                if (cause != null) {
                    fireExceptionCaught(ctx, cause);
                }
            }
        });
        super.channelClosed(ctx, e);
    }
    private final class SSLEngineInboundCloseFuture extends DefaultChannelFuture {
        SSLEngineInboundCloseFuture() {
            super(null, true);
        }
        void setClosed() {
            super.setSuccess();
        }
        @Override
        public Channel getChannel() {
            if (ctx == null) {
                return null;
            } else {
                return ctx.getChannel();
            }
        }
        @Override
        public boolean setSuccess() {
            return false;
        }
        @Override
        public boolean setFailure(Throwable cause) {
            return false;
        }
    }
}
