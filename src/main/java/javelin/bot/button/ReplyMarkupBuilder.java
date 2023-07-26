package javelin.bot.button;


import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyMarkupBuilder {

    private final List<KeyboardRow> keyboardRows = new ArrayList<>();

    public ReplyMarkupBuilder addButtons(List<String> texts) {
        KeyboardRow row = new KeyboardRow();
        keyboardRows.add(row);
        texts.forEach(row::add);
        return this;
    }

    public ReplyMarkupBuilder addButton(String text) {
        addButtons(List.of(text));
        return this;
    }

    public ReplyMarkupBuilder shareContact(String text) {
        KeyboardRow row = new KeyboardRow();
        keyboardRows.add(row);
        var btn = new KeyboardButton();
        btn.setText(text);
        btn.setRequestContact(true);
        row.add(btn);
        return this;
    }

    public ReplyKeyboardMarkup build() {
        ReplyKeyboardMarkup markupInline = new ReplyKeyboardMarkup();
        markupInline.setKeyboard(keyboardRows);
        markupInline.setOneTimeKeyboard(false);
        markupInline.setResizeKeyboard(true);
        markupInline.setSelective(true);
        return markupInline;
    }

}
