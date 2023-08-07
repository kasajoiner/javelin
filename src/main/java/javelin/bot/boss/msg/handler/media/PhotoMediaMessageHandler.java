package javelin.bot.boss.msg.handler.media;

import javelin.bot.boss.msg.handler.MediaMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;

@Component
public class PhotoMediaMessageHandler implements MediaMessageHandler {

    @Override
    public boolean isApplicable(Message msg) {
        return msg.getPhoto() != null;
    }

    @Override
    public SendMediaBotMethod<?> handle(Message msg) {
        var photos = msg.getPhoto();
        var photo = photos.stream()
            .max(Comparator.comparing(PhotoSize::getFileSize))
            .orElse(null);

        var sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(msg.getChatId());
        sendPhotoRequest.setPhoto(new InputFile(photo.getFileId()));
        if (msg.getCaption() != null) {
            sendPhotoRequest.setCaption(msg.getCaption());
        }
        return sendPhotoRequest;
    }
}
