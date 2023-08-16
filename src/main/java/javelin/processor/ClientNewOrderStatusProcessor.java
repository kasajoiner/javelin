package javelin.processor;

import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientNewOrderStatusProcessor implements OrderStatusProcessor {

    private final MessageTemplateContext templateContext;
    private final MessageQService qService;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.NEW;
    }

    @Override
    public String process(Client c, Order o) {
        log.info("order:new c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_NEW
        );
        qService.push(c.getId(), txt);
        return txt;
    }
}
