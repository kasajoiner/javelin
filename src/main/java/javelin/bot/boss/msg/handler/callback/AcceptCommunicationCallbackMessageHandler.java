package javelin.bot.boss.msg.handler.callback;

import javelin.bot.boss.msg.handler.EntityCallbackMessageHandler;
import javelin.bot.button.InlineMarkupBuilder;
import javelin.bot.client.EntityType;
import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AcceptCommunicationCallbackMessageHandler implements EntityCallbackMessageHandler {

    private final MessageTemplateContext templateContext;
    private final CommunicationService communicationService;

    @Override
    public HandleResult handle(CallbackCommand command) {
        var communication = communicationService.accept((Long) command.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.COMMUNICATION_ACCEPT,
            Map.of("id", communication.getId())
        );
        return new HandleResult(
            txt,
            new InlineMarkupBuilder().build()
        );
    }

    @Override
    public CommandType commandType() {
        return CommandType.ACCEPT;
    }

    @Override
    public EntityType entityType() {
        return EntityType.COMMUNICATION;
    }
}
