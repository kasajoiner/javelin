package javelin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PosterModification {
    @JsonProperty("modificator_id")
    private String modificatorId;
    @JsonProperty("modificator_name")
    private String modificatorName;
    @JsonProperty("modificator_selfprice")
    private String modificatorSelfprice;
    private String order;
    @JsonProperty("modificator_barcode")
    private String modificatorBarcode;
    @JsonProperty("modificator_product_code")
    private String modificatorProductCode;
    private List<PosterSpot> spots;
    private List<PosterSource> sources;
    @JsonProperty("ingredient_id")
    private String ingredientId;
    @JsonProperty("fiscal_code")
    private String fiscalCode;
}
