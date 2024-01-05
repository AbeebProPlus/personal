package africa.learnspace.financemanager.data.dto.response;

import africa.learnspace.loan.models.institute.InstituteAddress;
import africa.learnspace.loan.models.institute.InstituteLoanDetail;
import africa.learnspace.loan.models.institute.InstituteStatus;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstituteResponse {
    private String instituteId;

    private String instituteName;

    private String instituteAdminFirstName;

    private String instituteAdminLastName;

    private String instituteAdminEmail;

    private List<InstituteAddress> instituteAddress;

    private InstituteStatus status;

    private InstituteLoanDetail instituteLoanDetails;
}