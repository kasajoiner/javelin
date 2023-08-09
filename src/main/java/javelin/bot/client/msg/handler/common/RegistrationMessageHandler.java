package javelin.bot.client.msg.handler.common;

import javelin.bot.client.ClientMessageService;
import javelin.bot.client.msg.ClientMsgName;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.client.msg.SendMessageBuilder;
import javelin.bot.button.ReplyMarkupBuilder;
import javelin.bot.client.msg.handler.MessageHandler;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.model.ClientRequest;
import javelin.service.ClientService;
import javelin.service.MessageQService;
import javelin.service.MessageTemplateService;
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
    private final ClientMessageService msgService;
    private final MessageQService qService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var client = clientService.save(new ClientRequest(cc.getChatId(), cc.getKey(), cc.getFrom().getUserName()));
        var text = templateContext.processEmojiTemplate(TemplateNames.REGISTER);
        var dineInBtn = templateContext.processTemplate(ButtonNames.DINE_IN);
        var orderBtn = templateContext.processTemplate(ButtonNames.ORDER);
        var keyboard = new ReplyMarkupBuilder()
            .addButtons(List.of(dineInBtn, orderBtn))
            .build();

        msgService.findByName(ClientMsgName.REGISTRATION_WARN)
                .ifPresent(mt -> qService.push(client.getId(), mt.getTxt()));
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(keyboard)
            .build();
    }

    @Override
    public String trigger() {
        return REG;
    }
}
