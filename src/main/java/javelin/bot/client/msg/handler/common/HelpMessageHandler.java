package javelin.bot.client.msg.handler.common;

import javelin.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.client.msg.SendMessageBuilder;
import javelin.bot.client.msg.handler.MessageHandler;
import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;

@Component
@RequiredArgsConstructor
public class HelpMessageHandler implements MessageHandler {

    public static final String HELP = "/help";

    private final MessageTemplateContext templateContext;
    private final ClientService clientService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var reader = clientService.findById(cc.getChatId()).orElseThrow();
        var text = templateContext.processTemplate(TemplateNames.HELP);
        return new SendMessageBuilder(cc.getChatId(), text)
            .build();
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
