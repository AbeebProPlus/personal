package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.institute.InstituteLoanDetail;
import africa.learnspace.loan.repository.InstituteLoanDetailRepository;
import org.springframework.stereotype.Component;

@Component
public class InstituteLoanDetailManager {

    private final InstituteLoanDetailRepository instituteLoanDetailRepository;

    public InstituteLoanDetailManager(InstituteLoanDetailRepository instituteLoanDetailRepository) {
        this.instituteLoanDetailRepository = instituteLoanDetailRepository;
    }

    public InstituteLoanDetail createInstituteLoanDetail(InstituteLoanDetail instituteLoanDetail) {
        return instituteLoanDetailRepository.save(instituteLoanDetail);
    }
}
