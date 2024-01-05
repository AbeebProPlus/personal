package africa.learnspace.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private boolean isDisabled = true;

    private boolean isVerified;

    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;
}