package javelin.client.poster;

import javelin.client.StatusClient;
import javelin.dto.StatusResponse;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PosterStatusClient implements StatusClient {

    private final RestTemplate restTemplate;
    @Value("${order.status.url}")
    private String statusUrl;
    @Value("${menu.token}")
    private String token;


    @Override
    public Optional<Order.Status> getStatus(Long id) {
        var url = tokenize(statusUrl)
            .queryParam("transaction_id", id);

        var r = restTemplate.getForEntity(url.toUriString(), StatusResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody()
                .transaction()
                .stream()
                .findFirst()
                .map(t -> Order.Status.of(t.status()));
        }
        return Optional.empty();
    }

    private UriComponentsBuilder tokenize(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).queryParam("token", token);
    }
}
