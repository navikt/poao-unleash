package no.nav.poao_unleash;

import no.nav.common.auth.oidc.discovery.OidcDiscoveryConfiguration;
import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.featuretoggle.UnleashClientImpl;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import no.nav.poao_unleash.auth.TokenValidator;
import no.nav.poao_unleash.auth.TokenValidatorImpl;
import no.nav.poao_unleash.auth.discovery.OidcDiscoveryConfigurationClient;
import no.nav.poao_unleash.config.EnvironmentProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EnvironmentProperties.class})
public class ApplicationConfig {

    private final String APPLICATION_NAME = "poao-unleash";

    @Bean
    public FilterRegistrationBean<SetStandardHttpHeadersFilter> setStandardHttpHeadersFilter() {
        FilterRegistrationBean<SetStandardHttpHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SetStandardHttpHeadersFilter());
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public UnleashClient unleashClient() {
        // TODO: Hent URL til unleash gjennom properties
        return new UnleashClientImpl("https://unleash.nais.io/api/", APPLICATION_NAME);
    }

    @Bean
    public TokenValidator tokenValidator(EnvironmentProperties environmentProperties) {
        OidcDiscoveryConfigurationClient oidcClient = new OidcDiscoveryConfigurationClient();
        OidcDiscoveryConfiguration oidcDiscoveryConfiguration = oidcClient.fetchDiscoveryConfiguration(environmentProperties.getAzureAdDiscoveryUrl());
        return new TokenValidatorImpl(environmentProperties.getAzureAdClientId(), oidcDiscoveryConfiguration);
    }

}
