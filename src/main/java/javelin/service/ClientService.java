package javelin.service;

import javelin.client.OrderClient;
import javelin.entity.Client;
import javelin.model.ClientImportRequest;
import javelin.model.ClientRequest;
import javelin.model.CategoryView;
import javelin.model.ProductView;
import javelin.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository rep;
    private final OrderClient orderClient;

    public List<Client> getAll() {
        return rep.findAll();
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
        return rep.save(c);
    }

    @Transactional
    public void deleteById(Long id) {
        rep.findById(id).ifPresent(rep::delete);
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
