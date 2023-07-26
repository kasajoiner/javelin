package javelin.bot.button;

import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

@Component
@RequiredArgsConstructor
public class ChatMenuButtonBuilder {

    private final MessageTemplateContext templateContext;

    @Value("${web.url}")
    private String webUrl;

    @Value("${dinein.url}")
    private String dineInUrl;


    public SetChatMenuButton build(Long chatId, String txt) {
        var webApp = define(txt);
        return SetChatMenuButton.builder()
            .chatId(chatId)
            .menuButton(webApp)
            .build();
    }

    private MenuButtonWebApp define(String txt) {
        var dineInBtn = templateContext.processTemplate(ButtonNames.DINE_IN);
        var orderBtn = templateContext.processTemplate(ButtonNames.ORDER);

        if (dineInBtn.equals(txt)) {
            return makeIt(dineInBtn, dineInUrl);
        }

        if (orderBtn.equals(txt)) {
            return makeIt(orderBtn, webUrl);
        }
        return null;
    }

    private MenuButtonWebApp makeIt( String txt, String url) {
        var webAppInfo = WebAppInfo.builder().url(url).build();
        return MenuButtonWebApp.builder()
            .text(txt)
            .webAppInfo(webAppInfo)
            .build();
    }
}
