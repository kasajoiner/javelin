package javelin.bot.boss.msg;

import javelin.bot.cmd.CommandTranslator;
import javelin.bot.boss.msg.handler.common.HelpMessageHandler;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
            msg2cmd.put(t.processTemplate(ButtonNames.HELP), HelpMessageHandler.HELP);
            }
        );
    }

    @Override
    public String translate(String text) {
        return msg2cmd.get(text);
    }
}
