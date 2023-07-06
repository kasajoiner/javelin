package javelin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryRequest(String title, @JsonProperty("external_id") String externalId) {
}
