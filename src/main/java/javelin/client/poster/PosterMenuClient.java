package javelin.client.poster;

import javelin.client.MenuClient;
import javelin.dto.PosterCategory;
import javelin.dto.PosterProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PosterMenuClient implements MenuClient {

    private final RestTemplate restTemplate;

    @Value("${category.url}")
    private String categoryUrl;
    @Value("${products.url}")
    private String productsUrl;
    @Value("${menu.token}")
    private String token;

    @Override
    public List<PosterCategory> getCategories() {
        var url = tokenize(categoryUrl);
        var r = restTemplate.getForEntity(url.toUriString(), CategoryResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody().getResponse();
        }
        return List.of();
    }

    @Override
    public List<PosterProduct> getProducts() {
        var url = tokenize(productsUrl)
            .queryParam("type", "batchtickets");
        var r = restTemplate.getForEntity(url.toUriString(), ProductsResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody().getResponse();
        }
        return List.of();
    }

    @Override
    public List<PosterProduct> getProducts(String categoryId) {
        var url = tokenize(productsUrl)
            .queryParam("category_id", categoryId)
            .queryParam("type", "batchtickets");

        var r = restTemplate.getForEntity(url.toUriString(), ProductsResponse.class);
        if (r.getStatusCode().is2xxSuccessful() && r.getBody() != null) {
            return r.getBody().getResponse();
        }
        return List.of();
    }

    private UriComponentsBuilder tokenize(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).queryParam("token", token);
    }
}
