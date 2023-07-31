package javelin.dto;

import javelin.bot.boss.msg.BossMsgName;

public record BossMessageTemplateRequest(BossMsgName name, String txt) {
}
