package javelin.bot.client.cmd;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum CommandType {
    LIST("l"),
    PICK("p"),
    REMOVE("r"),
    STATUS_USER("sr"),
    SETTINGS("st");

    public static final Set<CommandType> UTIL = Set.of(
        STATUS_USER,
        SETTINGS
    );
    private static final Map<String, CommandType> VALUES = Arrays.stream(values())
        .collect(Collectors.toMap(CommandType::getKey, Function.identity()));
    private final String key;

    CommandType(String key) {
        this.key = key;
    }

    public static CommandType of(String key) {
        return VALUES.get(key);
    }

    @Override
    public String toString() {
        return key;
    }
}
