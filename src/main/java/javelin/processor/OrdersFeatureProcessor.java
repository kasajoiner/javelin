package javelin.processor;

import javelin.entity.Order;
import javelin.repo.FeaturesRepository;
import javelin.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdersFeatureProcessor {

    private final CommunicationService communicationService;
    private final FeaturesRepository featuresRepository;

    @Value("${orders.url1}")
    private String photoUrl1;
    @Value("${orders.url5}")
    private String photoUrl5;

    public void process(List<Order> orders) {
        var features = featuresRepository.findById(1L).orElseThrow();
        if (features.isOrderCounter()) {
            if (orders.size() == 1) {
                communicationService.pushNewClientPhoto(orders.get(0), photoUrl1);
            }
            if (orders.size() == 5) {
                communicationService.pushNewClientPhoto(orders.get(0), photoUrl5);
            }
        }
    }
}
