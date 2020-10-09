package no.nav.pto_unleash;

import no.nav.common.featuretoggle.UnleashService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Configuration
@Import({FeatureController.class })
public class TestApplicationConfig {

    @Bean
    public UnleashService unleashService() {
        return mock(UnleashService.class);
    }

}
