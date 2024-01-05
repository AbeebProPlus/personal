package africa.learnspace.loan.repository;
import africa.learnspace.loan.models.trainee.Payment;
import africa.learnspace.loan.models.trainee.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByTrainee(Trainee trainee);
}
