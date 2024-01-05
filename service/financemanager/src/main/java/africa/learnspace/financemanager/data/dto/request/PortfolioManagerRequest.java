package africa.learnspace.financemanager.data.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioManagerRequest {
    @NotEmpty(message = "institute name must not be empty")
    private String instituteName;
    @NotEmpty(message = "institute first name must not be empty")
    private String instituteAdminFirstName;
    @NotEmpty(message = "institute admin last name must not be empty")
    private String instituteAdminLastName;
    @NotEmpty(message = "institute admin email must not be empty")
    private String instituteAdminEmail;


}
