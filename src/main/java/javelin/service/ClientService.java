package javelin.service;

import javelin.entity.Client;
import javelin.entity.Communication;
import javelin.entity.Receiver;
import javelin.model.ClientRequest;
import javelin.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository rep;
    private final CommunicationService communicationService;

    public List<Client> getAll() {
        return rep.findAll();
    }

    public List<Client> findAllByCommunication(Communication c) {
        return switch (c.getReceiver()) {
            case ALL -> rep.findAllByStatus(Client.Status.ENABLED);
            case CLIENT -> rep.findAllById(c.getSender());
            default -> rep.findAllByStatus(Client.Status.ENABLED);
        };
    }

    public Optional<Client> findById(Long id) {
        return rep.findById(id);
    }

    @Transactional
    public Client save(ClientRequest r) {
        var c = rep.findById(r.id())
            .orElseGet(() -> {
                var client = new Client();
                client.setId(r.id());
                return client;
            });
        c.setPhone(r.phone());
        c.setTag(r.tag());
        var saved = rep.save(c);
        communicationService.pushNewClient(saved);
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        rep.findById(id).ifPresent(rep::delete);
    }

    public Optional<Client> findByPhone(String phone) {
        return rep.findByPhone(phone);
    }

//    @Transactional
//    public List<Client> importClients(ClientImportRequest r) {
//        var start = r.from().atStartOfDay();
//        var to = r.to().atStartOfDay();
//        var imported = orderClient.findOrders(start, to)
//            .stream()
//            .map(io -> {
//                var c = new Client();
//                c.setPhone(io.phone());
//                c.setFirst(io.firstName());
//                c.setLast(io.lastName());
//            })
//            .toList();
//        List<Category> savedCategories = rep.saveAll(imported);
//        return savedCategories.stream()
//            .map(ClientService::categoryView)
//            .toList();
//    }
}
