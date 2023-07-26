package javelin.bot.boss.msg.handler.common;

import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.bot.boss.msg.handler.AdminMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SettingsCommonMessageHandler implements AdminMessageHandler {

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
