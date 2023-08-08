package javelin.bot.boss.msg.handler.common;

import javelin.bot.boss.msg.BossMessageService;
import javelin.bot.boss.msg.BossMsgName;
import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.bot.boss.msg.handler.AdminMessageHandler;
import javelin.bot.button.InlineMarkupBuilder;
import javelin.bot.client.EntityType;
import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CommunicationMessageHandler implements AdminMessageHandler {

    public static final String COMMUNICATION = "/communication";

    private final MessageTemplateContext templateContext;
    private final BossMessageService messageService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        var msg = messageService.findByName(BossMsgName.ADMIN_COMMUNICATION);
        var txt = templateContext.processTemplate(ButtonNames.COMMUNICATION);
        var cmd = CallbackCommand.of(CommandType.MAKE, EntityType.COMMUNICATION, 0).toString();
        var keyboard = new InlineMarkupBuilder()
            .button(txt, cmd)
            .build();
        return new SendMessageBuilder(cc.getChatId(), msg.getTxt())
            .replyMarkup(keyboard)
            .build();
    }

    @Override
    public String trigger() {
        return COMMUNICATION;
    }
}
