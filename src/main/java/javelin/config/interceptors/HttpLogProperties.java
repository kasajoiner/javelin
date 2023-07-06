package javelin.config.interceptors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "logging.http")
public class HttpLogProperties {
    /**
     * Set of header names that would be included to logs.
     */
    private Set<String> headersInclude = new HashSet<>();
}
