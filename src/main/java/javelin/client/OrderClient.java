package javelin.client;

import javelin.dto.IncomingOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderClient {

    List<IncomingOrder> findOrders(LocalDateTime from, LocalDateTime to);

    Optional<IncomingOrder> findOrderById(Long id);
}
