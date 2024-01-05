package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.trainee.BankDetail;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.repository.BankDetailRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankDetailManager {
    private final BankDetailRepository bankDetailRepository;

    public BankDetailManager(BankDetailRepository bankDetailRepository) {
        this.bankDetailRepository = bankDetailRepository;
    }

    public BankDetail createBankDetail(BankDetail bankDetail) {
        return bankDetailRepository.save(bankDetail);
    }

    public List<BankDetail> getTraineeBankDetails(Trainee trainee){
        return bankDetailRepository.findByTrainee(trainee);
    }
}