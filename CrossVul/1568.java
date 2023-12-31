
package io.vertx.core.http.impl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.util.AsciiString;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpServerRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import static io.vertx.core.http.Http2Settings.*;
public final class HttpUtils {
  private HttpUtils() {
  }
  private static int indexOfSlash(CharSequence str, int start) {
    for (int i = start; i < str.length(); i++) {
      if (str.charAt(i) == '/') {
        return i;
      }
    }
    return -1;
  }
  private static boolean matches(CharSequence path, int start, String what) {
    return matches(path, start, what, false);
  }
  private static boolean matches(CharSequence path, int start, String what, boolean exact) {
    if (exact) {
      if (path.length() - start != what.length()) {
        return false;
      }
    }
    if (path.length() - start >= what.length()) {
      for (int i = 0; i < what.length(); i++) {
        if (path.charAt(start + i) != what.charAt(i)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  public static String removeDots(CharSequence path) {
    if (path == null) {
      return null;
    }
    final StringBuilder obuf = new StringBuilder(path.length());
    int i = 0;
    while (i < path.length()) {
      if (matches(path, i, "./")) {
        i += 2;
      } else if (matches(path, i, "../")) {
        i += 3;
      } else if (matches(path, i, "/./")) {
        i += 2;
      } else if (matches(path, i,"/.", true)) {
        path = "/";
        i = 0;
      } else if (matches(path, i, "/../")) {
        i += 3;
        int pos = obuf.lastIndexOf("/");
        if (pos != -1) {
          obuf.delete(pos, obuf.length());
        }
      } else if (matches(path, i, "/..", true)) {
        path = "/";
        i = 0;
        int pos = obuf.lastIndexOf("/");
        if (pos != -1) {
          obuf.delete(pos, obuf.length());
        }
      } else if (matches(path, i, ".", true) || matches(path, i, "..", true)) {
        break;
      } else {
        if (path.charAt(i) == '/') {
          i++;
          if (obuf.length() == 0 || obuf.charAt(obuf.length() - 1) != '/') {
            obuf.append('/');
          }
        }
        int pos = indexOfSlash(path, i);
        if (pos != -1) {
          obuf.append(path, i, pos);
          i = pos;
        } else {
          obuf.append(path, i, path.length());
          break;
        }
      }
    }
    return obuf.toString();
  }
  public static URI resolveURIReference(String base, String ref) throws URISyntaxException {
    return resolveURIReference(URI.create(base), ref);
  }
  public static URI resolveURIReference(URI base, String ref) throws URISyntaxException {
    URI _ref = URI.create(ref);
    String scheme;
    String authority;
    String path;
    String query;
    if (_ref.getScheme() != null) {
      scheme = _ref.getScheme();
      authority = _ref.getAuthority();
      path = removeDots(_ref.getPath());
      query = _ref.getRawQuery();
    } else {
      if (_ref.getAuthority() != null) {
        authority = _ref.getAuthority();
        path = _ref.getPath();
        query = _ref.getRawQuery();
      } else {
        if (_ref.getPath().length() == 0) {
          path = base.getPath();
          if (_ref.getRawQuery() != null) {
            query = _ref.getRawQuery();
          } else {
            query = base.getRawQuery();
          }
        } else {
          if (_ref.getPath().startsWith("/")) {
            path = removeDots(_ref.getPath());
          } else {
            String mergedPath;
            String basePath = base.getPath();
            if (base.getAuthority() != null && basePath.length() == 0) {
              mergedPath = "/" + _ref.getPath();
            } else {
              int index = basePath.lastIndexOf('/');
              if (index > -1) {
                mergedPath = basePath.substring(0, index + 1) + _ref.getPath();
              } else {
                mergedPath = _ref.getPath();
              }
            }
            path = removeDots(mergedPath);
          }
          query = _ref.getRawQuery();
        }
        authority = base.getAuthority();
      }
      scheme = base.getScheme();
    }
    return new URI(scheme, authority, path, query, _ref.getFragment());
  }
  static String parsePath(String uri) {
    int i;
    if (uri.charAt(0) == '/') {
      i = 0;
    } else {
      i = uri.indexOf(":
      if (i == -1) {
        i = 0;
      } else {
        i = uri.indexOf('/', i + 3);
        if (i == -1) {
          return "/";
        }
      }
    }
    int queryStart = uri.indexOf('?', i);
    if (queryStart == -1) {
      queryStart = uri.length();
    }
    return uri.substring(i, queryStart);
  }
  static String parseQuery(String uri) {
    int i = uri.indexOf('?');
    if (i == -1) {
      return null;
    } else {
      return uri.substring(i + 1 , uri.length());
    }
  }
  static String absoluteURI(String serverOrigin, HttpServerRequest req) throws URISyntaxException {
    String absoluteURI;
    URI uri = new URI(req.uri());
    String scheme = uri.getScheme();
    if (scheme != null && (scheme.equals("http") || scheme.equals("https"))) {
      absoluteURI = uri.toString();
    } else {
      String host = req.host();
      if (host != null) {
        absoluteURI = req.scheme() + ":
      } else {
        absoluteURI = serverOrigin + uri;
      }
    }
    return absoluteURI;
  }
  static MultiMap params(String uri) {
    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
    Map<String, List<String>> prms = queryStringDecoder.parameters();
    MultiMap params = new CaseInsensitiveHeaders();
    if (!prms.isEmpty()) {
      for (Map.Entry<String, List<String>> entry: prms.entrySet()) {
        params.add(entry.getKey(), entry.getValue());
      }
    }
    return params;
  }
  public static void fromVertxInitialSettings(boolean server, io.vertx.core.http.Http2Settings vertxSettings, Http2Settings nettySettings) {
    if (vertxSettings != null) {
      if (!server && vertxSettings.isPushEnabled() != DEFAULT_ENABLE_PUSH) {
        nettySettings.pushEnabled(vertxSettings.isPushEnabled());
      }
      if (vertxSettings.getHeaderTableSize() != DEFAULT_HEADER_TABLE_SIZE) {
        nettySettings.put('\u0001', (Long)vertxSettings.getHeaderTableSize());
      }
      if (vertxSettings.getInitialWindowSize() != DEFAULT_INITIAL_WINDOW_SIZE) {
        nettySettings.initialWindowSize(vertxSettings.getInitialWindowSize());
      }
      if (vertxSettings.getMaxConcurrentStreams() != DEFAULT_MAX_CONCURRENT_STREAMS) {
        nettySettings.maxConcurrentStreams(vertxSettings.getMaxConcurrentStreams());
      }
      if (vertxSettings.getMaxFrameSize() != DEFAULT_MAX_FRAME_SIZE) {
        nettySettings.maxFrameSize(vertxSettings.getMaxFrameSize());
      }
      if (vertxSettings.getMaxHeaderListSize() != DEFAULT_MAX_HEADER_LIST_SIZE) {
        nettySettings.maxHeaderListSize(vertxSettings.getMaxHeaderListSize());
      }
      Map<Integer, Long> extraSettings = vertxSettings.getExtraSettings();
      if (extraSettings != null) {
        extraSettings.forEach((code, setting) -> {
          nettySettings.put((char)(int)code, setting);
        });
      }
    }
  }
  public static Http2Settings fromVertxSettings(io.vertx.core.http.Http2Settings settings) {
    Http2Settings converted = new Http2Settings();
    converted.pushEnabled(settings.isPushEnabled());
    converted.maxFrameSize(settings.getMaxFrameSize());
    converted.initialWindowSize(settings.getInitialWindowSize());
    converted.headerTableSize(settings.getHeaderTableSize());
    converted.maxConcurrentStreams(settings.getMaxConcurrentStreams());
    converted.maxHeaderListSize(settings.getMaxHeaderListSize());
    if (settings.getExtraSettings() != null) {
      settings.getExtraSettings().forEach((key, value) -> {
        converted.put((char)(int)key, value);
      });
    }
    return converted;
  }
  public static io.vertx.core.http.Http2Settings toVertxSettings(Http2Settings settings) {
    io.vertx.core.http.Http2Settings converted = new io.vertx.core.http.Http2Settings();
    Boolean pushEnabled = settings.pushEnabled();
    if (pushEnabled != null) {
      converted.setPushEnabled(pushEnabled);
    }
    Long maxConcurrentStreams = settings.maxConcurrentStreams();
    if (maxConcurrentStreams != null) {
      converted.setMaxConcurrentStreams(maxConcurrentStreams);
    }
    Long maxHeaderListSize = settings.maxHeaderListSize();
    if (maxHeaderListSize != null) {
      converted.setMaxHeaderListSize(maxHeaderListSize);
    }
    Integer maxFrameSize = settings.maxFrameSize();
    if (maxFrameSize != null) {
      converted.setMaxFrameSize(maxFrameSize);
    }
    Integer initialWindowSize = settings.initialWindowSize();
    if (initialWindowSize != null) {
      converted.setInitialWindowSize(initialWindowSize);
    }
    Long headerTableSize = settings.headerTableSize();
    if (headerTableSize != null) {
      converted.setHeaderTableSize(headerTableSize);
    }
    settings.forEach((key, value) -> {
      if (key > 6) {
        converted.set(key, value);
      }
    });
    return converted;
  }
  static Http2Settings decodeSettings(String base64Settings) {
    try {
      Http2Settings settings = new Http2Settings();
      Buffer buffer = Buffer.buffer(Base64.getUrlDecoder().decode(base64Settings));
      int pos = 0;
      int len = buffer.length();
      while (pos < len) {
        int i = buffer.getUnsignedShort(pos);
        pos += 2;
        long j = buffer.getUnsignedInt(pos);
        pos += 4;
        settings.put((char)i, (Long)j);
      }
      return settings;
    } catch (Exception ignore) {
    }
    return null;
  }
  public static String encodeSettings(io.vertx.core.http.Http2Settings settings) {
    Buffer buffer = Buffer.buffer();
    fromVertxSettings(settings).forEach((c, l) -> {
      buffer.appendUnsignedShort(c);
      buffer.appendUnsignedInt(l);
    });
    return Base64.getUrlEncoder().encodeToString(buffer.getBytes());
  }
  public static ByteBuf generateWSCloseFrameByteBuf(short statusCode, String reason) {
    if (reason != null)
      return Unpooled.copiedBuffer(
        Unpooled.copyShort(statusCode), 
        Unpooled.copiedBuffer(reason, Charset.forName("UTF-8"))
      );
    else
      return Unpooled.copyShort(statusCode);
  }
  private static class CustomCompressor extends HttpContentCompressor {
    @Override
    public ZlibWrapper determineWrapper(String acceptEncoding) {
      return super.determineWrapper(acceptEncoding);
    }
  }
  private static final CustomCompressor compressor = new CustomCompressor();
  static String determineContentEncoding(Http2Headers headers) {
    String acceptEncoding = headers.get(HttpHeaderNames.ACCEPT_ENCODING) != null ? headers.get(HttpHeaderNames.ACCEPT_ENCODING).toString() : null;
    if (acceptEncoding != null) {
      ZlibWrapper wrapper = compressor.determineWrapper(acceptEncoding);
      if (wrapper != null) {
        switch (wrapper) {
          case GZIP:
            return "gzip";
          case ZLIB:
            return "deflate";
        }
      }
    }
    return null;
  }
  static HttpMethod toNettyHttpMethod(io.vertx.core.http.HttpMethod method, String rawMethod) {
    switch (method) {
      case CONNECT: {
        return HttpMethod.CONNECT;
      }
      case GET: {
        return HttpMethod.GET;
      }
      case PUT: {
        return HttpMethod.PUT;
      }
      case POST: {
        return HttpMethod.POST;
      }
      case DELETE: {
        return HttpMethod.DELETE;
      }
      case HEAD: {
        return HttpMethod.HEAD;
      }
      case OPTIONS: {
        return HttpMethod.OPTIONS;
      }
      case TRACE: {
        return HttpMethod.TRACE;
      }
      case PATCH: {
        return HttpMethod.PATCH;
      }
      default: {
        return HttpMethod.valueOf(rawMethod);
      }
    }
  }
  static HttpVersion toNettyHttpVersion(io.vertx.core.http.HttpVersion version) {
    switch (version) {
      case HTTP_1_0: {
        return HttpVersion.HTTP_1_0;
      }
      case HTTP_1_1: {
        return HttpVersion.HTTP_1_1;
      }
      default:
        throw new IllegalArgumentException("Unsupported HTTP version: " + version);
    }
  }
  static io.vertx.core.http.HttpMethod toVertxMethod(String method) {
    try {
      return io.vertx.core.http.HttpMethod.valueOf(method);
    } catch (IllegalArgumentException e) {
      return io.vertx.core.http.HttpMethod.OTHER;
    }
  }
  private static final AsciiString TIMEOUT_EQ = AsciiString.of("timeout=");
  public static int parseKeepAliveHeaderTimeout(CharSequence value) {
    int len = value.length();
    int pos = 0;
    while (pos < len) {
      int idx = AsciiString.indexOf(value, ',', pos);
      int next;
      if (idx == -1) {
        idx = next = len;
      } else {
        next = idx + 1;
      }
      while (pos < idx && value.charAt(pos) == ' ') {
        pos++;
      }
      int to = idx;
      while (to > pos && value.charAt(to -1) == ' ') {
        to--;
      }
      if (AsciiString.regionMatches(value, true, pos, TIMEOUT_EQ, 0, TIMEOUT_EQ.length())) {
        pos += TIMEOUT_EQ.length();
        if (pos < to) {
          int ret = 0;
          while (pos < to) {
            int ch = value.charAt(pos++);
            if (ch >= '0' && ch < '9') {
              ret = ret * 10 + (ch - '0');
            } else {
              ret = -1;
              break;
            }
          }
          if (ret > -1) {
            return ret;
          }
        }
      }
      pos = next;
    }
    return -1;
  }
}
