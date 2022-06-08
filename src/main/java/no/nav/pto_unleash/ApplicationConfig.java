package no.nav.pto_unleash;

import no.nav.common.featuretoggle.UnleashClient;
import no.nav.common.featuretoggle.UnleashClientImpl;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
        return new UnleashClientImpl("https://unleash.nais.io/api/", APPLICATION_NAME);
    }

}
