package javelin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderResponse(
    @JsonProperty("response") IncomingOrder incomingOrder
) {
}
