package africa.learnspace.investment.manager;


import africa.learnspace.investment.model.Investor;
import africa.learnspace.investment.repository.InvestorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InvestorManager {

    private final InvestorRepository investorRepository;

    public InvestorManager(InvestorRepository investorRepository) {
        this.investorRepository = investorRepository;
    }

    public Investor createInvestor(Investor investor){
        return investorRepository.save(investor);
    }

    public Investor save(Investor investor){
        return investorRepository.save(investor);
    }

    public Optional<Investor> findInvestorUserById(String id){
        return investorRepository.findById(id);
    }

    public String findInvestorByName(String email) {
        return investorRepository.findInvestorByEmail();
    }
}