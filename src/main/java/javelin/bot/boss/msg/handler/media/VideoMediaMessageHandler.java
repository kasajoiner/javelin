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
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static javelin.bot.client.EntityType.COMMUNICATION;

@Component
@RequiredArgsConstructor
public class VideoMediaMessageHandler implements MediaMessageHandler {

    private final CommunicationService communicationService;
    private final MessageTemplateContext templateContext;

    @Override
    public boolean isApplicable(Message msg) {
        return msg.getVideo() != null;
    }

    @Override
    public SendMediaBotMethod<?> handle(Message msg) {

        var sendVideo = new SendVideo();
        var inputFile = new InputFile(msg.getVideo().getFileId());
        sendVideo.setVideo(inputFile);
        sendVideo.setChatId(msg.getChatId());

        if (msg.getCaption() != null) {
            sendVideo.setCaption(msg.getCaption());
        }

        var communication = createNew(msg);

        var builder = new InlineMarkupBuilder();
        var accept = CallbackCommand.of(CommandType.ACCEPT, COMMUNICATION, communication.getId());
        var cancel = CallbackCommand.of(CommandType.CANCEL, COMMUNICATION, communication.getId());

        var yesTxt = templateContext.processEmojiTemplate(ButtonNames.YES);
        var noTxt = templateContext.processEmojiTemplate(ButtonNames.NO);

        builder.addButtons(List.of(
            builder.makeButton(yesTxt, accept.toString()),
            builder.makeButton(noTxt, cancel.toString())
        ));
        sendVideo.setReplyMarkup(builder.build());
        return sendVideo;
    }

    private Communication createNew(Message m) {
        var r = new NewCommunicationRequest(
            m.getVideo().getFileId(),
            Communication.Type.VIDEO,
            m.getCaption(),
            m.getChatId(),
            Receiver.ALL
        );

        return communicationService.createNew(r);
    }

}
