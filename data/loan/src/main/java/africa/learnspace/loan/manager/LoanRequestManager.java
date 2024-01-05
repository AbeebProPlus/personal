package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.trainee.LoanRequest;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.repository.LoanRequestRepository;
import africa.learnspace.loan.utils.LoanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static africa.learnspace.loan.utils.ConstantUtils.INVALID_DETAILS;

@Component
public class LoanRequestManager {


    private final LoanRequestRepository loanRequestRepository;

    public LoanRequestManager(LoanRequestRepository loanRequestRepository) {
        this.loanRequestRepository = loanRequestRepository;
    }

    public LoanRequest createLoanRequest(LoanRequest loanRequest) {
        return loanRequestRepository.save(loanRequest);
    }

    public LoanRequest findByTraineeEmail(String email){
        return loanRequestRepository.findByTrainee_User_Email(email);
    }

    public LoanRequest findById(String loanRequestId) {
        return loanRequestRepository.findById(loanRequestId).orElseThrow(() -> new LoanException(INVALID_DETAILS));
    }

    public Page<LoanRequest> findAll(Pageable pageable) {
        return loanRequestRepository.findAll(pageable);
    }
    public List<LoanRequest> findAll() {
        return loanRequestRepository.findAll();
    }
    public LoanRequest findLoanRequestByTrainee(Trainee trainee) {
        return loanRequestRepository.findByTrainee(trainee).orElseThrow(() -> new LoanException(INVALID_DETAILS));
    }

    public boolean existsByTrainee(Trainee trainee){
        return loanRequestRepository.existsByTrainee(trainee);
    }
}
