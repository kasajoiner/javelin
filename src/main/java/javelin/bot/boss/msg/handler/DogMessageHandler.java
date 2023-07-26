package javelin.bot.boss.msg.handler;

import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.bot.cmd.ChatCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DogMessageHandler implements IDefaultMessageHandler {

    @Override
    public BotApiMethod<?> handle(ChatCommand cc) {
        return new SendMessageBuilder(cc.getChatId(), "Ти не босс. Ти пес!").build();
    }

    @Override
    public String trigger() {
        return null;
    }
}
