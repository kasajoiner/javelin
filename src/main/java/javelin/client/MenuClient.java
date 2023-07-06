package javelin.client;

import javelin.dto.PosterCategory;
import javelin.dto.PosterProduct;

import java.util.List;

public interface MenuClient {

    List<PosterCategory> getCategories();

    List<PosterProduct> getProducts();

    List<PosterProduct> getProducts(String categoryId);
}
