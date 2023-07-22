package javelin.client;

import javelin.dto.ClientOrder;
import javelin.dto.poster.IncomingOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderClient {

    List<ClientOrder> findOrders(LocalDateTime from, LocalDateTime to);

    Optional<ClientOrder> findOrderById(Long id);
}
