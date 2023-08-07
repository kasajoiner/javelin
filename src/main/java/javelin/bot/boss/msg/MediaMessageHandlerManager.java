package javelin.bot.boss.msg;

import javelin.bot.boss.msg.handler.MediaMessageHandler;
import javelin.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MediaMessageHandlerManager {

    private final List<MediaMessageHandler> mediaMessageHandlers;
    private final EmployeeService employeeService;

    public MediaMessageHandlerManager(
        @Autowired List<MediaMessageHandler> mediaMessageHandlers,
        @Autowired EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
        this.mediaMessageHandlers = mediaMessageHandlers;
    }

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

}
