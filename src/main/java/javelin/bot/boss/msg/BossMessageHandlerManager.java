package javelin.bot.boss.msg;

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
    private final EmployeeService employeeService;

    public BossMessageHandlerManager(
        @Autowired List<AdminMessageHandler> commonMessageHandlers,
        @Autowired IDefaultMessageHandler defaultMessageHandler,
        @Autowired EmployeeService employeeService
    ) {
        this.commonMessageHandlers = commonMessageHandlers.stream()
            .collect(Collectors.toMap(AdminMessageHandler::trigger, Function.identity()));
        this.defaultMessageHandler = defaultMessageHandler;
        this.employeeService = employeeService;
    }

    public BotApiMethod<?> manage(Message message) {
        var cc = ChatCommandParser.parse(
            message.getText(),
            message.getChatId(),
            message.getFrom()
        );
        return employeeService.findBossById(message.getChatId())
            .map(e -> commonMessageHandlers.get(cc.getTrigger()))
            .orElse(defaultMessageHandler)
            .handle(cc);
    }
}
