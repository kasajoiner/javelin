package javelin.service;

import javelin.client.OrderClient;
import javelin.client.StatusClient;
import javelin.dto.ClientOrder;
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
                .map(co -> orderService.findById(co.getId())
                    .map(o -> handleExisting(co, o))
                    .orElseGet(() -> handleNew(co)))
                .filter(o -> !o.isFinished())
                .toList();

            log.info("actual orders {}", actualOrders);
        }
    }

    private Order handleExisting(ClientOrder co, Order o) {
        if (o.isFinished()) {
            return o;
        }
        var actualStatus = Optional.ofNullable(co.getTransactionId())
            .flatMap(statusClient::getStatus)
            .orElseGet(co::getStatus);
        if (o.getStatus() != actualStatus) {
            var c = clientService.findById(o.getClientId()).orElseThrow();
            var synced = orderService.sync(co, c);
            synced.setStatus(actualStatus);
            log.info("order {} update", o);
            var updated = orderService.update(synced);
            notificationService.notify(c, updated);
            return updated;
        }
        return o;
    }

    private Order handleNew(ClientOrder co) {
        return clientService.findByPhone(co.getPhone())
            .map(c -> createNewOrder(co, c))
            .orElseGet(() -> {
                log.info("order {} is not made via app", co);
                var c = clientService.findById(-1L).orElseThrow();
                return createNewOrder(co, c);
            });
    }

    private Order createNewOrder(ClientOrder co, Client c) {
        var saved = orderService.sync(co, c);
        log.info("new order {}", saved);
        notificationService.notify(c, saved);
        return saved;
    }
}
