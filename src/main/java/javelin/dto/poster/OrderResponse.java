package javelin.dto.poster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderResponse(
    @JsonProperty("response") IncomingOrder incomingOrder
) {
}
