package javelin.bot.boss.msg;

import javelin.bot.boss.msg.handler.MediaMessageHandler;
import javelin.service.CommunicationService;
import javelin.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaMessageHandlerManager {

    private final List<MediaMessageHandler> mediaMessageHandlers;
    private final EmployeeService employeeService;
    private final CommunicationService communicationService;

    public SendMediaBotMethod<?> manage(Message message) {
        return employeeService.findBossById(message.getChatId())
            .map(e -> getMediaHandler(message))
            .filter(Objects::nonNull)
            .map(h -> h.handle(message))
            .orElse(null);
    }

    private MediaMessageHandler getMediaHandler(Message msg) {
        return mediaMessageHandlers.stream()
            .filter(h -> h.isApplicable(msg))
            .findFirst()
            .orElse(null);
    }

    public void updateMediaUrl(String objectId, String url) {
        communicationService.updateUrl(objectId, url);
    }

}
