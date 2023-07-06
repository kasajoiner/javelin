package javelin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PosterSpot {
    @JsonProperty("spot_id")
    private String spotId;
    private String price;
    private String profit;
    @JsonProperty("profit_netto")
    private String profitNetto;
    private String visible;
}
