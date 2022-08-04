package no.nav.poao_unleash;

import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.poao_unleash.auth.TokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@Import({FeatureController.class })
public class TestApplicationConfig {

    @Bean
    public UnleashClient unleashClient() {
        return mock(UnleashClient.class);
    }

    @Bean
    public TokenValidator tokenValidator() {
        return new TokenValidator() {
            @Override
            public IDTokenClaimsSet validate(String token) {
                return null;
            }
        };
    }
}
