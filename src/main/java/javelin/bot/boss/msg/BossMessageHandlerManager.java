package javelin.bot.boss.msg;

import javelin.bot.boss.BossCommandTranslator;
import javelin.bot.cmd.ChatCommandParser;
import javelin.bot.boss.msg.handler.IDefaultMessageHandler;
import javelin.bot.boss.msg.handler.AdminMessageHandler;
import javelin.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BossMessageHandlerManager {

    private final Map<String, AdminMessageHandler> commonMessageHandlers;
    private final IDefaultMessageHandler defaultMessageHandler;
    private final BossCommandTranslator txt2Cmd;
    private final EmployeeService employeeService;

    public BossMessageHandlerManager(
        @Autowired List<AdminMessageHandler> commonMessageHandlers,
        @Autowired IDefaultMessageHandler defaultMessageHandler,
        @Autowired BossCommandTranslator txt2Cmd,
        @Autowired EmployeeService employeeService
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(Collectors.toMap(AdminMessageHandler::trigger, Function.identity()));
        this.defaultMessageHandler = defaultMessageHandler;
        this.txt2Cmd = txt2Cmd;
        this.employeeService = employeeService;
    }

    public BotApiMethod<?> manage(Message message) {
        var cc = ChatCommandParser.parse(
            message.getText(),
            message.getChatId(),
            message.getFrom()
        );
        return employeeService.findBossById(message.getChatId())
            .map(e -> getCommonHandler(cc.getTrigger()))
            .orElse(defaultMessageHandler)
            .handle(cc);
    }

    private AdminMessageHandler getCommonHandler(String text) {
        var commonMessageHandler = commonMessageHandlers.get(text);
        if (commonMessageHandler != null) {
            return commonMessageHandler;
        }
        var cmd = txt2Cmd.translate(text);
        return cmd != null ? commonMessageHandlers.get(cmd) : defaultMessageHandler;
    }

}
