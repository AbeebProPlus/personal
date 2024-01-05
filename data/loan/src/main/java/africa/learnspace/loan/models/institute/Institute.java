package africa.learnspace.loan.models.institute;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Institute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String instituteId;

    private String instituteName;

    @Enumerated(EnumType.STRING)
    private InstituteStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    private InstituteLoanDetail instituteLoanDetails;

    private String createdBy;

    @CreationTimestamp
    private LocalDateTime dateCreated;

}
