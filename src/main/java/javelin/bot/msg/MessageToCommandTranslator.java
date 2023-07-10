package javelin.bot.msg;

import javelin.bot.msg.handler.common.RegistrationMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javelin.bot.cmd.CommandTranslator;
import javelin.bot.msg.handler.common.HelpMessageHandler;
import javelin.bot.msg.handler.common.SettingsCommonMessageHandler;
import javelin.bot.msg.template.ButtonNames;
import javelin.bot.msg.template.MessageTemplateContext;

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
            msg2cmd.put(
                t.processTemplate(ButtonNames.SETTINGS),
                SettingsCommonMessageHandler.SETTINGS
            );
            }
        );
    }

    @Override
    public String translate(String text) {
        return msg2cmd.get(text);
    }
}
