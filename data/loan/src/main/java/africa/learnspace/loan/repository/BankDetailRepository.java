package africa.learnspace.loan.repository;

import africa.learnspace.loan.models.trainee.BankDetail;
import africa.learnspace.loan.models.trainee.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankDetailRepository extends JpaRepository<BankDetail, String> {
    List<BankDetail> findByTrainee(Trainee trainee);
}
