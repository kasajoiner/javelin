package javelin.bot.boss.msg.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MediaMessageHandler {

    boolean isApplicable(Message msg);

    SendMediaBotMethod<?> handle(Message msg);
}
