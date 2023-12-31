
package com.google.api.client.auth.oauth2;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow.CredentialCreatedListener;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Joiner;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class AuthorizationCodeFlowTest extends AuthenticationTestBase {
  static class MyCredentialCreatedListener implements CredentialCreatedListener {
    boolean called = false;
    public void onCredentialCreated(Credential credential, TokenResponse tokenResponse)
        throws IOException {
      called = true;
    }
  }
  static class MyCredentialRefreshListener implements CredentialRefreshListener {
    boolean calledOnResponse = false;
    boolean calledOnError = false;
    public void onTokenResponse(Credential credential, TokenResponse tokenResponse)
        throws IOException {
      calledOnResponse = true;
    }
    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse)
        throws IOException {
      calledOnError = true;
    }
  }
  public void testCredentialCreatedListener() throws IOException {
    MyCredentialCreatedListener listener = new MyCredentialCreatedListener();
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.queryParameterAccessMethod(),
            new AccessTokenTransport(),
            new JacksonFactory(),
            TOKEN_SERVER_URL,
            new BasicAuthentication(CLIENT_ID, CLIENT_SECRET),
            CLIENT_ID,
            "authorizationServerEncodedUrl").setCredentialCreatedListener(listener).build();
    assertFalse(listener.called);
    flow.createAndStoreCredential(new TokenResponse(), "userId");
    assertTrue(listener.called);
  }
  public void testRefreshListeners() throws IOException {
    MyCredentialRefreshListener listener1 = new MyCredentialRefreshListener();
    MyCredentialRefreshListener listener2 = new MyCredentialRefreshListener();
    AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
        .queryParameterAccessMethod(),
        new AccessTokenTransport(),
        new JacksonFactory(),
        TOKEN_SERVER_URL,
        new BasicAuthentication(CLIENT_ID, CLIENT_SECRET),
        CLIENT_ID,
        "authorizationServerEncodedUrl").addRefreshListener(listener1)
        .addRefreshListener(listener2).build();
    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setAccessToken(ACCESS_TOKEN);
    tokenResponse.setRefreshToken(REFRESH_TOKEN);
    Credential cred = flow.createAndStoreCredential(tokenResponse, "userId");
    assertFalse(listener1.calledOnResponse);
    assertFalse(listener2.calledOnResponse);
    assertFalse(listener1.calledOnError);
    assertFalse(listener2.calledOnError);
    assertTrue(cred.refreshToken());
    assertTrue(listener1.calledOnResponse);
    assertTrue(listener2.calledOnResponse);
    assertFalse(listener1.calledOnError);
    assertFalse(listener2.calledOnError);
  }
  public void testNewAuthorizationUrl() {
    subsetTestNewAuthorizationUrl(Collections.<String>emptyList());
    subsetTestNewAuthorizationUrl(Collections.singleton("a"));
    subsetTestNewAuthorizationUrl(Arrays.asList("a", "b", "c", "d"));
  }
  public void subsetTestNewAuthorizationUrl(Collection<String> scopes) {
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.queryParameterAccessMethod(),
            new AccessTokenTransport(),
            new JacksonFactory(),
            TOKEN_SERVER_URL,
            new BasicAuthentication(CLIENT_ID, CLIENT_SECRET),
            CLIENT_ID,
            "https:
    AuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
    if (scopes.isEmpty()) {
      assertNull(url.getScopes());
    } else {
      assertEquals(Joiner.on(' ').join(scopes), url.getScopes());
    }
  }
  public void testPKCE() {
    AuthorizationCodeFlow flow =
        new AuthorizationCodeFlow.Builder(BearerToken.queryParameterAccessMethod(),
            new AccessTokenTransport(),
            new JacksonFactory(),
            TOKEN_SERVER_URL,
            new BasicAuthentication(CLIENT_ID, CLIENT_SECRET),
            CLIENT_ID,
            "https:
        .enablePKCE()
        .build();
    AuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
    assertNotNull(url.getCodeChallenge());
    assertNotNull(url.getCodeChallengeMethod());
    Set<String> methods = new HashSet<>(Arrays.asList("plain", "s256"));
    assertTrue(methods.contains(url.getCodeChallengeMethod().toLowerCase()));
    assertTrue(url.getCodeChallenge().length() > 0);
  }
}
