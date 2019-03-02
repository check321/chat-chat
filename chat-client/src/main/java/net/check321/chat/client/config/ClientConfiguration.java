package net.check321.chat.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("chat")
@Data
public class ClientConfiguration {

    private String serverHost;

    private Integer serverPort;

    private Integer maxRetry;
}
