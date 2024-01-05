package africa.learnspace.loan.models.trainee;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BankDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bankDetailId;

    private String bankName;

    private String accountNumber;

    private String accountName;

    private String bankCode;

    @ManyToOne(cascade = CascadeType.ALL)
    private Trainee trainee;
}
