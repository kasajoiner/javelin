package javelin.dto.poster;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebhookRequest(
    String account,
    @JsonProperty("account_number") String accountNumber,
    String object,
    @JsonProperty("object_id") long objectId,
    String action,
    String time,
    String verify) {}

