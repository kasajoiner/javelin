package javelin.bot.client.msg.button;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javelin.bot.client.msg.template.ButtonNames;
import javelin.bot.client.msg.template.MessageTemplateContext;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuKeyboardBuilder {

    private final MessageTemplateContext templateContext;

    public ReplyMarkupBuilder getMenu() {
        var replyMarkup = new ReplyMarkupBuilder();
        var srcText = templateContext.processTemplate(ButtonNames.CONTACT);
        var helpText = templateContext.processTemplate(ButtonNames.HELP);
        replyMarkup.addButtons(List.of(srcText));
        replyMarkup.addButtons(List.of(helpText));
        return replyMarkup;
    }
}
