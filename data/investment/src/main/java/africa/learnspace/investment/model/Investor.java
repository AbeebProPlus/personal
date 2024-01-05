package africa.learnspace.investment.model;


import africa.learnspace.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String investorId;
    @OneToOne(cascade = CascadeType.MERGE)
    private  User user;
    private String  createdBy;
    private BigDecimal amountInvested;
}