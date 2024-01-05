package africa.learnspace.loan.models.institute;

import africa.learnspace.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InstituteEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private boolean isAdmin;
    @ManyToOne
    private User user;
    @ManyToOne
    private Institute institute;
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
