package javelin.config.customizers;

import lombok.SneakyThrows;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.cert.X509Certificate;

@Component
@ConditionalOnProperty(name = "rest.template.ssl.enabled", havingValue = "true")
public class SslRestTemplateCustomizer implements RestTemplateCustomizer {

    @SneakyThrows
    @Override
    public void customize(RestTemplate restTemplate) {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        var sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        var sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        var closeableHttpClient = HttpClients.custom()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(closeableHttpClient));
    }
}