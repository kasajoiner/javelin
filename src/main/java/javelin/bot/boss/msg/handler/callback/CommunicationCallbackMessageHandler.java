package javelin.bot.boss.msg.handler.callback;

import javelin.bot.boss.msg.handler.EntityCallbackMessageHandler;
import javelin.bot.client.EntityType;
import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class CommunicationCallbackMessageHandler implements EntityCallbackMessageHandler {

    private final MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(CallbackCommand command) {
        String txt = templateContext.processTemplate(TemplateNames.COMMUNICATION);
        return new HandleResult(
            txt,
            new InlineKeyboardMarkup()
        );
    }

    @Override
    public CommandType commandType() {
        return CommandType.MAKE;
    }

    @Override
    public EntityType entityType() {
        return EntityType.COMMUNICATION;
    }
}
