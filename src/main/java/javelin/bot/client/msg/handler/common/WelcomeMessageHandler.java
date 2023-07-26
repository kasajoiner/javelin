package javelin.bot.client.msg.handler.common;

import javelin.bot.client.msg.button.ReplyMarkupBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import javelin.bot.client.cmd.ChatCommand;
import javelin.bot.client.msg.SendMessageBuilder;
import javelin.bot.client.msg.handler.MessageHandler;
import javelin.bot.client.msg.template.MessageTemplateContext;

import static javelin.bot.client.msg.template.TemplateNames.WELCOME;
import static javelin.bot.client.msg.template.ButtonNames.CONTACT;

@Component
@Slf4j
@RequiredArgsConstructor
public class WelcomeMessageHandler implements MessageHandler {

    private final MessageTemplateContext templateContext;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var text = templateContext.processTemplate(WELCOME);
        var contact = templateContext.processTemplate(CONTACT);
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(new ReplyMarkupBuilder().shareContact(contact).build())
            .enableHtml()
            .build();
    }

    @Override
    public String trigger() {
        return "/start";
    }
}
