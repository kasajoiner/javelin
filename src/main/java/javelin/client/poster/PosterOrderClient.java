package javelin.client.poster;

import javelin.client.OrderClient;
import javelin.dto.ClientOrder;
import javelin.dto.poster.IncomingOrder;
import javelin.dto.poster.OrderResponse;
import javelin.dto.poster.OrdersResponse;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PosterOrderClient implements OrderClient {
    private static final DateTimeFormatter PARAM_FORMATTER
        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Map<Integer, Order.Status> STATUS_MAP = Map.of(
        0, Order.Status.NEW,
        1, Order.Status.ACCEPTED,
        7, Order.Status.CANCELLED
    );

    private static final Map<Integer, Order.Service> SERVICE_MAP = Map.of(
        1, Order.Service.DINEIN,
        2, Order.Service.DINEIN,
        3, Order.Service.DELIVERY
    );

    private final RestTemplate restTemplate;

    @Value("${order.get.url}")
    private String orderUrl;
    @Value("${order.list.url}")
    private String ordersUrl;
    @Value("${menu.token}")
    private String token;

    @Override
    public List<ClientOrder> findOrders(LocalDateTime from, LocalDateTime to) {
        var url = tokenize(ordersUrl)
            .queryParam("date_from", PARAM_FORMATTER.format(from))
            .queryParam("date_to", PARAM_FORMATTER.format(to));
        var r = restTemplate.getForEntity(url.toUriString(), OrdersResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody().response()
                .stream()
                .map(this::convert2Dto)
                .toList();
        }
        return List.of();
    }

    @Override
    public Optional<ClientOrder> findOrderById(Long id) {
        var url = tokenize(orderUrl)
            .queryParam("incoming_order_id", id);
        var r = restTemplate.getForEntity(url.toUriString(), OrderResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return Optional.ofNullable(r.getBody().incomingOrder())
                .map(this::convert2Dto);
        }
        return Optional.empty();
    }

    private ClientOrder convert2Dto(IncomingOrder io) {
        return ClientOrder.builder()
            .id(io.id())
            .transactionId(io.transactionId())
            .status(STATUS_MAP.get(io.status()))
            .service(SERVICE_MAP.get(io.serviceMode()))
            .phone(io.phone())
            .address(io.address())
            .price(io.countPrice())
            .created(io.createdAt())
            .updated(io.updatedAt())
            .build();
    }

    private UriComponentsBuilder tokenize(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).queryParam("token", token);
    }
}
