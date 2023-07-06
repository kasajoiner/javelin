package javelin.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

@Component
public class LogbookRestTemplateCustomizer implements RestTemplateCustomizer {
    private final LogbookClientHttpRequestInterceptor interceptor;

    public LogbookRestTemplateCustomizer(LogbookClientHttpRequestInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void customize(RestTemplate restTemplate) {
        var existingInterceptors = restTemplate.getInterceptors();
        if (!existingInterceptors.contains(this.interceptor)) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(this.interceptor);
            interceptors.addAll(existingInterceptors);
            restTemplate.setInterceptors(interceptors);
        }
    }

}
