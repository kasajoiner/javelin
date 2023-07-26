package javelin.bot.client.cmd;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;

@Component
public class ChatCommandParser {

    public static ChatCommand parse(String text, long chatId, User from) {
        Pair<String, String> keys = parseKeys(text);
        return new ChatCommand(keys.getLeft(), keys.getRight(), chatId, from);
    }

    private static Pair<String, String> parseKeys(String text) {
        switch (text.charAt(0)) {
            case '#' -> {
                return Pair.of("#", text.substring(1));
            }
            case '/' -> {
                String[] keys = text.split("[ _]");
                if (keys.length > 1) {
                    return Pair.of(
                        keys[0],
                        String.join("", Arrays.copyOfRange(keys, 1, keys.length))
                    );
                }
                return Pair.of(keys[0], "");
            }
            default -> {
                return Pair.of(text, text);
            }
        }
    }
}
