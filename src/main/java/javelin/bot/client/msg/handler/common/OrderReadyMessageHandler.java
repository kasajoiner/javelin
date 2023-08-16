package javelin.bot.client.msg.handler.common;

import javelin.bot.cmd.ChatCommand;
import javelin.bot.client.msg.handler.MessageHandler;
import javelin.entity.Employee;
import javelin.entity.Order;
import javelin.service.EmployeeService;
import javelin.service.OrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class OrderReadyMessageHandler implements MessageHandler {

    public static final String READY = "/ready";

    private final OrderManager orderManager;
    private final EmployeeService employeeService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        employeeService.findById(cc.getChatId())
            .filter(e -> e.getRole() == Employee.Role.ADMIN)
            .map(e -> {
                var orderId = Long.parseLong(cc.getKey());
                return orderManager.handleStatusChange(orderId, Order.Status.COOKED);
            });
        return null;
    }

    @Override
    public String trigger() {
        return READY;
    }
}
