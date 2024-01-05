package africa.learnspace.loan.manager;
import africa.learnspace.loan.models.trainee.Payment;
import africa.learnspace.loan.models.trainee.Trainee;
import africa.learnspace.loan.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentManager {

    private final PaymentRepository paymentRepository;

    public PaymentManager(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> findTraineePayments(Trainee trainee){
        return paymentRepository.findByTrainee(trainee);
    }

}