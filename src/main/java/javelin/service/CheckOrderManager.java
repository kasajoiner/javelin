package javelin.service;

import javelin.client.OrderClient;
import javelin.client.StatusClient;
import javelin.dto.IncomingOrder;
import javelin.entity.Client;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckOrderManager {

    private static final Set<Integer> ALLOWED_SERVICES = Set.of(2, 3);

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

            var incomingOrders = orderClient.findOrders(shiftStart, shiftEnd);
            log.info(
                "found {} order for today {} - {}",
                incomingOrders.size(),
                shiftStart,
                shiftEnd
            );
            incomingOrders
                .stream()
                .filter(io -> ALLOWED_SERVICES.contains(io.serviceMode()))
                .forEach(io -> {
                    orderService.findById(io.id())
                        .ifPresentOrElse(
                            o -> handleExisting(io, o),
                            () -> handleNew(io)
                        );
                });
        }
    }

    private void handleExisting(IncomingOrder io, Order o) {
        var actualStatus = statusClient.getStatus(io.transactionId())
            .orElseGet(() -> Order.Status.of(io.status()));
        if (o.getStatus() != actualStatus) {
            log.info("order {} status update {}", o, actualStatus);
            var c = clientService.findById(o.getClientId()).orElseThrow();
            var synced = orderService.sync(io, c);
            synced.setStatus(actualStatus);
            var updated = orderService.update(synced);
            notificationService.notify(c, updated);
        }
    }

    private void handleNew(IncomingOrder io) {
        clientService.findByPhone(io.phone())
            .ifPresentOrElse(
                c -> createNewOrder(io, c),
                () -> log.info("order {} is not made via app", io)
            );
    }

    private void createNewOrder(IncomingOrder io, Client c) {
        var saved = orderService.sync(io, c);
        log.info("new order {}", saved);
        notificationService.notify(c, saved);
    }
}
