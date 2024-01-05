package africa.learnspace.investor.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@RequiredArgsConstructor
public class AddInvestorRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private BigDecimal amountInvested;
}
