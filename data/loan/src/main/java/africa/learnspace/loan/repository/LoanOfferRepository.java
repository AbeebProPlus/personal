package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.trainee.LoanOffer;
import africa.learnspace.loan.models.trainee.LoanRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoanOfferRepository extends JpaRepository<LoanOffer, String> {

//    LoanOffer findByTraineeId(String traineeId);

//    LoanOffer findByLoanRequestId(String loanRequestId);

    LoanOffer findByLoanRequest(LoanRequest loanRequest);
}
