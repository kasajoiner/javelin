package javelin.scheduler;

import javelin.service.CheckOrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CheckOrderSchedule {

    private final CheckOrderManager manager;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        manager.checkOnlineOrders();
    }
}
