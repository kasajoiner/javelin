package javelin.bot.client.msg.handler;


import javelin.bot.client.EntityType;

public interface EntityCallbackMessageHandler extends CallbackMessageHandler {

    EntityType entityType();
}
