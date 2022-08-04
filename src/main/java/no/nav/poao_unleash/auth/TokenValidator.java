package no.nav.poao_unleash.auth;

import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;

public interface TokenValidator {
    IDTokenClaimsSet validate(String token);

}
