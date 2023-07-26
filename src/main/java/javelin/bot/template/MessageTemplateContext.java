package javelin.bot.template;

import com.vdurmont.emoji.EmojiParser;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javelin.bot.LangUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
public class MessageTemplateContext {

    public static final String ERROR = "error";

    private final Configuration configuration;

    public MessageTemplateContext(
        @Qualifier("localTemplateConfig") Configuration configuration
    ) {
        this.configuration = configuration;
    }

    public String processTemplate(String dir) {
        return processTemplate(dir, Collections.emptyMap());
    }

    public String processEmojiTemplate(String dir) {
        return EmojiParser.parseToUnicode(processTemplate(dir, Collections.emptyMap()));
    }

    public String processTemplate(String dir, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = configuration.getTemplate(dir + "/" + LangUtils.UKRAINIAN + ".ftl");
            template.process(params, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            log.error("Template processing failed {}", e.getMessage());
            return processTemplate(ERROR, Collections.emptyMap());
        }
    }

    public String errorTemplate(Map<String, Object> params) {
        return processTemplate(ERROR, params);
    }

    public String errorTemplate() {
        return processTemplate(ERROR, Collections.emptyMap());
    }

}
