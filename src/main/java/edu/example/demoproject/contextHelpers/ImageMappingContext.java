package edu.example.demoproject.contextHelpers;

import edu.example.demoproject.entities.ClientEntity;
import edu.example.demoproject.entities.ProductEntity;
import edu.example.demoproject.entities.user.UserEntity;
import edu.example.demoproject.repos.ProductRepository;
import edu.example.demoproject.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageMappingContext {

  private final ProductRepository productRepo;
  private final UserRepository userRepo;
//  private final ClientRepository clientRepo;

  public ProductEntity refProduct(Long id)  { return productRepo.getReferenceById(id); }
  public UserEntity refUser(Long id)        { return userRepo.getReferenceById(id); }
//  public ClientEntity refClient(Long id)    { return clientRepo.getReferenceById(id); }


}
