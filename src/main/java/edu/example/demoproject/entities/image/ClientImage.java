package edu.example.demoproject.entities.image;

import edu.example.demoproject.entities.ClientEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@DiscriminatorValue("CLIENT")
public class ClientImage extends ImageEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id")
  private ClientEntity client;
}
