package javelin.bot.boss;

import javelin.bot.boss.msg.BossCallbackMessageHandlerManager;
import javelin.bot.boss.msg.BossMessageHandlerManager;
import javelin.bot.boss.msg.MediaMessageHandlerManager;
import javelin.bot.boss.msg.SendMessageBuilder;
import javelin.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BossBot extends TelegramLongPollingBot {

    private final MediaMessageHandlerManager mediaManager;
    private final BossMessageHandlerManager msgManager;
    private final BossCallbackMessageHandlerManager callbackManager;

    @Value("${bot.boss.name}")
    private String name;

    @Value("${bot.boss.token}")
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

        try {
            if (update.hasMessage()) {
                var media = Optional.ofNullable(mediaManager.manage(update.getMessage()))
                    .orElse(null);
                if (media != null) {
                    if (media instanceof SendPhoto photo) {
                        execute(photo);
                    } else if (media instanceof SendVideo video) {
                        execute(video);
                    }
                    updateMediaUrl(media.getFile().getAttachName());
                } else {
                    execute(msgManager.manage(update.getMessage()));
                }
            } else if (update.hasCallbackQuery()) {
                var callbackQuery = update.getCallbackQuery();
                var m = callbackManager.manageCallback(
                    callbackQuery.getMessage(),
                    callbackQuery.getData()
                );
                if (m == null) {
                    return;
                }
                execute(m);
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
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

    private void updateMediaUrl(String objectId) throws TelegramApiException {
        var getFileMethod = new GetFile();
        getFileMethod.setFileId(objectId);
        var file = execute(getFileMethod);
        var fileUrl = file.getFileUrl(token);
        mediaManager.updateMediaUrl(objectId, fileUrl);
    }
}
