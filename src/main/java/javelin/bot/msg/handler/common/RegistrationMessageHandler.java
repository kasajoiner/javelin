package javelin.bot.msg.handler.common;

import javelin.bot.cmd.ChatCommand;
import javelin.bot.msg.SendMessageBuilder;
import javelin.bot.msg.button.InlineMarkupBuilder;
import javelin.bot.msg.button.MenuKeyboardBuilder;
import javelin.bot.msg.handler.MessageHandler;
import javelin.bot.msg.template.ButtonNames;
import javelin.bot.msg.template.MessageTemplateContext;
import javelin.bot.msg.template.TemplateNames;
import javelin.model.ClientRequest;
import javelin.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class RegistrationMessageHandler implements MessageHandler {

    public static final String REG = "/reg";

    private final MessageTemplateContext templateContext;
    private final ClientService clientService;

    @Value("${web.url}")
    private String webUrl;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var client = clientService.save(new ClientRequest(cc.getChatId(), cc.getKey()));
        var text = templateContext.processTemplate(TemplateNames.REGISTER);
        var orderBtn = templateContext.processTemplate(ButtonNames.ORDER);
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(new InlineMarkupBuilder().addButton(orderBtn, webUrl).build())
            .build();
    }

    @Override
    public String trigger() {
        return REG;
    }
}
