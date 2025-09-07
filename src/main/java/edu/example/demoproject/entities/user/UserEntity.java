package edu.example.demoproject.entities.user;

import edu.example.demoproject.entities.ClientEntity;
import edu.example.demoproject.entities.RoleEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Collection;

import static org.hibernate.annotations.CascadeType.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Table(name="user_entities")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 128, name="user_id")
    private Long id;

    @Column(length = 128, name="first_name", nullable = false)
    private String firstName;

    @Column(length = 128, name="last_name", nullable = false)
    private String lastName;

    @Column(length = 128, name="account_name")
    private String accountName;

    @Column(length = 128, name="email")
    private String email;

    @Column(length = 128, name="password")
    private String password;

    @Column(name="enabled")
    private boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Cascade({DETACH})
    private Collection<RoleEntity> roles;

    @OneToMany(mappedBy = "user")
    private Collection<UserUpdate> updates;

    //to track the updates done by a user:
    @OneToMany(mappedBy = "updatedBy")
    private Collection<UserUpdate> updatesDone;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity clientId;

}
