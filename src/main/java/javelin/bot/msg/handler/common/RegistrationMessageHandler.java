package javelin.bot.msg.handler.common;

import javelin.bot.cmd.ChatCommand;
import javelin.bot.msg.SendMessageBuilder;
import javelin.bot.msg.button.ReplyMarkupBuilder;
import javelin.bot.msg.handler.MessageHandler;
import javelin.bot.msg.template.ButtonNames;
import javelin.bot.msg.template.MessageTemplateContext;
import javelin.bot.msg.template.TemplateNames;
import javelin.model.ClientRequest;
import javelin.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegistrationMessageHandler implements MessageHandler {

    public static final String REG = "/reg";

    private final MessageTemplateContext templateContext;
    private final ClientService clientService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var client = clientService.save(new ClientRequest(cc.getChatId(), cc.getKey()));
        var text = templateContext.processEmojiTemplate(TemplateNames.REGISTER);
        var dineInBtn = templateContext.processTemplate(ButtonNames.DINE_IN);
        var orderBtn = templateContext.processTemplate(ButtonNames.ORDER);
        var keyboard = new ReplyMarkupBuilder()
            .addButtons(List.of(dineInBtn, orderBtn))
            .build();
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(keyboard)
            .build();
    }

    @Override
    public String trigger() {
        return REG;
    }
}
