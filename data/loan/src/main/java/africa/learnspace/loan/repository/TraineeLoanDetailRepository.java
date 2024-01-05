package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.models.trainee.TraineeLoanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TraineeLoanDetailRepository extends JpaRepository<TraineeLoanDetail, String> {
    Optional<TraineeLoanDetail> findByTrainee(Trainee trainee);
//    boolean existsByTraineeTraineeIdAndTraineeLoanDetailIsNotNull(String traineeId);
    Optional<TraineeLoanDetail> findByTrainee_TraineeId (String id);

    boolean existsByTrainee_TraineeId(String traineeId);
}