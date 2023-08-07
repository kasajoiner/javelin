package javelin.bot.boss.msg.handler.media;

import javelin.bot.boss.msg.handler.MediaMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class VideoMediaMessageHandler implements MediaMessageHandler {

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
        return sendVideo;
    }

}
