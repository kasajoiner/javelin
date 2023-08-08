package javelin.bot.boss.msg.handler.media;

import javelin.bot.boss.msg.handler.MediaMessageHandler;
import javelin.bot.button.InlineMarkupBuilder;
import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.template.ButtonNames;
import javelin.bot.template.MessageTemplateContext;
import javelin.dto.NewCommunicationRequest;
import javelin.entity.Communication;
import javelin.entity.Receiver;
import javelin.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;

import static javelin.bot.client.EntityType.COMMUNICATION;

@Component
@RequiredArgsConstructor
public class PhotoMediaMessageHandler implements MediaMessageHandler {

    private final CommunicationService communicationService;
    private final MessageTemplateContext templateContext;

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

        var communication = createNew(msg, photo);

        var builder = new InlineMarkupBuilder();
        var accept = CallbackCommand.of(CommandType.ACCEPT, COMMUNICATION, communication.getId());
        var cancel = CallbackCommand.of(CommandType.CANCEL, COMMUNICATION, communication.getId());

        var yesTxt = templateContext.processEmojiTemplate(ButtonNames.YES);
        var noTxt = templateContext.processEmojiTemplate(ButtonNames.NO);

        builder.addButtons(List.of(
            builder.makeButton(yesTxt, accept.toString()),
            builder.makeButton(noTxt, cancel.toString())
        ));
        sendPhotoRequest.setReplyMarkup(builder.build());
        return sendPhotoRequest;
    }

    private Communication createNew(Message m, PhotoSize photo) {
        var r = new NewCommunicationRequest(
            photo.getFileId(),
            Communication.Type.PHOTO,
            m.getCaption(),
            m.getChatId(),
            Receiver.ALL
        );

        return communicationService.createNew(r);
    }
}
