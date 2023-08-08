package javelin.bot.client;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum EntityType {
    COMMUNICATION("c");
    private static final Map<String, EntityType> VALUES = Arrays.stream(values())
        .collect(Collectors.toMap(EntityType::getKey, Function.identity()));

    private final String key;

    EntityType(String key) {
        this.key = key;
    }

    public static EntityType of(String key) {
        return VALUES.get(key);
    }
}
