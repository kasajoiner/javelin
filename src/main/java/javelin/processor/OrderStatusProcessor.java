package javelin.processor;

import javelin.entity.Client;
import javelin.entity.Order;

public interface OrderStatusProcessor {

    boolean isApplicable(Order o);

    String process(Client c, Order o);
}
