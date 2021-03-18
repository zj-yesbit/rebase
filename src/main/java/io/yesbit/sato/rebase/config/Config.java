package io.yesbit.sato.rebase.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "sato")
@PropertySource(value = "classpath:sato.yml")
@Setter
@Getter
public class Config {
    @Value("${sato.tokenAddress}")
    private String tokenAddress;

    @Value("${sato.monetaryPolicy}")
    private String monetaryPolicy;

    @Value("${sato.orchestrator}")
    private String orchestrator;

    @Value("${sato.marketOracle}")
    private String marketOracle;

    @Value("${sato.provider}")
    private String Provider;

    @Value("${sato.providerKey}")
    private String ProviderKey;

}
