package edu.example.demoproject.entities.image;

import edu.example.demoproject.entities.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@DiscriminatorValue("PRODUCT")
public class ProductImage extends ImageEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private ProductEntity product;

}
