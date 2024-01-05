package africa.learnspace.loan.manager;

import africa.learnspace.loan.repository.LoanProductRepository;
import org.springframework.stereotype.Component;

@Component
public class LoanProductManager {
    private final LoanProductRepository loanProductRepository;

    public LoanProductManager(LoanProductRepository loanProductRepository) {
        this.loanProductRepository = loanProductRepository;
    }


}