package africa.learnspace.investor.data.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditInvestorRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
