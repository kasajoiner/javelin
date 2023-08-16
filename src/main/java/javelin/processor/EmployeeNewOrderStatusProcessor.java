package javelin.processor;

import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.repo.OrderRepository;
import javelin.service.EmployeeMessageQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeNewOrderStatusProcessor implements OrderStatusProcessor {

    private final MessageTemplateContext templateContext;
    private final EmployeeMessageQService qService;
    private final OrderRepository orderRepository;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.NEW;
    }

    @Override
    public String process(Client c, Order o) {
        var clientOrders = orderRepository.findAllByClientId(c.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ADMIN_NEW,
            Map.of(
                "id", o.getId(),
                "tag", c.getTag(),
                "phone", c.getPhone(),
                "orderSize", clientOrders.size()
            )
        );
        qService.push(c.getId(), txt);
        return txt;
    }
}
