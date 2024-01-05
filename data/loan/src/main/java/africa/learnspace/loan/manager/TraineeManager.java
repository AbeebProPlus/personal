package africa.learnspace.loan.manager;

import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.repository.TraineeRepository;
import africa.learnspace.loan.utils.LoanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static africa.learnspace.loan.utils.ConstantUtils.INVALID_DETAILS;

@Component
public class TraineeManager {
    private final TraineeRepository traineeRepository;

    public TraineeManager(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    public Page<Trainee> findAllByCohort(Cohort cohort, Pageable pageable) {
        return traineeRepository.findAllByCohort(cohort, pageable);
    }

    public Optional<Trainee> findTraineeInCohort(String traineeId, Cohort cohort) {
        return traineeRepository.findByTraineeIdAndCohort(traineeId, cohort);
    }

    public boolean trainingExistsInCohort(String traineeEmail){
        return traineeRepository.existsByUserEmail(traineeEmail);
    }

    public boolean findTraineeByEmail(String email){
        return traineeRepository.existsByUserEmail(email);
    }

    public Optional<Trainee> findTraineeById(String id) {
        return traineeRepository.findByTraineeId(id);
    }

    public void deleteTrainee(String traineeId) {
        traineeRepository.deleteByTraineeId(traineeId);
    }
    public List<Trainee> getAllTraineesByCohortId(String cohortId){
        return traineeRepository.findAllByCohort_CohortId(cohortId);
    }

    public Trainee findById(String traineeId) {
        return traineeRepository.findById(traineeId).orElseThrow(() -> new LoanException(INVALID_DETAILS) );
    }

}
