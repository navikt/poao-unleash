package no.nav.poao_unleash.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.env")
public class EnvironmentProperties {
    private String unleashUrl;
    private String azureAdDiscoveryUrl;
    private String azureAdClientId;
    private String axsysUrl;
    private String axsysScope;
}
