package javelin.controller;

import javelin.entity.MessageTemplate;
import javelin.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/msgs")
@RequiredArgsConstructor
public class MessageTemplateController {

    private final MessageTemplateService messageTemplateService;

    @GetMapping
    public List<MessageTemplate> findAll() {
        return messageTemplateService.findAll();
    }

    @PostMapping
    public MessageTemplate save(MessageTemplate msg) {
        return messageTemplateService.save(msg);
    }
}
