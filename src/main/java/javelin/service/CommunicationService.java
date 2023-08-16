package javelin.service;

import javelin.bot.template.MessageTemplateContext;
import javelin.dto.NewCommunicationRequest;
import javelin.entity.Client;
import javelin.entity.Communication;
import javelin.entity.Order;
import javelin.entity.Receiver;
import javelin.repo.CommunicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static javelin.entity.Communication.Status.ACCEPTED;
import static javelin.entity.Communication.Status.CANCELLED;
import static javelin.entity.Communication.Status.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunicationService {
    private static final String NEW_CLIENT = "employee/reg";

    private final CommunicationRepository repository;
    private final MessageQService qService;
    private final EmployeeMessageQService employeeMessageQService;
    private final MessageTemplateContext templateContext;

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

    public Communication pushNewClient(Client client) {

        var txt = templateContext.processTemplate(
            NEW_CLIENT,
            Map.of(
                "phone", client.getPhone(),
                "tag", client.getTag()
            )
        );
        var c = new Communication();
        c.setTxt(txt);
        c.setType(Communication.Type.TEXT);
        c.setSender(client.getId());
        c.setReceiver(Receiver.ADMIN);
        c.setStatus(CREATED);
        var newClient = repository.save(c);
        employeeMessageQService.pushCommunication(newClient);
        return newClient;
    }

    public Communication pushNewClientPhoto(Order order, String url) {

        var c = new Communication();
        c.setObjectUrl(url);
        c.setType(Communication.Type.PHOTO);
        c.setSender(order.getClientId());
        c.setReceiver(Receiver.CLIENT);
        c.setStatus(CREATED);
        var clientPhoto = repository.save(c);
        qService.pushCommunication(clientPhoto);
        return clientPhoto;
    }

    public Communication accept(Long id) {
        return repository.findById(id)
            .map(c -> {
                if (c.getStatus() == CREATED || c.getStatus() == CANCELLED) {
                    c.setStatus(ACCEPTED);
                    var accepted = repository.save(c);
                    qService.pushCommunication(accepted);
                    log.info("communication accepted {}", accepted);
                    return accepted;
                }
                return c;
            })
            .orElseThrow();
    }

    public Communication cancel(Long id) {
        return repository.findById(id)
            .map(c -> {
                if (c.getStatus() == CREATED) {
                    c.setStatus(CANCELLED);
                    var cancelled = repository.save(c);
                    log.info("communication cancelled {}", cancelled);
                    return cancelled;
                }
                return c;
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
