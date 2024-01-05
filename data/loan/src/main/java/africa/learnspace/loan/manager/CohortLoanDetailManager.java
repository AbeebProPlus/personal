package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import africa.learnspace.loan.repository.CohortLoanDetailRepository;
import org.springframework.stereotype.Component;

@Component
public class CohortLoanDetailManager {

    private final CohortLoanDetailRepository cohortLoanDetailRepository;


    public CohortLoanDetailManager(CohortLoanDetailRepository cohortLoanDetailRepository) {
        this.cohortLoanDetailRepository = cohortLoanDetailRepository;
    }

    public CohortLoanDetail createCohortLoanDetail(CohortLoanDetail cohortLoanDetail) {
        return cohortLoanDetailRepository.save(cohortLoanDetail);
    }

    public CohortLoanDetail save (CohortLoanDetail cohortLoanDetail) {
        return cohortLoanDetailRepository.save(cohortLoanDetail);
    }
}
