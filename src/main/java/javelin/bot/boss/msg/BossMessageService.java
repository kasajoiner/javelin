package javelin.bot.boss.msg;

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
}
