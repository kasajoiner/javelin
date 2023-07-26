package javelin.bot.client;

import javelin.bot.client.msg.CallbackMessageHandlerManager;
import javelin.bot.client.msg.MessageHandlerManager;
import javelin.bot.client.msg.SendMessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotRouter extends TelegramLongPollingBot {

    private final MessageHandlerManager msgManager;
    private final CallbackMessageHandlerManager callbackManager;

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;

    @PostConstruct
    public void initBots() throws TelegramApiException {
        var botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                var msg = msgManager.manage(update.getMessage());
                if (msg != null) {
                    execute(msg);
                }
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
                //TODO: send to admin
                SendMessage m = SendMessage.builder()
                    .chatId(String.valueOf(update.getMessage().getChatId()))
                    .text(e.getMessage())
                    .build();
                try {
                    execute(m);
                } catch (TelegramApiException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        } else if (update.hasCallbackQuery()){
            var callbackQuery = update.getCallbackQuery();
            var m = callbackManager.manageCallback(
                callbackQuery.getMessage(),
                callbackQuery.getData()
            );
            if (m == null) {
                return;
            }
            try {
                execute(m);
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void sendNew(Long chatId, String text) {
        try {
            execute(new SendMessageBuilder(chatId, text).build());
        } catch (TelegramApiException e) {
            log.error("bot:message:failed:{}:{}", chatId, text);
        }
    }

    public void sendNew(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        try {
            execute(new SendMessageBuilder(chatId, text).replyMarkup(keyboard).build());
        } catch (TelegramApiException e) {
            log.error("bot:message:failed:{}:{}", chatId, text);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
