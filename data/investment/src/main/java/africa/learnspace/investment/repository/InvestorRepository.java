package africa.learnspace.investment.repository;

import africa.learnspace.investment.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvestorRepository extends JpaRepository<Investor, String> {
    @Query("SELECT e.user.email FROM Investor e")
    String findInvestorByEmail();


}
