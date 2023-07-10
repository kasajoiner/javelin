package javelin.bot.msg.handler;


import javelin.bot.EntityType;

public interface EntityCallbackMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
