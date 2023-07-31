package javelin.bot.client.msg;

import javelin.bot.LangUtils;
import javelin.bot.client.msg.handler.common.RegistrationMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javelin.bot.client.CommandTranslator;
import javelin.bot.client.msg.handler.common.HelpMessageHandler;
import javelin.bot.client.msg.handler.menu.OrderOptionMenuHandler;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageToCommandTranslator implements CommandTranslator {

    private final Map<String, String> msg2cmd = new HashMap<>();

    private final MessageTemplateContext t;

    @PostConstruct
    public void initContainer() {
        LangUtils.SUPPORTED.forEach(l -> {
            msg2cmd.put(t.processTemplate(ButtonNames.CONTACT), RegistrationMessageHandler.REG);
            msg2cmd.put(t.processTemplate(ButtonNames.HELP), HelpMessageHandler.HELP);
            msg2cmd.put(t.processTemplate(ButtonNames.DINE_IN), OrderOptionMenuHandler.OPTION);
            msg2cmd.put(t.processTemplate(ButtonNames.ORDER), OrderOptionMenuHandler.OPTION);
            }
        );
    }

    @Override
    public String translate(String text) {
        return msg2cmd.get(text);
    }
}
