package javelin.bot.boss.msg.handler.common;

import javelin.bot.boss.msg.BossMessageService;
import javelin.bot.boss.msg.BossMsgName;
import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.bot.boss.msg.handler.AdminMessageHandler;
import javelin.bot.button.MenuKeyboardBuilder;
import javelin.bot.cmd.ChatCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CommunicationMessageHandler implements AdminMessageHandler {

    public static final String COMMUNICATION = "/communication";

    private final BossMessageService messageService;
    private final MenuKeyboardBuilder menuKeyboardBuilder;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var msg = messageService.findByName(BossMsgName.ADMIN_COMMUNICATION);
        return new SendMessageBuilder(cc.getChatId(), msg.getTxt())
            .replyMarkup(menuKeyboardBuilder.getMenu().build())
            .build();
    }

    @Override
    public String trigger() {
        return COMMUNICATION;
    }
}
