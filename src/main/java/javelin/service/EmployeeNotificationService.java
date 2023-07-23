package javelin.service;

import javelin.bot.BotRouter;
import javelin.bot.msg.template.MessageTemplateContext;
import javelin.bot.msg.template.TemplateNames;
import javelin.entity.Employee;
import javelin.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmployeeNotificationService {

    private final EmployeeService employeeService;
    private final BotRouter bot;
    private final MessageTemplateContext templateContext;

    public void notify(Order o) {
        if (o.getService() == Order.Service.DINEIN){
            switch (o.getStatus()) {
                case ACCEPTED -> notifyAccepted(o);
            }
        }
    }

    private void notifyAccepted(Order o) {
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ADMIN_COOKED,
            Map.of(
                "id", o.getId()
            )
        );
        employeeService.findByRole(Employee.Role.ADMIN)
                .forEach(employee -> {
                    bot.sendNew(employee.getId(), txt);
                });
    }
}
