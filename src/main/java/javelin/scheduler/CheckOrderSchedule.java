package javelin.scheduler;

import javelin.service.OrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CheckOrderSchedule {

    private final OrderManager manager;

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        manager.checkOrders();
    }
}
