package edu.example.demoproject.repos;

import edu.example.demoproject.entities.ClientEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository extends BaseRepository<ClientEntity, Number> {

  public ClientEntity getReferenceById(Long id) {
    return findById(ClientEntity.class, id);
  }
}
