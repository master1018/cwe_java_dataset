
package com.google.api.client.auth.oauth2;
import java.util.Collection;
import java.util.Collections;
public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl {
  public AuthorizationCodeRequestUrl(String authorizationServerEncodedUrl, String clientId) {
    super(authorizationServerEncodedUrl, clientId, Collections.singleton("code"));
  }
  @Override
  public AuthorizationCodeRequestUrl setResponseTypes(Collection<String> responseTypes) {
    return (AuthorizationCodeRequestUrl) super.setResponseTypes(responseTypes);
  }
  @Override
  public AuthorizationCodeRequestUrl setRedirectUri(String redirectUri) {
    return (AuthorizationCodeRequestUrl) super.setRedirectUri(redirectUri);
  }
  @Override
  public AuthorizationCodeRequestUrl setScopes(Collection<String> scopes) {
    return (AuthorizationCodeRequestUrl) super.setScopes(scopes);
  }
  @Override
  public AuthorizationCodeRequestUrl setClientId(String clientId) {
    return (AuthorizationCodeRequestUrl) super.setClientId(clientId);
  }
  @Override
  public AuthorizationCodeRequestUrl setState(String state) {
    return (AuthorizationCodeRequestUrl) super.setState(state);
  }
  @Override
  public AuthorizationCodeRequestUrl set(String fieldName, Object value) {
    return (AuthorizationCodeRequestUrl) super.set(fieldName, value);
  }
  @Override
  public AuthorizationCodeRequestUrl clone() {
    return (AuthorizationCodeRequestUrl) super.clone();
  }
}
