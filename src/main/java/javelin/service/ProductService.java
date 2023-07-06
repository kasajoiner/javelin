package javelin.service;

import javelin.client.MenuClient;
import javelin.entity.Product;
import javelin.model.ImportRequest;
import javelin.model.ProductRequest;
import javelin.model.ProductView;
import javelin.repo.CategoryRepository;
import javelin.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository rep;
    private final CategoryRepository categoryRepository;
    private final MenuClient menuClient;

    public List<ProductView> getAllProducts() {
        return rep.findAll().stream()
            .map(ProductService::productView)
            .toList();
    }

    public Optional<ProductView> getProductById(Long id) {
        return rep.findById(id)
            .map(ProductService::productView);
    }

    @Transactional
    public ProductView saveProduct(ProductRequest r) {
        var product = new Product();
        product.setTitle(r.title());
        product.setExternalId(r.externalId());
        if (r.categoryIds() != null) {
            product.setCategories(categoryRepository.findAllById(r.categoryIds()));
        }
        Product savedProduct = rep.save(product);
        return productView(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        rep.findById(id).ifPresent(rep::delete);
    }

    @Transactional
    public List<ProductView> importProducts(ImportRequest r) {
        var imported = menuClient.getProducts()
            .stream()
            .filter(pp -> r.ids().contains(Integer.parseInt(pp.getProductId())))
            .map(pp -> {
                var p = new Product();
                p.setTitle(pp.getProductName());
                p.setExternalId(pp.getProductId());
                if (pp.getMenuCategoryId() != null) {
                    categoryRepository.findByExternalId(pp.getMenuCategoryId())
                        .ifPresent(c -> p.setCategories(List.of(c)));
                }
                return p;
            })
            .toList();
        List<Product> savedProducts = rep.saveAll(imported);
        return savedProducts.stream()
            .map(ProductService::productView)
            .toList();
    }

    @NotNull
    private static ProductView productView(Product p) {
        return new ProductView(p.getId(), p.getTitle(), p.getExternalId());
    }
}

