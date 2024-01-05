package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.trainee.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanProductRepository extends JpaRepository<LoanProduct, String> {
}
