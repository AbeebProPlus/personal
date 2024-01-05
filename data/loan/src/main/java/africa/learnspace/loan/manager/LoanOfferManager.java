package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.trainee.LoanOffer;
import africa.learnspace.loan.models.trainee.LoanRequest;
import africa.learnspace.loan.repository.LoanOfferRepository;
import africa.learnspace.loan.utils.LoanException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.Pageable;
import java.util.List;

import static africa.learnspace.loan.utils.ConstantUtils.INVALID_DETAILS;

@Component
public class LoanOfferManager {

    private final LoanOfferRepository loanOfferRepository;

    public LoanOfferManager(LoanOfferRepository loanOfferRepository) {
        this.loanOfferRepository = loanOfferRepository;
    }


    public LoanOffer creatLoanOffer(LoanOffer loanOffer){
        return loanOfferRepository.save(loanOffer);
    }


    public List<LoanOffer> findAll() {
        return loanOfferRepository.findAll();
    }
     public Page<LoanOffer> findAll(Pageable pageable) {
            return loanOfferRepository.findAll(pageable);
        }

    public void deleteAll() {
        loanOfferRepository.deleteAll();
    }


    public LoanOffer findById(String loanOfferId) {
        return loanOfferRepository.findById(loanOfferId).orElseThrow(() -> new LoanException(INVALID_DETAILS));

    }

    public LoanOffer findByLoanOfferByLoanRequest(LoanRequest loanRequest) {
        return  loanOfferRepository.findByLoanRequest(loanRequest);
    }
}
