package javelin.processor;

import javelin.bot.client.ClientMessageService;
import javelin.bot.client.msg.ClientMsgName;
import javelin.bot.template.MessageTemplateContext;
import javelin.bot.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.repo.OrderRepository;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientAcceptedOrderStatusProcessor implements OrderStatusProcessor {

    private final MessageTemplateContext templateContext;
    private final MessageQService qService;
    private final OrderRepository orderRepository;
    private final OrdersFeatureProcessor ordersFeature;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.ACCEPTED;
    }

    @Override
    public String process(Client c, Order o) {
        log.info("order:accepted c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ACCEPT
        );
        qService.push(c.getId(), txt);

        var clientOrders = orderRepository.findAllByClientId(c.getId());
        ordersFeature.process(clientOrders);
        return txt;
    }
}
