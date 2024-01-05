package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.trainee.LoanRequest;
import africa.learnspace.loan.models.trainee.Trainee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {
    LoanRequest findByTrainee_User_Email(String traineeEmail);

    Optional<LoanRequest> findByTrainee(Trainee trainee);
    boolean existsByTrainee(Trainee trainee);
}
