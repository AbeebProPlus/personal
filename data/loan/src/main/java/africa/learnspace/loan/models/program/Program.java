package africa.learnspace.loan.models.program;

import africa.learnspace.loan.models.institute.Institute;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String programId;

    @OneToOne()
    private ProgramLoanDetail programLoanDetails;

    private String description;

    private String name;

    private String curriculum;

    private String type;

    private String mode;

    private int duration;

    private String programType;

    private String deliveryType;

    private String programStatus;

    private String outcome;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String createdBy;

    @ManyToOne()
    private Institute institute;
}