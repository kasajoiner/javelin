package javelin.client.poster;

import javelin.dto.PosterProduct;
import lombok.Data;

import java.util.List;

@Data
public class ProductsResponse {
    private List<PosterProduct> response;
}
