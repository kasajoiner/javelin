package javelin.dto.poster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StatusResponse(
    @JsonProperty("response") List<Transaction> transaction
) {
}
