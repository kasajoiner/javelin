package javelin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryView(Long id, String title, @JsonProperty("external_id") String externalId,
                           List<ProductView> products) {
}
