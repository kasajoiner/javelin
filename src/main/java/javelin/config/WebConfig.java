package javelin.config;

import javelin.config.interceptors.HttpLogProperties;
import org.apache.http.HttpHeaders;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.CorrelationId;
import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.HeaderFilters;

import java.time.Duration;

@Configuration
public class WebConfig {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;

    @Bean
    public HeaderFilter headerFilter(HttpLogProperties httpLogProperties) {
        return HeaderFilters.removeHeaders(header -> !httpLogProperties.getHeadersInclude().contains(header));
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            .setBufferRequestBody(true)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
            .setReadTimeout(Duration.ofMillis(READ_TIMEOUT))
            .build();
    }

    @Bean
    public CorrelationId correlationId() {
        return new UUIDCorrelationId();
    }
}