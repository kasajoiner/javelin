package javelin.dto.poster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrdersResponse(List<IncomingOrder> response) {
}
