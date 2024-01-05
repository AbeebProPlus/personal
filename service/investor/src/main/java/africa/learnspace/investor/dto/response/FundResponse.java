package africa.learnspace.investor.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FundResponse {

    private String fundProductId;
    private String name;
    private double interestRate;
    private String tenor;
}
