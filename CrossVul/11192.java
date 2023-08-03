
package com.google.api.client.auth.oauth2;
import com.google.api.client.util.Key;
import java.util.Collection;
import java.util.Collections;
public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl {
  @Key("code_challenge")
  String codeChallenge;
  @Key("code_challenge_method")
  String codeChallengeMethod;
  public AuthorizationCodeRequestUrl(String authorizationServerEncodedUrl, String clientId) {
    super(authorizationServerEncodedUrl, clientId, Collections.singleton("code"));
  }
  public String getCodeChallenge() {
    return codeChallenge;
  }
  public String getCodeChallengeMethod() {
    return codeChallengeMethod;
  }
  public void setCodeChallenge(String codeChallenge) {
    this.codeChallenge = codeChallenge;
  }
  public void setCodeChallengeMethod(String codeChallengeMethod) {
    this.codeChallengeMethod = codeChallengeMethod;
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
