package javelin.bot.boss.msg.handler.common;

import javelin.bot.boss.msg.BossMessageService;
import javelin.bot.boss.msg.BossMsgName;
import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.bot.boss.msg.handler.AdminMessageHandler;
import javelin.bot.cmd.ChatCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class BossHelpMessageHandler implements AdminMessageHandler {

    public static final String HELP = "/help";

    private final BossMessageService messageService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var msg = messageService.findByName(BossMsgName.ADMIN_HELP);
        return new SendMessageBuilder(cc.getChatId(), msg.getTxt())
            .build();
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
