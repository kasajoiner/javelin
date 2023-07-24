package javelin.service;

import javelin.client.OrderClient;
import javelin.client.StatusClient;
import javelin.dto.ClientOrder;
import javelin.entity.Client;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderManager {

    private final OrderClient orderClient;
    private final StatusClient statusClient;
    private final OrderService orderService;
    private final ClientService clientService;
    private final TimeService timeService;
    private final ClientNotificationService notificationService;

    public void checkOrders() {

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

    public Order handleStatusChange(Long id, Order.Status status) {
        return orderService.findById(id)
            .filter(o -> o.getService() == Order.Service.DINEIN)
            .map(o -> {
                o.setStatus(status);
                var co = ClientOrder.from(o);
                return handleStatusChange(co, o);
            })
            .orElse(null);
    }

    private Order handleExisting(ClientOrder co, Order o) {
        if (o.isFinished()) {
            return o;
        }
        var actualStatus = Optional.ofNullable(co.getTransactionId())
            .flatMap(statusClient::getStatus)
            .orElseGet(co::getStatus);
        if (o.getStatus() != actualStatus && o.getStatus().ordinal() < actualStatus.ordinal()) {
            o.setStatus(actualStatus);
            return handleStatusChange(co, o);
        }
        return o;
    }

    @NotNull
    private Order handleStatusChange(ClientOrder co, Order o) {
        var c = clientService.findById(o.getClientId()).orElseThrow();
        var synced = orderService.sync(co, c);
        synced.setStatus(o.getStatus());
        var updated = orderService.update(synced);
        log.info("order {} update", updated);
        notificationService.notify(c, updated);
        return synced;
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
