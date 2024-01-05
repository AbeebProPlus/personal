package africa.learnspace.loan.models.institute;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InstituteAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String instituteAddressId;

    @ManyToOne()
    private Institute instituteAddress;


    @ManyToOne

    private Institute institute;


}

