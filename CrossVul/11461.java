
package org.opencastproject.kernel.http.impl;
import org.opencastproject.kernel.http.api.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
public class HttpClientImpl implements HttpClient {
  private static final Logger logger = LoggerFactory.getLogger(HttpClientImpl.class);
  private DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
  @Override
  public HttpParams getParams() {
    return defaultHttpClient.getParams();
  }
  @Override
  public CredentialsProvider getCredentialsProvider() {
    return defaultHttpClient.getCredentialsProvider();
  }
  @Override
  public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
    return defaultHttpClient.execute(httpUriRequest);
  }
  @Override
  public ClientConnectionManager getConnectionManager() {
    return defaultHttpClient.getConnectionManager();
  }
}
