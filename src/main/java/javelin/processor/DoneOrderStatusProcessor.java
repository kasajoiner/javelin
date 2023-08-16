package javelin.processor;

import javelin.bot.client.ClientMessageService;
import javelin.bot.client.msg.ClientMsgName;
import javelin.entity.Client;
import javelin.entity.Order;
import javelin.repo.OrderRepository;
import javelin.service.MessageQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoneOrderStatusProcessor implements OrderStatusProcessor {

    private final OrderRepository orderRepository;
    private final OrdersFeatureProcessor ordersFeature;
    private final ClientMessageService messageService;
    private final MessageQService qService;

    @Override
    public boolean isApplicable(Order o) {
        return o.getStatus() == Order.Status.DONE;
    }

    @Override
    public String process(Client c, Order o) {
        var clientOrders = orderRepository.findAllByClientId(c.getId());
        ordersFeature.process(clientOrders);
        messageService.findByName(ClientMsgName.ORDER_DONE)
            .ifPresent(msg -> qService.push(c.getId(), msg.getTxt()));
        return Order.Status.DONE.toString();
    }
}
