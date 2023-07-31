package javelin.dto;

import javelin.bot.client.msg.ClientMsgName;

public record ClientMessageTemplateRequest(ClientMsgName name, String txt) {
}
