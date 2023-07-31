package javelin.bot.client;

import javelin.bot.client.msg.ClientMsgName;
import javelin.dto.ClientMessageTemplateRequest;
import javelin.entity.MessageTemplate;
import javelin.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientMessageService {

    private final MessageTemplateService messageService;

    public Optional<MessageTemplate> findByName(ClientMsgName name) {
        return messageService.findById(name.name());
    }

    public MessageTemplate save(ClientMessageTemplateRequest r) {
        var msg = new MessageTemplate();
        msg.setId(r.name().name());
        msg.setTxt(r.txt());
        return messageService.save(msg);
    }
}
