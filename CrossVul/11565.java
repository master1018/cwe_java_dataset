
package io.vertx.core.http.impl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpFrame;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.impl.headers.VertxHttpHeaders;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetSocket;
import java.util.List;
import java.util.Objects;
import static io.vertx.core.http.HttpHeaders.*;
public class HttpClientRequestImpl extends HttpClientRequestBase implements HttpClientRequest {
  static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
  private final VertxInternal vertx;
  private Handler<HttpClientResponse> respHandler;
  private Handler<Void> endHandler;
  private boolean chunked;
  private String hostHeader;
  private String rawMethod;
  private Handler<Void> continueHandler;
  private Handler<Void> drainHandler;
  private Handler<HttpClientRequest> pushHandler;
  private Handler<HttpConnection> connectionHandler;
  private boolean completed;
  private Handler<Void> completionHandler;
  private Long reset;
  private ByteBuf pendingChunks;
  private int pendingMaxSize = -1;
  private int followRedirects;
  private long written;
  private VertxHttpHeaders headers;
  private HttpClientStream stream;
  private boolean connecting;
  HttpClientRequestImpl(HttpClientImpl client, boolean ssl, HttpMethod method, String host, int port,
                        String relativeURI, VertxInternal vertx) {
    super(client, ssl, method, host, port, relativeURI);
    this.chunked = false;
    this.vertx = vertx;
  }
  @Override
  public int streamId() {
    HttpClientStream s;
    synchronized (this) {
      if ((s = stream) == null) {
        return -1;
      }
    }
    return s.id();
  }
  @Override
  public  synchronized HttpClientRequest handler(Handler<HttpClientResponse> handler) {
    if (handler != null) {
      checkComplete();
      respHandler = checkConnect(method, handler);
    } else {
      respHandler = null;
    }
    return this;
  }
  @Override
  public HttpClientRequest pause() {
    return this;
  }
  @Override
  public HttpClientRequest resume() {
    return this;
  }
  @Override
  public HttpClientRequest setFollowRedirects(boolean followRedirects) {
    synchronized (this) {
      checkComplete();
      if (followRedirects) {
        this.followRedirects = client.getOptions().getMaxRedirects() - 1;
      } else {
        this.followRedirects = 0;
      }
      return this;
    }
  }
  @Override
  public HttpClientRequest endHandler(Handler<Void> handler) {
    synchronized (this) {
      if (handler != null) {
        checkComplete();
      }
      endHandler = handler;
      return this;
    }
  }
  @Override
  public HttpClientRequestImpl setChunked(boolean chunked) {
    synchronized (this) {
      checkComplete();
      if (written > 0) {
        throw new IllegalStateException("Cannot set chunked after data has been written on request");
      }
      if (client.getOptions().getProtocolVersion() != io.vertx.core.http.HttpVersion.HTTP_1_0) {
        this.chunked = chunked;
      }
      return this;
    }
  }
  @Override
  public synchronized boolean isChunked() {
    return chunked;
  }
  @Override
  public synchronized String getRawMethod() {
    return rawMethod;
  }
  @Override
  public synchronized HttpClientRequest setRawMethod(String method) {
    this.rawMethod = method;
    return this;
  }
  @Override
  public synchronized HttpClientRequest setHost(String host) {
    this.hostHeader = host;
    return this;
  }
  @Override
  public synchronized String getHost() {
    return hostHeader;
  }
  @Override
  public synchronized MultiMap headers() {
    if (headers == null) {
      headers = new VertxHttpHeaders();
    }
    return headers;
  }
  @Override
  public synchronized HttpClientRequest putHeader(String name, String value) {
    checkComplete();
    headers().set(name, value);
    return this;
  }
  @Override
  public synchronized HttpClientRequest putHeader(String name, Iterable<String> values) {
    checkComplete();
    headers().set(name, values);
    return this;
  }
  @Override
  public HttpClientRequest setWriteQueueMaxSize(int maxSize) {
    HttpClientStream s;
    synchronized (this) {
      checkComplete();
      if ((s = stream) == null) {
        pendingMaxSize = maxSize;
        return this;
      }
    }
    s.doSetWriteQueueMaxSize(maxSize);
    return this;
  }
  @Override
  public boolean writeQueueFull() {
    HttpClientStream s;
    synchronized (this) {
      checkComplete();
      if ((s = stream) == null) {
        return false;
      }
    }
    return s.isNotWritable();
  }
  @Override
  public HttpClientRequest drainHandler(Handler<Void> handler) {
    synchronized (this) {
      if (handler != null) {
        checkComplete();
        drainHandler = handler;
        HttpClientStream s;
        if ((s = stream) == null) {
          return this;
        }
        s.getContext().runOnContext(v -> {
          synchronized (HttpClientRequestImpl.this) {
            if (!stream.isNotWritable()) {
              handleDrained();
            }
          }
        });
      } else {
        drainHandler = null;
      }
      return this;
    }
  }
  @Override
  public synchronized HttpClientRequest continueHandler(Handler<Void> handler) {
    if (handler != null) {
      checkComplete();
    }
    this.continueHandler = handler;
    return this;
  }
  @Override
  public HttpClientRequest sendHead() {
    return sendHead(null);
  }
  @Override
  public synchronized HttpClientRequest sendHead(Handler<HttpVersion> headersHandler) {
    checkComplete();
    checkResponseHandler();
    if (stream != null) {
      throw new IllegalStateException("Head already written");
    } else {
      connect(headersHandler);
    }
    return this;
  }
  @Override
  public synchronized HttpClientRequest putHeader(CharSequence name, CharSequence value) {
    checkComplete();
    headers().set(name, value);
    return this;
  }
  @Override
  public synchronized HttpClientRequest putHeader(CharSequence name, Iterable<CharSequence> values) {
    checkComplete();
    headers().set(name, values);
    return this;
  }
  @Override
  public synchronized HttpClientRequest pushHandler(Handler<HttpClientRequest> handler) {
    pushHandler = handler;
    return this;
  }
  @Override
  public boolean reset(long code) {
    HttpClientStream s;
    synchronized (this) {
      if (reset != null) {
        return false;
      }
      reset = code;
      if (tryComplete()) {
        if (completionHandler != null) {
          completionHandler.handle(null);
        }
      }
      s = stream;
    }
    if (s != null) {
      s.reset(code);
    }
    return true;
  }
  private boolean tryComplete() {
    if (!completed) {
      completed = true;
      drainHandler = null;
      return true;
    } else {
      return false;
    }
  }
  @Override
  public HttpConnection connection() {
    HttpClientStream s;
    synchronized (this) {
      if ((s = stream) == null) {
        return null;
      }
    }
    return s.connection();
  }
  @Override
  public synchronized HttpClientRequest connectionHandler(@Nullable Handler<HttpConnection> handler) {
    connectionHandler = handler;
    return this;
  }
  @Override
  public synchronized HttpClientRequest writeCustomFrame(int type, int flags, Buffer payload) {
    HttpClientStream s;
    synchronized (this) {
      checkComplete();
      if ((s = stream) == null) {
        throw new IllegalStateException("Not yet connected");
      }
    }
    s.writeFrame(type, flags, payload.getByteBuf());
    return this;
  }
  void handleDrained() {
    Handler<Void> handler;
    synchronized (this) {
      if ((handler = drainHandler) == null) {
        return;
      }
    }
    try {
      handler.handle(null);
    } catch (Throwable t) {
      handleException(t);
    }
  }
  private void handleNextRequest(HttpClientRequestImpl next, long timeoutMs) {
    next.handler(respHandler);
    next.exceptionHandler(exceptionHandler());
    exceptionHandler(null);
    next.endHandler(endHandler);
    next.pushHandler = pushHandler;
    next.followRedirects = followRedirects - 1;
    next.written = written;
    if (next.hostHeader == null) {
      next.hostHeader = hostHeader;
    }
    if (headers != null && next.headers == null) {
      next.headers().addAll(headers);
    }
    Future<Void> fut = Future.future();
    fut.setHandler(ar -> {
      if (ar.succeeded()) {
        if (timeoutMs > 0) {
          next.setTimeout(timeoutMs);
        }
        next.end();
      } else {
        next.handleException(ar.cause());
      }
    });
    if (exceptionOccurred != null) {
      fut.fail(exceptionOccurred);
    }
    else if (completed) {
      fut.complete();
    } else {
      exceptionHandler(err -> {
        if (!fut.isComplete()) {
          fut.fail(err);
        }
      });
      completionHandler = v -> {
        if (!fut.isComplete()) {
          fut.complete();
        }
      };
    }
  }
  protected void doHandleResponse(HttpClientResponseImpl resp, long timeoutMs) {
    if (reset == null) {
      int statusCode = resp.statusCode();
      if (followRedirects > 0 && statusCode >= 300 && statusCode < 400) {
        Future<HttpClientRequest> next = client.redirectHandler().apply(resp);
        if (next != null) {
          next.setHandler(ar -> {
            if (ar.succeeded()) {
              handleNextRequest((HttpClientRequestImpl) ar.result(), timeoutMs);
            } else {
              handleException(ar.cause());
            }
          });
          return;
        }
      }
      if (statusCode == 100) {
        if (continueHandler != null) {
          continueHandler.handle(null);
        }
      } else {
        if (respHandler != null) {
          respHandler.handle(resp);
        }
        if (endHandler != null) {
          endHandler.handle(null);
        }
      }
    }
  }
  @Override
  protected String hostHeader() {
    return hostHeader != null ? hostHeader : super.hostHeader();
  }
  private Handler<HttpClientResponse> checkConnect(io.vertx.core.http.HttpMethod method, Handler<HttpClientResponse> handler) {
    if (method == io.vertx.core.http.HttpMethod.CONNECT) {
      handler = connectHandler(handler);
    }
    return handler;
  }
  private Handler<HttpClientResponse> connectHandler(Handler<HttpClientResponse> responseHandler) {
    Objects.requireNonNull(responseHandler, "no null responseHandler accepted");
    return resp -> {
      HttpClientResponse response;
      if (resp.statusCode() == 200) {
        NetSocket socket = resp.netSocket();
        socket.pause();
        response = new HttpClientResponse() {
          private boolean resumed;
          @Override
          public HttpClientRequest request() {
            return resp.request();
          }
          @Override
          public int statusCode() {
            return resp.statusCode();
          }
          @Override
          public String statusMessage() {
            return resp.statusMessage();
          }
          @Override
          public MultiMap headers() {
            return resp.headers();
          }
          @Override
          public String getHeader(String headerName) {
            return resp.getHeader(headerName);
          }
          @Override
          public String getHeader(CharSequence headerName) {
            return resp.getHeader(headerName);
          }
          @Override
          public String getTrailer(String trailerName) {
            return resp.getTrailer(trailerName);
          }
          @Override
          public MultiMap trailers() {
            return resp.trailers();
          }
          @Override
          public List<String> cookies() {
            return resp.cookies();
          }
          @Override
          public HttpVersion version() {
            return resp.version();
          }
          @Override
          public HttpClientResponse bodyHandler(Handler<Buffer> bodyHandler) {
            resp.bodyHandler(bodyHandler);
            return this;
          }
          @Override
          public HttpClientResponse customFrameHandler(Handler<HttpFrame> handler) {
            resp.customFrameHandler(handler);
            return this;
          }
          @Override
          public synchronized NetSocket netSocket() {
            if (!resumed) {
              resumed = true;
              vertx.getContext().runOnContext((v) -> socket.resume()); 
            }
            return socket;
          }
          @Override
          public HttpClientResponse endHandler(Handler<Void> endHandler) {
            resp.endHandler(endHandler);
            return this;
          }
          @Override
          public HttpClientResponse handler(Handler<Buffer> handler) {
            resp.handler(handler);
            return this;
          }
          @Override
          public HttpClientResponse pause() {
            resp.pause();
            return this;
          }
          @Override
          public HttpClientResponse resume() {
            resp.resume();
            return this;
          }
          @Override
          public HttpClientResponse exceptionHandler(Handler<Throwable> handler) {
            resp.exceptionHandler(handler);
            return this;
          }
        };
      } else {
        response = resp;
      }
      responseHandler.handle(response);
    };
  }
  private synchronized void connect(Handler<HttpVersion> headersHandler) {
    if (!connecting) {
      if (method == HttpMethod.OTHER && rawMethod == null) {
        throw new IllegalStateException("You must provide a rawMethod when using an HttpMethod.OTHER method");
      }
      String peerHost;
      if (hostHeader != null) {
        int idx = hostHeader.lastIndexOf(':');
        if (idx != -1) {
          peerHost = hostHeader.substring(0, idx);
        } else {
          peerHost = hostHeader;
        }
      } else {
        peerHost = host;
      }
      Handler<HttpConnection> initializer = connectionHandler;
      ContextInternal connectCtx = vertx.getOrCreateContext();
      connecting = true;
      client.getConnectionForRequest(connectCtx, peerHost, ssl, port, host, ar1 -> {
        if (ar1.succeeded()) {
          HttpClientStream stream = ar1.result();
          ContextInternal ctx = (ContextInternal) stream.getContext();
          if (stream.id() == 1 && initializer != null) {
            ctx.executeFromIO(v -> {
              initializer.handle(stream.connection());
            });
          }
          if (exceptionOccurred != null || reset != null) {
            stream.reset(0);
          } else {
            ctx.executeFromIO(v -> {
              connected(headersHandler, stream);
            });
          }
        } else {
          connectCtx.executeFromIO(v -> {
            handleException(ar1.cause());
          });
        }
      });
    }
  }
  private void connected(Handler<HttpVersion> headersHandler, HttpClientStream stream) {
    synchronized (this) {
      this.stream = stream;
      stream.beginRequest(this);
      if (pendingMaxSize != -1) {
        stream.doSetWriteQueueMaxSize(pendingMaxSize);
      }
      if (pendingChunks != null) {
        ByteBuf pending = pendingChunks;
        pendingChunks = null;
        if (completed) {
          stream.writeHead(method, rawMethod, uri, headers, hostHeader(), chunked, pending, true);
          stream.reportBytesWritten(written);
          stream.endRequest();
        } else {
          stream.writeHead(method, rawMethod, uri, headers, hostHeader(), chunked, pending, false);
        }
      } else {
        if (completed) {
          stream.writeHead(method, rawMethod, uri, headers, hostHeader(), chunked, null, true);
          stream.reportBytesWritten(written);
          stream.endRequest();
        } else {
          stream.writeHead(method, rawMethod, uri, headers, hostHeader(), chunked, null, false);
        }
      }
      this.connecting = false;
      this.stream = stream;
    }
    if (headersHandler != null) {
      headersHandler.handle(stream.version());
    }
  }
  private boolean contentLengthSet() {
    return headers != null && headers().contains(CONTENT_LENGTH);
  }
  @Override
  public void end(String chunk) {
    end(Buffer.buffer(chunk));
  }
  @Override
  public void end(String chunk, String enc) {
    Objects.requireNonNull(enc, "no null encoding accepted");
    end(Buffer.buffer(chunk, enc));
  }
  @Override
  public void end(Buffer chunk) {
    write(chunk.getByteBuf(), true);
  }
  @Override
  public void end() {
    write(null, true);
  }
  @Override
  public HttpClientRequestImpl write(Buffer chunk) {
    ByteBuf buf = chunk.getByteBuf();
    write(buf, false);
    return this;
  }
  @Override
  public HttpClientRequestImpl write(String chunk) {
    return write(Buffer.buffer(chunk));
  }
  @Override
  public HttpClientRequestImpl write(String chunk, String enc) {
    Objects.requireNonNull(enc, "no null encoding accepted");
    return write(Buffer.buffer(chunk, enc));
  }
  private void write(ByteBuf buff, boolean end) {
    HttpClientStream s;
    synchronized (this) {
      checkComplete();
      checkResponseHandler();
      if (end) {
        if (buff != null && !chunked && !contentLengthSet()) {
          headers().set(CONTENT_LENGTH, String.valueOf(buff.readableBytes()));
        }
      } else {
        if (!chunked && !contentLengthSet()) {
          throw new IllegalStateException("You must set the Content-Length header to be the total size of the message "
            + "body BEFORE sending any data if you are not using HTTP chunked encoding.");
        }
      }
      if (buff == null && !end) {
        return;
      }
      if (buff != null) {
        written += buff.readableBytes();
      }
      if ((s = stream) == null) {
        if (buff != null) {
          if (pendingChunks == null) {
            pendingChunks = buff;
          } else {
            CompositeByteBuf pending;
            if (pendingChunks instanceof CompositeByteBuf) {
              pending = (CompositeByteBuf) pendingChunks;
            } else {
              pending = Unpooled.compositeBuffer();
              pending.addComponent(true, pendingChunks);
              pendingChunks = pending;
            }
            pending.addComponent(true, buff);
          }
        }
        if (end) {
          tryComplete();
          if (completionHandler != null) {
            completionHandler.handle(null);
          }
        }
        connect(null);
        return;
      }
    }
    s.writeBuffer(buff, end);
    if (end) {
      s.reportBytesWritten(written); 
    }
    if (end) {
      Handler<Void> handler;
      synchronized (this) {
        tryComplete();
        s.endRequest();
        if ((handler = completionHandler) == null) {
          return;
        }
      }
      handler.handle(null);
    }
  }
  protected void checkComplete() {
    if (completed) {
      throw new IllegalStateException("Request already complete");
    }
  }
  private void checkResponseHandler() {
    if (respHandler == null) {
      throw new IllegalStateException("You must set an handler for the HttpClientResponse before connecting");
    }
  }
  synchronized Handler<HttpClientRequest> pushHandler() {
    return pushHandler;
  }
}
