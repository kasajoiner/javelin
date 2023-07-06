package javelin.client.poster;

import javelin.dto.PosterCategory;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    private List<PosterCategory> response;
}
