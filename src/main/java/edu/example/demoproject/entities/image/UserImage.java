package edu.example.demoproject.entities.image;

import edu.example.demoproject.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@DiscriminatorValue("USER")
public class UserImage extends ImageEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

}
