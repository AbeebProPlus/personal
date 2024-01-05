package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;
    private BigDecimal deposit;
    private String paymentReference;
    private String paymentMethod;
    @CreationTimestamp
    private LocalDateTime paymentDate;
    @ManyToOne(cascade = CascadeType.ALL)
    private Trainee trainee;
}
