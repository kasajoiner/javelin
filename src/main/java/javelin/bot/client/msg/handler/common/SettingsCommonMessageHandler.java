package javelin.bot.client.msg.handler.common;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import javelin.bot.client.cmd.CallbackCommand;
import javelin.bot.client.cmd.ChatCommand;
import javelin.bot.client.cmd.CommandType;
import javelin.bot.client.msg.SendMessageBuilder;
import javelin.bot.client.msg.handler.MessageHandler;

@Component
public class SettingsCommonMessageHandler implements MessageHandler {

    public static final String SETTINGS = "/settings";

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        CallbackCommand callbackCmd = CallbackCommand.of(CommandType.SETTINGS);
        callbackCmd.setChatId(cc.getChatId());

        return new SendMessageBuilder(cc.getChatId(), "Settings")
            .enableHtml()
            .build();
    }

    @Override
    public String trigger() {
        return SETTINGS;
    }

}
