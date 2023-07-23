package javelin.service;

import javelin.bot.BotRouter;
import javelin.bot.msg.template.MessageTemplateContext;
import javelin.bot.msg.template.TemplateNames;
import javelin.entity.Client;
import javelin.entity.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ClientNotificationService {

    private final BotRouter bot;
    private final MessageTemplateContext templateContext;
    private final EmployeeNotificationService employeeNotificationService;

    public void notify(Client c, Order o) {
        switch (o.getStatus()) {
            case NEW -> notifyNew(c, o);
            case ACCEPTED -> notifyAccepted(c, o);
            case COOKED -> notifyCooked(c, o);
            case DELIVERING -> notifyDelivering(c, o);
        }
    }

    public void notifyNew(Client c, Order o) {
        log.info("order:new c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_NEW,
            Map.of(
                "id", o.getId()
            )
        );
        bot.sendNew(c.getId(), txt);
    }

    public void notifyAccepted(Client c, Order o) {
        log.info("order:accepted c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_ACCEPT,
            Map.of(
                "id", o.getId()
            )
        );
        bot.sendNew(c.getId(), txt);
        employeeNotificationService.notify(o);
    }

    public void notifyCooked(Client c, Order o) {
        log.info("order:cooked c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_COOKED,
            Map.of(
                "id", o.getId()
            )
        );
        bot.sendNew(c.getId(), txt);
    }

    public void notifyDelivering(Client c, Order o) {
        log.info("order:delivery c:{} o:{}", c.getId(), o.getId());
        var txt = templateContext.processTemplate(
            TemplateNames.ORDER_DELIVERING,
            Map.of(
                "id", o.getId(),
                "addr", o.getAddress()
            )
        );
        bot.sendNew(c.getId(), txt);
    }

}
