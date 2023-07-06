package javelin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosterProduct {
    private String barcode;
    @JsonProperty("category_name")
    private String categoryName;
//    private String unit;
//    private String cost;
//    @JsonProperty("cost_netto")
//    private String costNetto;
    @JsonProperty("menu_category_id")
    private String menuCategoryId;
//    private String workshop;
//    private String nodiscount;
//    private String photo;
//    @JsonProperty("photo_origin")
//    private String photoOrigin;
//    @JsonProperty("product_code")
//    private String productCode;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_name")
    private String productName;
//    @JsonProperty("sort_order")
//    private String sortOrder;
//    @JsonProperty("tax_id")z
//    private String taxId;
//    @JsonProperty("product_tax_id")
//    private String productTaxId;
//    private String type;
//    @JsonProperty("weight_flag")
//    private String weightFlag;
//    private String color;
//    @JsonProperty("ingredient_id")
//    private String ingredientId;
//    @JsonProperty("cooking_time")
//    private String cookingTime;
//    @JsonProperty("fiscal_code")
//    private String fiscalCode;
//    private List<Modification> modifications;
//    private int out;
}
