package javelin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ProductRequest(
    String title, @JsonProperty("external_id") String externalId, List<Long> categoryIds
) {
}