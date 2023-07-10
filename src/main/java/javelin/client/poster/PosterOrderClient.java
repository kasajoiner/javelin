package javelin.client.poster;

import javelin.client.OrderClient;
import javelin.dto.IncomingOrder;
import javelin.dto.OrderResponse;
import javelin.dto.OrdersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PosterOrderClient implements OrderClient {
    private static final DateTimeFormatter PARAM_FORMATTER
        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private final RestTemplate restTemplate;

    @Value("${order.url}")
    private String orderUrl;
    @Value("${orders.url}")
    private String ordersUrl;
    @Value("${menu.token}")
    private String token;


    @Override
    public List<IncomingOrder> findOrders(LocalDateTime from, LocalDateTime to) {
        var url = tokenize(ordersUrl)
            .queryParam("incoming_order_id", PARAM_FORMATTER.format(from))
            .queryParam("date_from", PARAM_FORMATTER.format(from))
            .queryParam("date_to", PARAM_FORMATTER.format(to));
        var r = restTemplate.getForEntity(url.toUriString(), OrdersResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody().response();
        }
        return List.of();
    }

    @Override
    public Optional<IncomingOrder> findOrderById(Long id) {
        var url = tokenize(ordersUrl)
            .queryParam("incoming_order_id", id);
        var r = restTemplate.getForEntity(url.toUriString(), OrderResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return Optional.ofNullable(r.getBody().incomingOrder());
        }
        return Optional.empty();
    }

    private UriComponentsBuilder tokenize(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).queryParam("token", token);
    }
}
