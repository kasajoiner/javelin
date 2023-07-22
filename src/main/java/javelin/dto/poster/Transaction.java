package javelin.dto.poster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Transaction(
    @JsonProperty("transaction_id") Long id,
    @JsonProperty("processing_status") Integer status
) {

//    public record HistoryEntry(
//        @JsonProperty("history_id") Long id,
//        @JsonProperty("type_history") String type,
//        String value
//    ) {}
}
