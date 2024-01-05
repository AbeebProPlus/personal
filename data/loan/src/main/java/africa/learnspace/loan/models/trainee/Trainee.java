package africa.learnspace.loan.models.trainee;

import africa.learnspace.loan.models.cohort.Cohort;
import africa.learnspace.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String traineeId;
    private BigDecimal initialDeposit;
    private String status;
    private boolean verified;
    private String fitnessToWorkRating;
    private String nextOfKin;
    private String educationalBackground;
    private LocalDate dateOfBirth;
    @OneToOne
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Cohort cohort;
}