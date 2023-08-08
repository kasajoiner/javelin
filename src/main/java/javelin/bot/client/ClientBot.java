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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientBot extends TelegramLongPollingBot {

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

    public void sendPhoto(Long chatId, File file, String caption) {
        try  {
            var sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(chatId);
            sendPhotoRequest.setPhoto(new InputFile(file));
            if (caption != null) {
                sendPhotoRequest.setCaption(caption);
            }
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            log.error("bot:photo:failed:{}:{}", chatId, file.getName(), e);
        }
    }

    public void sendVideo(Long chatId, File file, String caption) {
        try {
            var sendVideo = new SendVideo();
            var inputFile = new InputFile(file);
            sendVideo.setVideo(inputFile);
            sendVideo.setChatId(chatId);

            if (caption != null) {
                sendVideo.setCaption(caption);
            }
            execute(sendVideo);
        } catch (TelegramApiException e) {
            log.error("bot:video:failed:{}:{}", chatId, file.getName(), e);
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
