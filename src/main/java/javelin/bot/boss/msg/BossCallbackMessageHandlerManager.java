package javelin.bot.boss.msg;

import javelin.bot.cmd.CallbackCommand;
import javelin.bot.cmd.CommandType;
import javelin.bot.boss.msg.handler.CallbackMessageHandler;
import javelin.bot.boss.msg.handler.EntityCallbackMessageHandler;
import javelin.bot.boss.msg.handler.UtilMessageHandler;
import javelin.bot.client.EntityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BossCallbackMessageHandlerManager {

    private final Map<CommandType, Function<EntityType, CallbackMessageHandler>> functionContainer;
    private final Map<CommandType, UtilMessageHandler> utilHandlers;

    public BossCallbackMessageHandlerManager(
        List<EntityCallbackMessageHandler> entityMessageHandlers,
        List<UtilMessageHandler> utilMessageHandler
    ) {
        Map<CommandType, Function<EntityType, CallbackMessageHandler>> functionBuilder = new HashMap<>();
        entityMessageHandlers.stream()
            .collect(Collectors.groupingBy(
                EntityCallbackMessageHandler::commandType,
                Collectors.toMap(EntityCallbackMessageHandler::entityType, Function.identity())
            )).forEach((type, handler) -> functionBuilder.put(type, handler::get));
        this.functionContainer = functionBuilder;

        this.utilHandlers = utilMessageHandler.stream()
            .collect(Collectors.toMap(UtilMessageHandler::commandType, Function.identity()));
    }

    public BotApiMethod<?> manageCallback(Message message, String data) {
        var txt = new StringBuilder();
        CallbackMessageHandler.HandleResult handleResult = null;
        for (String cc : data.split(";")) {
            handleResult = handleCallback(message.getChatId(), cc);
            if (handleResult != null && handleResult.getText() != null) {
                txt.append("\n").append(handleResult.getText());
            }
        }
        if (handleResult == null) {
            return null;
        }
        var msg = SendMessage.builder()
            .chatId(String.valueOf(message.getChatId()))
            .text(txt.toString())
            .disableWebPagePreview(!handleResult.isPreviewPage())
            .replyMarkup(handleResult.getInline())
            .build();
        msg.enableHtml(true);
        return msg;
    }

    private CallbackMessageHandler.HandleResult handleCallback(long chatId, String data) {
        var cc = CallbackCommand.fromText(data);
        cc.setChatId(chatId);
        if (CommandType.UTIL.contains(cc.getCommandType())) {
            return utilHandlers.get(cc.getCommandType()).handle(cc);
        }
        return functionContainer.get(cc.getCommandType()).apply(cc.getEntityType()).handle(cc);
    }

}
