package africa.learnspace.investor.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class FundProductRequest {

    @NotNull
    private String name;
    @NotNull
    private double interestRate;
    @NotNull
    private Long tenor;
    @NotNull
    private String objective;

    @NotNull
    private BigDecimal size;
    @NotNull
    private BigDecimal amountInvested;
    @NotNull
    private BigDecimal collected;

    @NotNull
    private String riskProfile;

}
