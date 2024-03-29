package no.nav.poao_unleash.auth.discovery;


import com.fasterxml.jackson.annotation.JsonAlias;

public class OidcDiscoveryConfiguration {

	@JsonAlias("jwks_uri")
	public String jwksUri;

	@JsonAlias("token_endpoint")
	public String tokenEndpoint;

	@JsonAlias("authorization_endpoint")
	public String authorizationEndpoint;

	public String issuer;
}
