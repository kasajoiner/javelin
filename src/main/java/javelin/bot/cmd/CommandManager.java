package javelin.bot.cmd;

import org.springframework.stereotype.Component;
import javelin.bot.client.EntityType;

import static javelin.bot.cmd.CommandType.LIST;

@Component
public class CommandManager {



    public String list(EntityType entity) {
        return list(entity, 1);
    }

    public String list(EntityType entity, int page) {
        return toCmd(CallbackCommand.of(LIST, entity, page));
    }

    public String generic(CommandType c, EntityType e, int page, Object id) {
        return toCmd(CallbackCommand.of(c, e, page, id));
    }

    public String util(CommandType c) {
        return toCmd(CallbackCommand.of(c));
    }

    public String status() {
        return toCmd(CallbackCommand.of(CommandType.STATUS_USER));
    }


    private String toCmd(CallbackCommand cc) {
        return cc.toString();
    }

}
