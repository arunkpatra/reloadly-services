package com.reloadly.security.auth.model;

/**
 * A credentials object that wraps a reloadly issued JWT token. This is useful when a microservice wants to communicate
 * with another. In such scenarios, the JWT token issued already, may be transmitted in the <code>Authorization</code>
 * header. The format of the header value should be "Bearer _token_", where _token_ is the JWT token string.
 *
 * @author Arun Patra
 */
public class ReloadlyCredentials {

	public enum CredentialType {
		RELOADLY_JWT_TOKEN, MOCK_UID
	}
	private CredentialType type;
	/**
	 * The credential, usually a JWT token.
	 */
	private String credentials;

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
}
