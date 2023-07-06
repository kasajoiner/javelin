package javelin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductView(Long id, String title, @JsonProperty("external_id") String externalId) {
}
