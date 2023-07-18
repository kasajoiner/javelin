package javelin.service;

import javelin.client.OrderClient;
import javelin.dto.IncomingOrder;
import javelin.dto.WebhookRequest;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ClientService clientService;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;
    private final OrderClient orderClient;

    public Optional<Order> handleEvent(WebhookRequest r) {
        var providerOrder = orderClient.findOrderById(r.objectId())
            .orElseThrow();
        var client = clientService.findByPhone(providerOrder.phone())
            .orElseThrow();
        var order = sync(providerOrder, client);
        notificationService.notify(client, order);
        return Optional.of(orderRepository.save(order));
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order sync(IncomingOrder io, Client c) {
        var order = orderRepository.findById(io.id())
            .orElseGet(() -> {
                var o = new Order();
                o.setId(io.id());
                o.setCreated(io.createdAt());
                return o;
            });
        order.setClientId(c.getId());
        order.setPrice(io.countPrice());
        order.setUpdated(io.updatedAt());
        order.setAddress(io.address() == null ? "-" : io.address());
        order.setStatus(Order.Status.of(io.status()));
        return orderRepository.save(order);
    }

    public Order update(Order o) {
        return orderRepository.save(o);
    }
}
