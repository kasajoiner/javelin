package javelin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PosterCategory {

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

//    @JsonProperty("category_photo")
//    private String categoryPhoto;
//
//    @JsonProperty("parent_category")
//    private String parentCategory;
//
//    @JsonProperty("category_color")
//    private String categoryColor;
//
//    @JsonProperty("category_hidden")
//    private String categoryHidden;
//
//    @JsonProperty("sort_order")
//    private String sortOrder;
//
//    @JsonProperty("fiscal")
//    private String fiscal;
//
//    @JsonProperty("nodiscount")
//    private String nodiscount;
//
//    @JsonProperty("tax_id")
//    private String taxId;
//
//    @JsonProperty("left")
//    private String left;
//
//    @JsonProperty("right")
//    private String right;
//
//    @JsonProperty("level")
//    private String level;
//
//    @JsonProperty("category_tag")
//    private String categoryTag;
}

