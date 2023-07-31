package javelin.controller;

import javelin.bot.boss.msg.BossMessageService;
import javelin.bot.client.ClientMessageService;
import javelin.dto.BossMessageTemplateRequest;
import javelin.dto.ClientMessageTemplateRequest;
import javelin.entity.MessageTemplate;
import javelin.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/msgs")
@RequiredArgsConstructor
public class MessageTemplateController {

    private final ClientMessageService clientMessageService;
    private final BossMessageService bossMessageService;
    private final MessageTemplateService messageTemplateService;

    @GetMapping
    public List<MessageTemplate> findAll() {
        return messageTemplateService.findAll();
    }

    @PostMapping("/client")
    public MessageTemplate save(@RequestBody ClientMessageTemplateRequest msg) {
        return clientMessageService.save(msg);
    }

    @PostMapping("/boss")
    public MessageTemplate save(@RequestBody BossMessageTemplateRequest msg) {
        return bossMessageService.save(msg);
    }
}
