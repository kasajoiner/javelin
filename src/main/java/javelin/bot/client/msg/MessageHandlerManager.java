package javelin.bot.client.msg;

import javelin.bot.client.msg.handler.common.RegistrationMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import javelin.bot.client.cmd.ChatCommandParser;
import javelin.bot.client.cmd.CommandTranslator;
import javelin.bot.client.msg.handler.IDefaultMessageHandler;
import javelin.bot.client.msg.handler.MessageHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageHandlerManager {

    private final Map<String, MessageHandler> commonMessageHandlers;
    private final IDefaultMessageHandler defaultMessageHandler;
    private final CommandTranslator txt2Cmd;

    public MessageHandlerManager(
        @Autowired List<MessageHandler> commonMessageHandlers,
        @Autowired IDefaultMessageHandler defaultMessageHandler,
        @Autowired CommandTranslator txt2Cmd
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(Collectors.toMap(MessageHandler::trigger, Function.identity()));
        this.defaultMessageHandler = defaultMessageHandler;
        this.txt2Cmd = txt2Cmd;
    }

    public BotApiMethod<?> manage(Message message) {
        if (message.getContact() != null) {
            message.setText(
                RegistrationMessageHandler.REG + " " + message.getContact().getPhoneNumber()
            );
        }
        var cc = ChatCommandParser.parse(message.getText(), message.getChatId(), message.getFrom());
        return getCommonHandler(cc.getTrigger()).handle(cc);
    }

    private MessageHandler getCommonHandler(String text) {
        var commonMessageHandler = commonMessageHandlers.get(text);
        if (commonMessageHandler != null) {
            return commonMessageHandler;
        }
        var cmd = txt2Cmd.translate(text);
        return cmd != null ? commonMessageHandlers.get(cmd) : defaultMessageHandler;
    }

}
