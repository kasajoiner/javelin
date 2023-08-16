package javelin.service;

import javelin.entity.Client;
import javelin.entity.Order;
import javelin.processor.OrderStatusProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {
    private final List<OrderStatusProcessor> processors;

    public void notify(Client c, Order o) {
        processors.stream()
            .filter(p -> p.isApplicable(o))
            .forEach(p -> p.process(c, o));
    }

}
