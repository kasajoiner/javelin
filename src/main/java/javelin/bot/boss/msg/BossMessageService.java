package javelin.bot.boss.msg;

import javelin.dto.BossMessageTemplateRequest;
import javelin.entity.MessageTemplate;
import javelin.service.MessageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BossMessageService {

    private final MessageTemplateService messageService;

    public MessageTemplate findByName(BossMsgName name) {
        return messageService.findById(name.name())
            .orElseThrow();
    }

    public MessageTemplate save(BossMessageTemplateRequest r) {
        var msg = new MessageTemplate();
        msg.setId(r.name().name());
        msg.setTxt(r.txt());
        return messageService.save(msg);
    }
}
