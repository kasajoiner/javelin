package javelin.bot.msg.handler.common;

import javelin.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.msg.SendMessageBuilder;
import javelin.bot.msg.button.MenuKeyboardBuilder;
import javelin.bot.msg.handler.MessageHandler;
import javelin.bot.msg.template.MessageTemplateContext;
import javelin.bot.msg.template.TemplateNames;

@Component
@RequiredArgsConstructor
public class HelpMessageHandler implements MessageHandler {

    public static final String HELP = "/help";

    private final MessageTemplateContext templateContext;
    private final MenuKeyboardBuilder menuKeyboardBuilder;
    private final ClientService clientService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var reader = clientService.findById(cc.getChatId()).orElseThrow();
        var text = templateContext.processTemplate(TemplateNames.HELP);
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(menuKeyboardBuilder.getMenu().build())
            .build();
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
