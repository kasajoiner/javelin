package javelin.scheduler;

import javelin.bot.client.BotRouter;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageQScheduler {

    private final MessageQService messageQService;
    private final BotRouter bot;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendMsgs() {
        for (var msg = messageQService.poll(); msg != null; msg = messageQService.poll()) {
            bot.sendNew(msg.getKey(), msg.getValue());
        }
    }
}
