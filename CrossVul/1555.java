
package io.vertx.core.http;
public final class HttpHeaders {
  public static final CharSequence ACCEPT = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT);
  public static final CharSequence ACCEPT_CHARSET = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT_CHARSET);
  public static final CharSequence ACCEPT_ENCODING = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT_ENCODING);
  public static final CharSequence ACCEPT_LANGUAGE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT_LANGUAGE);
  public static final CharSequence ACCEPT_RANGES = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT_RANGES);
  public static final CharSequence ACCEPT_PATCH = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCEPT_PATCH);
  public static final CharSequence ACCESS_CONTROL_ALLOW_CREDENTIALS = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_ALLOW_CREDENTIALS);
  public static final CharSequence ACCESS_CONTROL_ALLOW_HEADERS = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_ALLOW_HEADERS);
  public static final CharSequence ACCESS_CONTROL_ALLOW_METHODS = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_ALLOW_METHODS);
  public static final CharSequence ACCESS_CONTROL_ALLOW_ORIGIN = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_ALLOW_ORIGIN);
  public static final CharSequence ACCESS_CONTROL_EXPOSE_HEADERS = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_EXPOSE_HEADERS);
  public static final CharSequence ACCESS_CONTROL_MAX_AGE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_MAX_AGE);
  public static final CharSequence ACCESS_CONTROL_REQUEST_HEADERS = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_REQUEST_HEADERS);
  public static final CharSequence ACCESS_CONTROL_REQUEST_METHOD = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ACCESS_CONTROL_REQUEST_METHOD);
  public static final CharSequence AGE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.AGE);
  public static final CharSequence ALLOW = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ALLOW);
  public static final CharSequence AUTHORIZATION = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION);
  public static final CharSequence CACHE_CONTROL = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CACHE_CONTROL);
  public static final CharSequence CONNECTION = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION);
  public static final CharSequence CONTENT_BASE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_BASE);
  public static final CharSequence CONTENT_ENCODING = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_ENCODING);
  public static final CharSequence CONTENT_LANGUAGE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LANGUAGE);
  public static final CharSequence CONTENT_LENGTH = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH);
  public static final CharSequence CONTENT_LOCATION = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LOCATION);
  public static final CharSequence CONTENT_TRANSFER_ENCODING = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TRANSFER_ENCODING);
  public static final CharSequence CONTENT_MD5 = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_MD5);
  public static final CharSequence CONTENT_RANGE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_RANGE);
  public static final CharSequence CONTENT_TYPE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE);
  public static final CharSequence COOKIE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.COOKIE);
  public static final CharSequence DATE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.DATE);
  public static final CharSequence ETAG = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ETAG);
  public static final CharSequence EXPECT = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.EXPECT);
  public static final CharSequence EXPIRES = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.EXPIRES);
  public static final CharSequence FROM = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.FROM);
  public static final CharSequence HOST = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.HOST);
  public static final CharSequence IF_MATCH = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.IF_MATCH);
  public static final CharSequence IF_MODIFIED_SINCE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.IF_MODIFIED_SINCE);
  public static final CharSequence IF_NONE_MATCH = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.IF_NONE_MATCH);
  public static final CharSequence LAST_MODIFIED = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.LAST_MODIFIED);
  public static final CharSequence LOCATION = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.LOCATION);
  public static final CharSequence ORIGIN = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.ORIGIN);
  public static final CharSequence PROXY_AUTHENTICATE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.PROXY_AUTHENTICATE);
  public static final CharSequence PROXY_AUTHORIZATION = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.PROXY_AUTHORIZATION);
  public static final CharSequence REFERER = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.REFERER);
  public static final CharSequence RETRY_AFTER = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.RETRY_AFTER);
  public static final CharSequence SERVER = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.SERVER);
  public static final CharSequence TRANSFER_ENCODING = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.TRANSFER_ENCODING);
  public static final CharSequence USER_AGENT = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.USER_AGENT);
  public static final CharSequence SET_COOKIE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE);
  public static final CharSequence APPLICATION_X_WWW_FORM_URLENCODED = createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_X_WWW_FORM_URLENCODED);
  public static final CharSequence CHUNKED =  createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.CHUNKED);
  public static final CharSequence CLOSE =  createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.CLOSE);
  public static final CharSequence CONTINUE =  createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.CONTINUE);
  public static final CharSequence IDENTITY =  createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.IDENTITY);
  public static final CharSequence KEEP_ALIVE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE);
  public static final CharSequence UPGRADE = createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.UPGRADE);
  public static final CharSequence WEBSOCKET = createOptimized(io.netty.handler.codec.http.HttpHeaders.Values.WEBSOCKET);
  public static final CharSequence DEFLATE_GZIP = createOptimized("deflate, gzip");
  public static final CharSequence TEXT_HTML = createOptimized("text/html");
  public static final CharSequence GET = createOptimized("GET");
  public static CharSequence createOptimized(String value) {
    return io.netty.handler.codec.http.HttpHeaders.newEntity(value);
  }
  private HttpHeaders() {
  }
}
