package javelin.scheduler;

import javelin.bot.client.ClientBot;
import javelin.entity.Client;
import javelin.entity.Communication;
import javelin.service.ClientService;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageQScheduler {

    private final MessageQService messageQService;
    private final ClientService clientService;
    private final ClientBot bot;
    private final TaskScheduler taskScheduler;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendMsgs() {
        for (var msg = messageQService.poll(); msg != null; msg = messageQService.poll()) {
            bot.sendNew(msg.getKey(), msg.getValue());
        }

        sendCommunications();
    }

    private void sendCommunications() {
        for (var msg = messageQService.pollCommunication(); msg != null; msg = messageQService.pollCommunication()) {
            var receivers = clientService.findByReceiver(msg.getReceiver())
                    .stream()
                    .filter(c -> c.getId() > 0)
                    .toList();

            var fileName = msg.getObjectUrl().substring(msg.getObjectUrl().lastIndexOf('/') + 1);

            notifyWithMsgFile(msg, receivers, fileName);
        }
    }

    private void notifyWithMsgFile(
        Communication msg,
        List<Client> receivers,
        String fileName
    ) {
        try (InputStream in = new URL(msg.getObjectUrl()).openStream()) {
            var tempPath = Files.createTempFile(fileName, null);
            Files.copy(in, tempPath, StandardCopyOption.REPLACE_EXISTING);
            var tempFile = tempPath.toFile();
            final var stableMsg = msg;
            notifyClients(receivers, stableMsg, tempFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void notifyClients(List<Client> receivers, Communication stableMsg, File tempFile) {
        int delay = 0;
        for (Client receiver : receivers) {
            taskScheduler.schedule(() -> {
                switch (stableMsg.getType()) {
                    case PHOTO -> bot.sendPhoto(receiver.getId(), tempFile, stableMsg.getTxt());
                    case VIDEO -> bot.sendVideo(receiver.getId(), tempFile, stableMsg.getTxt());
                }
            }, new Date(System.currentTimeMillis() + delay));
            delay += 100;
        }
    }
}
