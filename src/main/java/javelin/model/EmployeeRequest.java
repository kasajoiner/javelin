package javelin.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record EmployeeRequest(
    @NotNull @Min(0) Long id,
    @NotBlank String role,
    String status
) {
}
