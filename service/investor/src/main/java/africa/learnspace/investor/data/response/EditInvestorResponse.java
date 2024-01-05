package africa.learnspace.investor.data.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EditInvestorResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
