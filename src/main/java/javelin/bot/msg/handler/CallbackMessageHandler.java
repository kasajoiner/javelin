package javelin.bot.msg.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.CommandType;

public interface CallbackMessageHandler {

    HandleResult handle(CallbackCommand command);

    CommandType commandType();

    @Getter
    @AllArgsConstructor
    class HandleResult {

        private final String text;
        private final InlineKeyboardMarkup inline;
        private final boolean previewPage;

        public HandleResult() {
            this.text = null;
            this.inline = null;
            this.previewPage = true;
        }

        public HandleResult(String text, InlineKeyboardMarkup inline) {
            this.text = text;
            this.inline = inline;
            this.previewPage = true;
        }
    }
}
