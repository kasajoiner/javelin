package javelin.service;

import javelin.client.MenuClient;
import javelin.entity.Category;
import javelin.entity.Product;
import javelin.model.CategoryRequest;
import javelin.model.CategoryView;
import javelin.model.ImportRequest;
import javelin.model.ProductView;
import javelin.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository rep;
    private final MenuClient menuClient;

    public List<CategoryView> getAllCategories() {
        return rep.findAll().stream()
            .map(CategoryService::categoryView)
            .collect(Collectors.toList());
    }

    public Optional<CategoryView> getCategoryById(Long id) {
        return rep.findById(id)
            .map(CategoryService::categoryView);
    }

    @Transactional
    public CategoryView saveCategory(CategoryRequest r) {
        var category = new Category();
        category.setTitle(r.title());
        category.setExternalId(r.externalId());
        Category savedCategory = rep.save(category);
        return categoryView(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        rep.findById(id).ifPresent(rep::delete);
    }

    @Transactional
    public List<CategoryView> importCategories(ImportRequest r) {
        var imported = menuClient.getCategories()
            .stream()
            .filter(pc -> r.ids().contains(Integer.parseInt(pc.getCategoryId())))
            .map(pc -> {
                var c = new Category();
                c.setTitle(pc.getCategoryName());
                c.setExternalId(pc.getCategoryId());
                return c;
            })
            .toList();
        List<Category> savedCategories = rep.saveAll(imported);
        return savedCategories.stream()
            .map(CategoryService::categoryView)
            .toList();
    }

    @NotNull
    private static CategoryView categoryView(Category c) {
        return new CategoryView(
            c.getId(),
            c.getTitle(),
            c.getExternalId(),
            c.getProducts().stream()
                .map(CategoryService::productView)
                .toList()
        );
    }

    @NotNull
    private static ProductView productView(Product p) {
        return new ProductView(
            p.getId(),
            p.getTitle(),
            p.getExternalId()
        );
    }
}
