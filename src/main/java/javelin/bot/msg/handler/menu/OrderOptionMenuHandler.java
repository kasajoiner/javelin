package javelin.bot.msg.handler.menu;

import javelin.bot.cmd.ChatCommand;
import javelin.bot.msg.button.ChatMenuButtonBuilder;
import javelin.bot.msg.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Component
@RequiredArgsConstructor
public class OrderOptionMenuHandler implements MessageHandler {

    public static final String OPTION = "/option";

    private final ChatMenuButtonBuilder chatMenuButtonBuilder;

    @Override
    public BotApiMethod<?> handle(ChatCommand cc) {
        return chatMenuButtonBuilder.build(cc.getChatId(), cc.getKey());
    }

    @Override
    public String trigger() {
        return OPTION;
    }
}
