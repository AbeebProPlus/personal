package africa.learnspace.investment.repository;

import africa.learnspace.investment.model.FundProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FundProductRepository extends JpaRepository<FundProduct, String> {
    FundProduct findByFundProductId(String fundProductId);
    Optional<FundProduct> findByName(String name);
}
