package javelin.controller;

import javelin.dto.poster.WebhookRequest;
import javelin.dto.poster.WebhookResponse;
import javelin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<WebhookResponse> orderWebhook(@RequestBody WebhookRequest r) {
        switch (r.object()) {
            case "incoming_order":
                orderService.handleEvent(r);
            default:
        }
        return ResponseEntity.ok().build();
    }
}
