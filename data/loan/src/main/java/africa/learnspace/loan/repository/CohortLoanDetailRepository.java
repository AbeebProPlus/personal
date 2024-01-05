package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.cohort.CohortLoanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CohortLoanDetailRepository extends JpaRepository<CohortLoanDetail, String> {
}
