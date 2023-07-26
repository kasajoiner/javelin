package javelin.service;

import javelin.config.CachingConfig;
import javelin.entity.MessageTemplate;
import javelin.repo.MessageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageTemplateService {

    private final MessageTemplateRepository repository;

    public MessageTemplate save(MessageTemplate msg) {
        return repository.save(msg);
    }

    public List<MessageTemplate> findAll() {
        return repository.findAll();
    }

    @Cacheable(cacheNames = CachingConfig.MSG, unless = "#result == null")
    public Optional<MessageTemplate> findById(String id) {
        return repository.findById(id);
    }
}
