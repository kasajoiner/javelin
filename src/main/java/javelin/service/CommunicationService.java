package javelin.service;

import javelin.dto.NewCommunicationRequest;
import javelin.entity.Communication;
import javelin.repo.CommunicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static javelin.entity.Communication.Status.ACCEPTED;
import static javelin.entity.Communication.Status.CANCELLED;
import static javelin.entity.Communication.Status.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunicationService {
    private final CommunicationRepository repository;
    private final MessageQService qService;

    public Communication createNew(NewCommunicationRequest r) {
        log.info("new communication {}", r);
        var c = new Communication();
        c.setObjectId(r.objectId());
        c.setTxt(r.caption());
        c.setType(r.type());
        c.setSender(r.sender());
        c.setReceiver(r.receiver());
        c.setStatus(CREATED);
        return repository.save(c);
    }

    public Communication accept(Long id) {
        return repository.findById(id)
            .filter(c -> c.getStatus() == CREATED || c.getStatus() == Communication.Status.CANCELLED )
            .map(c -> {
                c.setStatus(ACCEPTED);
                var accepted = repository.save(c);
                qService.pushCommunication(accepted);
                log.info("communication accepted {}", accepted);
                return accepted;
            })
            .orElseThrow();
    }

    public Communication cancel(Long id) {
        return repository.findById(id)
            .filter(c -> c.getStatus() == CREATED)
            .map(c -> {
                c.setStatus(CANCELLED);
                var cancelled = repository.save(c);
                log.info("communication cancelled {}", cancelled);
                return cancelled;
            })
            .orElseThrow();
    }

    public Communication updateUrl(String objectId, String url) {
        return repository.findByObjectId(objectId)
            .map(c -> {
                c.setObjectUrl(url);
                return repository.save(c);
            }).orElseThrow();
    }
}
