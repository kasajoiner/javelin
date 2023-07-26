package javelin.bot.client.msg.template;

import java.util.Map;

public class TemplateUtils {

    private TemplateUtils() {}

    public static Map<String, Object> name(String name) {
        return params("name", name);
    }

    public static Map<String, Object> params(String key, Object value) {
        return Map.of(key, value);
    }

    public static Map<String, Object> params(String key1, Object value1, String key2, Object value2) {
        return Map.of(key1, value1, key2, value2);
    }
}
