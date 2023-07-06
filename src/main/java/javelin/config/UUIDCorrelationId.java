package javelin.config;

import org.zalando.logbook.CorrelationId;
import org.zalando.logbook.HttpRequest;

import java.util.UUID;

public class UUIDCorrelationId implements CorrelationId {
    @Override
    public String generate(HttpRequest request) {
        return UUID.randomUUID().toString();
    }
}