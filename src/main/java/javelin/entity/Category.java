package javelin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@Entity(name = "category")
public class Category extends IdEntity {
    private String title;
    @JsonProperty("external_id")
    private String externalId;

    @ManyToMany
    @JoinTable(
        name = "category_product",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;
}
