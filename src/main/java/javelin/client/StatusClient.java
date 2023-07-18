package javelin.client;

import javelin.entity.Order;

import java.util.Optional;

public interface StatusClient {
    Optional<Order.Status> getStatus(Long id);
}
