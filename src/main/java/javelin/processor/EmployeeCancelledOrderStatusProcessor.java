package javelin.processor;

import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.service.EmployeeMessageQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeCancelledOrderStatusProcessor implements OrderStatusProcessor {

    private final MessageTemplateContext templateContext;
    private final EmployeeMessageQService qService;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.CANCELLED;
    }

    @Override
    public String process(Client c, Order o) {
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ADMIN_CANCELLED,
            Map.of("id", o.getId())
        );
        qService.push(c.getId(), txt);
        return txt;
    }
}
