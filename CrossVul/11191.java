
package com.google.api.client.auth.oauth2;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Data;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import static com.google.api.client.util.Strings.isNullOrEmpty;
public class AuthorizationCodeFlow {
  private final AccessMethod method;
  private final HttpTransport transport;
  private final JsonFactory jsonFactory;
  private final String tokenServerEncodedUrl;
  private final HttpExecuteInterceptor clientAuthentication;
  private final String clientId;
  private final String authorizationServerEncodedUrl;
  private final PKCE pkce;
  @Beta
  @Deprecated
  private final CredentialStore credentialStore;
  @Beta
  private final DataStore<StoredCredential> credentialDataStore;
  private final HttpRequestInitializer requestInitializer;
  private final Clock clock;
  private final Collection<String> scopes;
  private final CredentialCreatedListener credentialCreatedListener;
  private final Collection<CredentialRefreshListener> refreshListeners;
  public AuthorizationCodeFlow(AccessMethod method,
      HttpTransport transport,
      JsonFactory jsonFactory,
      GenericUrl tokenServerUrl,
      HttpExecuteInterceptor clientAuthentication,
      String clientId,
      String authorizationServerEncodedUrl) {
    this(new Builder(method,
        transport,
        jsonFactory,
        tokenServerUrl,
        clientAuthentication,
        clientId,
        authorizationServerEncodedUrl));
  }
  protected AuthorizationCodeFlow(Builder builder) {
    method = Preconditions.checkNotNull(builder.method);
    transport = Preconditions.checkNotNull(builder.transport);
    jsonFactory = Preconditions.checkNotNull(builder.jsonFactory);
    tokenServerEncodedUrl = Preconditions.checkNotNull(builder.tokenServerUrl).build();
    clientAuthentication = builder.clientAuthentication;
    clientId = Preconditions.checkNotNull(builder.clientId);
    authorizationServerEncodedUrl =
        Preconditions.checkNotNull(builder.authorizationServerEncodedUrl);
    requestInitializer = builder.requestInitializer;
    credentialStore = builder.credentialStore;
    credentialDataStore = builder.credentialDataStore;
    scopes = Collections.unmodifiableCollection(builder.scopes);
    clock = Preconditions.checkNotNull(builder.clock);
    credentialCreatedListener = builder.credentialCreatedListener;
    refreshListeners = Collections.unmodifiableCollection(builder.refreshListeners);
    pkce = builder.pkce;
  }
  public AuthorizationCodeRequestUrl newAuthorizationUrl() {
    AuthorizationCodeRequestUrl url = new  AuthorizationCodeRequestUrl(authorizationServerEncodedUrl, clientId);
    url.setScopes(scopes);
    if (pkce != null) {
      url.setCodeChallenge(pkce.getChallenge());
      url.setCodeChallengeMethod(pkce.getChallengeMethod());
    }
    return url;
  }
  public AuthorizationCodeTokenRequest newTokenRequest(String authorizationCode) {
    HttpExecuteInterceptor pkceClientAuthenticationWrapper = new HttpExecuteInterceptor() {
      @Override
      public void intercept(HttpRequest request) throws IOException {
        clientAuthentication.intercept(request);
        if (pkce != null) {
          Map<String, Object> data = Data.mapOf(UrlEncodedContent.getContent(request).getData());
          data.put("code_verifier", pkce.getVerifier());
        }
      }
    };
    return new AuthorizationCodeTokenRequest(transport, jsonFactory,
        new GenericUrl(tokenServerEncodedUrl), authorizationCode).setClientAuthentication(
        pkceClientAuthenticationWrapper).setRequestInitializer(requestInitializer).setScopes(scopes);
  }
  @SuppressWarnings("deprecation")
  public Credential createAndStoreCredential(TokenResponse response, String userId)
      throws IOException {
    Credential credential = newCredential(userId).setFromTokenResponse(response);
    if (credentialStore != null) {
      credentialStore.store(userId, credential);
    }
    if (credentialDataStore != null) {
      credentialDataStore.set(userId, new StoredCredential(credential));
    }
    if (credentialCreatedListener != null) {
      credentialCreatedListener.onCredentialCreated(credential, response);
    }
    return credential;
  }
  @SuppressWarnings("deprecation")
  public Credential loadCredential(String userId) throws IOException {
    if (isNullOrEmpty(userId)) {
      return null;
    }
    if (credentialDataStore == null && credentialStore == null) {
      return null;
    }
    Credential credential = newCredential(userId);
    if (credentialDataStore != null) {
      StoredCredential stored = credentialDataStore.get(userId);
      if (stored == null) {
        return null;
      }
      credential.setAccessToken(stored.getAccessToken());
      credential.setRefreshToken(stored.getRefreshToken());
      credential.setExpirationTimeMilliseconds(stored.getExpirationTimeMilliseconds());
    } else if (!credentialStore.load(userId, credential)) {
      return null;
    }
    return credential;
  }
  @SuppressWarnings("deprecation")
  private Credential newCredential(String userId) {
    Credential.Builder builder = new Credential.Builder(method).setTransport(transport)
        .setJsonFactory(jsonFactory)
        .setTokenServerEncodedUrl(tokenServerEncodedUrl)
        .setClientAuthentication(clientAuthentication)
        .setRequestInitializer(requestInitializer)
        .setClock(clock);
    if (credentialDataStore != null) {
      builder.addRefreshListener(
          new DataStoreCredentialRefreshListener(userId, credentialDataStore));
    } else if (credentialStore != null) {
      builder.addRefreshListener(new CredentialStoreRefreshListener(userId, credentialStore));
    }
    builder.getRefreshListeners().addAll(refreshListeners);
    return builder.build();
  }
  public final AccessMethod getMethod() {
    return method;
  }
  public final HttpTransport getTransport() {
    return transport;
  }
  public final JsonFactory getJsonFactory() {
    return jsonFactory;
  }
  public final String getTokenServerEncodedUrl() {
    return tokenServerEncodedUrl;
  }
  public final HttpExecuteInterceptor getClientAuthentication() {
    return clientAuthentication;
  }
  public final String getClientId() {
    return clientId;
  }
  public final String getAuthorizationServerEncodedUrl() {
    return authorizationServerEncodedUrl;
  }
  @Beta
  @Deprecated
  public final CredentialStore getCredentialStore() {
    return credentialStore;
  }
  @Beta
  public final DataStore<StoredCredential> getCredentialDataStore() {
    return credentialDataStore;
  }
  public final HttpRequestInitializer getRequestInitializer() {
    return requestInitializer;
  }
  public final String getScopesAsString() {
    return Joiner.on(' ').join(scopes);
  }
  public final Collection<String> getScopes() {
    return scopes;
  }
  public final Clock getClock() {
    return clock;
  }
  public final Collection<CredentialRefreshListener> getRefreshListeners() {
    return refreshListeners;
  }
  public interface CredentialCreatedListener {
    void onCredentialCreated(Credential credential, TokenResponse tokenResponse) throws IOException;
  }
  private static class PKCE {
    private final String verifier;
    private String challenge;
    private String challengeMethod;
    public PKCE() {
      verifier = generateVerifier();
      generateChallenge(verifier);
    }
    private static String generateVerifier() {
      SecureRandom sr = new SecureRandom();
      byte[] code = new byte[32];
      sr.nextBytes(code);
      return Base64.encodeBase64URLSafeString(code);
    }
    private void generateChallenge(String verifier) {
      try {
        byte[] bytes = verifier.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        challenge = Base64.encodeBase64URLSafeString(digest);
        challengeMethod = "S256";
      } catch (NoSuchAlgorithmException e) {
        challenge = verifier;
        challengeMethod = "plain";
      }
    }
    public String getVerifier() {
      return verifier;
    }
    public String getChallenge() {
      return challenge;
    }
    public String getChallengeMethod() {
      return challengeMethod;
    }
  }
  public static class Builder {
    AccessMethod method;
    HttpTransport transport;
    JsonFactory jsonFactory;
    GenericUrl tokenServerUrl;
    HttpExecuteInterceptor clientAuthentication;
    String clientId;
    String authorizationServerEncodedUrl;
    PKCE pkce;
    @Deprecated
    @Beta
    CredentialStore credentialStore;
    @Beta
    DataStore<StoredCredential> credentialDataStore;
    HttpRequestInitializer requestInitializer;
    Collection<String> scopes = Lists.newArrayList();
    Clock clock = Clock.SYSTEM;
    CredentialCreatedListener credentialCreatedListener;
    Collection<CredentialRefreshListener> refreshListeners = Lists.newArrayList();
    public Builder(AccessMethod method,
        HttpTransport transport,
        JsonFactory jsonFactory,
        GenericUrl tokenServerUrl,
        HttpExecuteInterceptor clientAuthentication,
        String clientId,
        String authorizationServerEncodedUrl) {
      setMethod(method);
      setTransport(transport);
      setJsonFactory(jsonFactory);
      setTokenServerUrl(tokenServerUrl);
      setClientAuthentication(clientAuthentication);
      setClientId(clientId);
      setAuthorizationServerEncodedUrl(authorizationServerEncodedUrl);
    }
    public AuthorizationCodeFlow build() {
      return new AuthorizationCodeFlow(this);
    }
    public final AccessMethod getMethod() {
      return method;
    }
    public Builder setMethod(AccessMethod method) {
      this.method = Preconditions.checkNotNull(method);
      return this;
    }
    public final HttpTransport getTransport() {
      return transport;
    }
    public Builder setTransport(HttpTransport transport) {
      this.transport = Preconditions.checkNotNull(transport);
      return this;
    }
    public final JsonFactory getJsonFactory() {
      return jsonFactory;
    }
    public Builder setJsonFactory(JsonFactory jsonFactory) {
      this.jsonFactory = Preconditions.checkNotNull(jsonFactory);
      return this;
    }
    public final GenericUrl getTokenServerUrl() {
      return tokenServerUrl;
    }
    public Builder setTokenServerUrl(GenericUrl tokenServerUrl) {
      this.tokenServerUrl = Preconditions.checkNotNull(tokenServerUrl);
      return this;
    }
    public final HttpExecuteInterceptor getClientAuthentication() {
      return clientAuthentication;
    }
    public Builder setClientAuthentication(HttpExecuteInterceptor clientAuthentication) {
      this.clientAuthentication = clientAuthentication;
      return this;
    }
    public final String getClientId() {
      return clientId;
    }
    public Builder setClientId(String clientId) {
      this.clientId = Preconditions.checkNotNull(clientId);
      return this;
    }
    public final String getAuthorizationServerEncodedUrl() {
      return authorizationServerEncodedUrl;
    }
    public Builder setAuthorizationServerEncodedUrl(String authorizationServerEncodedUrl) {
      this.authorizationServerEncodedUrl =
          Preconditions.checkNotNull(authorizationServerEncodedUrl);
      return this;
    }
    @Beta
    @Deprecated
    public final CredentialStore getCredentialStore() {
      return credentialStore;
    }
    @Beta
    public final DataStore<StoredCredential> getCredentialDataStore() {
      return credentialDataStore;
    }
    public final Clock getClock() {
      return clock;
    }
    public Builder setClock(Clock clock) {
      this.clock = Preconditions.checkNotNull(clock);
      return this;
    }
    @Beta
    @Deprecated
    public Builder setCredentialStore(CredentialStore credentialStore) {
      Preconditions.checkArgument(credentialDataStore == null);
      this.credentialStore = credentialStore;
      return this;
    }
    @Beta
    public Builder setDataStoreFactory(DataStoreFactory dataStoreFactory) throws IOException {
      return setCredentialDataStore(StoredCredential.getDefaultDataStore(dataStoreFactory));
    }
    @Beta
    public Builder setCredentialDataStore(DataStore<StoredCredential> credentialDataStore) {
      Preconditions.checkArgument(credentialStore == null);
      this.credentialDataStore = credentialDataStore;
      return this;
    }
    public final HttpRequestInitializer getRequestInitializer() {
      return requestInitializer;
    }
    public Builder setRequestInitializer(HttpRequestInitializer requestInitializer) {
      this.requestInitializer = requestInitializer;
      return this;
    }
    @Beta
    public Builder enablePKCE() {
      this.pkce = new PKCE();
      return this;
    }
    public Builder setScopes(Collection<String> scopes) {
      this.scopes = Preconditions.checkNotNull(scopes);
      return this;
    }
    public final Collection<String> getScopes() {
      return scopes;
    }
    public Builder setCredentialCreatedListener(
        CredentialCreatedListener credentialCreatedListener) {
      this.credentialCreatedListener = credentialCreatedListener;
      return this;
    }
    public Builder addRefreshListener(CredentialRefreshListener refreshListener) {
      refreshListeners.add(Preconditions.checkNotNull(refreshListener));
      return this;
    }
    public final Collection<CredentialRefreshListener> getRefreshListeners() {
      return refreshListeners;
    }
    public Builder setRefreshListeners(Collection<CredentialRefreshListener> refreshListeners) {
      this.refreshListeners = Preconditions.checkNotNull(refreshListeners);
      return this;
    }
    public final CredentialCreatedListener getCredentialCreatedListener() {
      return credentialCreatedListener;
    }
  }
}
