package javelin.processor;

import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Employee;
import javelin.entity.Order;
import javelin.service.EmployeeService;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeAcceptedOrderStatusProcessor implements OrderStatusProcessor {

    private final MessageTemplateContext templateContext;
    private final MessageQService qService;
    private final EmployeeService employeeService;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.ACCEPTED;
    }

    @Override
    public String process(Client c, Order o) {
        if (o.getService() == Order.Service.DINEIN) {
            var txt = templateContext.processTemplate(
                TemplateNames.ORDER_ADMIN_ACCEPTED,
                Map.of(
                    "id", o.getId()
                )
            );
            employeeService
                .findByRole(Employee.Role.ADMIN)
                .forEach(employee -> qService.push(employee.getId(), txt));
            return txt;
        }
        return Order.Status.ACCEPTED.toString();
    }
}
