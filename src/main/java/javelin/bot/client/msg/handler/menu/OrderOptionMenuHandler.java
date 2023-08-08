package javelin.bot.client.msg.handler.menu;

import javelin.bot.client.ClientMessageService;
import javelin.bot.client.msg.ClientMsgName;
import javelin.bot.cmd.ChatCommand;
import javelin.bot.client.ChatMenuButtonBuilder;
import javelin.bot.client.msg.handler.MessageHandler;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Component
@RequiredArgsConstructor
public class OrderOptionMenuHandler implements MessageHandler {

    public static final String OPTION = "/option";

    private final ChatMenuButtonBuilder chatMenuButtonBuilder;
    private final MessageTemplateContext tmplCxt;
    private final ClientMessageService msgService;
    private final MessageQService qService;

    @Override
    public BotApiMethod<?> handle(ChatCommand cc) {
        var dineInBtn = tmplCxt.processTemplate(ButtonNames.DINE_IN);
        var orderBtn = tmplCxt.processTemplate(ButtonNames.ORDER);
        var txt = cc.getKey();

        if (dineInBtn.equals(txt)) {
            msgService.findByName(ClientMsgName.DINEIN_OPTION)
                    .ifPresent(msg -> qService.push(cc.getChatId(), msg.getTxt()));
        }

        if (orderBtn.equals(txt)) {
            msgService.findByName(ClientMsgName.DELIVERY_OPTION)
                .ifPresent(msg -> qService.push(cc.getChatId(), msg.getTxt()));
        }
        return chatMenuButtonBuilder.build(cc.getChatId(), txt);
    }

    @Override
    public String trigger() {
        return OPTION;
    }
}
