package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import org.springframework.stereotype.Component;

import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.repository.TraineeLoanDetailRepository;

import java.util.Optional;

@Component
public class TraineeLoanDetailManager {

    private final TraineeLoanDetailRepository traineeLoanDetailRepository;

    public TraineeLoanDetailManager(TraineeLoanDetailRepository traineeDetailRepository) {
        this.traineeLoanDetailRepository = traineeDetailRepository;
    }
    public TraineeLoanDetail createTraineeDetail(TraineeLoanDetail traineeDetail) {
        return traineeLoanDetailRepository.save(traineeDetail);
    }
    public TraineeLoanDetail findLoanDetailByTrainee(Trainee trainee){
        return traineeLoanDetailRepository.findByTrainee(trainee).orElse(null);
    }

    public boolean doesTraineeLoanDetailExist(String traineeId){
        return traineeLoanDetailRepository.existsByTrainee_TraineeId(traineeId);
    }
    public Optional<TraineeLoanDetail> findByTraineeId(String id){
        return traineeLoanDetailRepository.findByTrainee_TraineeId(id);
    }
}

