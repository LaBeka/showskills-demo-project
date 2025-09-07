package edu.example.demoproject.entities.user;


import jakarta.persistence.*;

@Entity
@Table(name = "user_updates")
public class UserUpdate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;   // the user being updated

  @ManyToOne
  @JoinColumn(name = "updated_by_user_id", nullable = false)
  private UserEntity updatedBy; // who did the update

  @Column(name = "updated_at", nullable = false)
  private java.time.Instant updatedAt;  // timestamp of update
}
