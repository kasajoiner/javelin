package javelin.service;

import javelin.bot.client.msg.template.MessageTemplateContext;
import javelin.bot.client.msg.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClientNotificationService {

    private final MessageQService qService;
    private final MessageTemplateContext templateContext;
    private final EmployeeNotificationService employeeNotificationService;

    public String notify(Client c, Order o) {
        return switch (o.getStatus()) {
            case NEW -> notifyNew(c, o);
            case ACCEPTED -> notifyAccepted(c, o);
            case COOKING -> notifyCooking(c, o);
            case COOKED -> notifyCooked(c, o);
            case DELIVERING -> notifyDelivering(c, o);
            default -> null;
        };
    }

    public String notifyNew(Client c, Order o) {
        log.info("order:new c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_NEW
        );
        qService.push(c.getId(), txt);
        return txt;
    }

    public String notifyAccepted(Client c, Order o) {
        log.info("order:accepted c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ACCEPT
        );
        qService.push(c.getId(), txt);
        employeeNotificationService.notify(o);
        return txt;
    }

    public String notifyCooking(Client c, Order o) {
        log.info("order:Cooking c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_COOKING
        );
        qService.push(c.getId(), txt);
        employeeNotificationService.notify(o);
        return txt;
    }

    public String notifyCooked(Client c, Order o) {
        log.info("order:cooked c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_COOKED
        );
        qService.push(c.getId(), txt);
        return txt;
    }

    public String notifyDelivering(Client c, Order o) {
        log.info("order:delivery c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_DELIVERING
        );
        qService.push(c.getId(), txt);
        return txt;
    }

}
