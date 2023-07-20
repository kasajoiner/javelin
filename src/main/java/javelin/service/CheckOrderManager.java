package javelin.service;

import javelin.client.OrderClient;
import javelin.client.StatusClient;
import javelin.dto.IncomingOrder;
import javelin.entity.Client;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckOrderManager {

    private final OrderClient orderClient;
    private final StatusClient statusClient;
    private final OrderService orderService;
    private final ClientService clientService;
    private final TimeService timeService;
    private final NotificationService notificationService;

    public void checkOnlineOrders() {

        if (timeService.isOpenNow()) {
            var shiftStart = timeService.shiftStart();
            var shiftEnd = timeService.shiftEnd();

            var actualOrders = orderClient.findOrders(shiftStart, shiftEnd)
                .stream()
                .map(io -> orderService.findById(io.id())
                    .filter(Order::isNotFinished)
                    .map(o -> handleExisting(io, o))
                    .orElseGet(() -> handleNew(io)))
                .toList();

            log.info("actual orders {}", actualOrders);
        }
    }

    private Order handleExisting(IncomingOrder io, Order o) {
        var actualStatus = Optional.ofNullable(io.transactionId())
            .flatMap(statusClient::getStatus)
            .orElseGet(() -> Order.Status.of(io.status()));
        if (o.getStatus() != actualStatus) {
            log.info("order {} status update {}", o, actualStatus);
            var c = clientService.findById(o.getClientId()).orElseThrow();
            var synced = orderService.sync(io, c);
            synced.setStatus(actualStatus);
            var updated = orderService.update(synced);
            notificationService.notify(c, updated);
            return updated;
        }
        return o;
    }

    private Order handleNew(IncomingOrder io) {
        return clientService.findByPhone(io.phone())
            .map(c -> createNewOrder(io, c))
            .orElseGet(() -> {
                log.info("order {} is not made via app", io);
                var c = clientService.findById(-1L).orElseThrow();
                return createNewOrder(io, c);
            });
    }

    private Order createNewOrder(IncomingOrder io, Client c) {
        var saved = orderService.sync(io, c);
        log.info("new order {}", saved);
        notificationService.notify(c, saved);
        return saved;
    }
}
