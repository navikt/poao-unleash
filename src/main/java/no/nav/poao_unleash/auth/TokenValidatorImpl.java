package no.nav.poao_unleash.auth;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.validators.IDTokenValidator;
import lombok.SneakyThrows;
import no.nav.common.auth.oidc.discovery.OidcDiscoveryConfiguration;

import java.net.MalformedURLException;
import java.net.URL;

public class TokenValidatorImpl implements TokenValidator {
    private final IDTokenValidator validator;
    private final static JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.RS256;

    public TokenValidatorImpl(String clientId, OidcDiscoveryConfiguration oidcConfiguration) {
        this.validator = createValidator(oidcConfiguration.issuer, oidcConfiguration.jwksUri, JWS_ALGORITHM, clientId);
    }

    @SneakyThrows
    public IDTokenClaimsSet validate(String token) {
        return this.validator.validate(JWTParser.parse(token), null);
    }

    private static IDTokenValidator createValidator(String issuerUrl, String jwksUrl, JWSAlgorithm algorithm, String clientId) {
        Issuer issuer = new Issuer(issuerUrl);
        ClientID clientID = new ClientID(clientId);
        try {
            return new IDTokenValidator(issuer, clientID, algorithm, new URL(jwksUrl));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid jwks URL " + jwksUrl);
        }
    }
}
