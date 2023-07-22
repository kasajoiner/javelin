package javelin.service;

import javelin.client.OrderClient;
import javelin.dto.ClientOrder;
import javelin.dto.poster.IncomingOrder;
import javelin.dto.poster.WebhookRequest;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        var client = clientService.findByPhone(providerOrder.getPhone())
            .orElseThrow();
        var order = sync(providerOrder, client);
        notificationService.notify(client, order);
        return Optional.of(orderRepository.save(order));
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order sync(ClientOrder co, Client c) {
        var order = orderRepository.findById(co.getId())
            .orElseGet(() -> {
                var o = new Order();
                o.setId(co.getId());
                o.setCreated(co.getCreated());
                return o;
            });
        order.setClientId(c.getId());
        order.setPrice(co.getPrice());
        order.setUpdated(co.getUpdated());
        order.setAddress(co.getAddress() == null ? "-" : co.getAddress());
        order.setService(co.getService());
        order.setStatus(co.getStatus());
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Order o) {
        return orderRepository.save(o);
    }
}
