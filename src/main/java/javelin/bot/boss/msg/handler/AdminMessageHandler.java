package javelin.bot.boss.msg.handler;

import javelin.bot.cmd.ChatCommand;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Optional;

public interface AdminMessageHandler {

    BotApiMethod<?> handle(ChatCommand cc);

    String trigger();

    default Optional<String> validateKey(String key) {
        if (StringUtils.isBlank(key)) {
            return Optional.empty();
        }
        if (key.length() < 2) {
            return Optional.empty();
        }
        if (key.length() >= 100) {
            return Optional.empty();
        }
        return Optional.of(key);
    }
}
