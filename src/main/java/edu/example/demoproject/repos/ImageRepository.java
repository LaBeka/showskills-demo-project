package edu.example.demoproject.repos;

import edu.example.demoproject.entities.image.ClientImage;
import edu.example.demoproject.entities.image.UserImage;
import edu.example.demoproject.entities.image.ProductImage;
import edu.example.demoproject.entities.image.ImageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepository extends BaseRepository<ImageEntity, Long>{

    public List<ProductImage> findByProductId(Long productId) {
        return em.createQuery(
                "SELECT i FROM ProductImage i WHERE i.product.id = :productId",
                ProductImage.class)
            .setParameter("productId", productId)
            .getResultList();
    }

    public List<UserImage> findByUserId(Long userId) {
        return em.createQuery(
                "SELECT i FROM UserImage i WHERE i.user.id = :userId",
                UserImage.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    public List<ClientImage> findByClientId(Long clientId) {
        return em.createQuery(
                "SELECT i FROM ClientImage i WHERE i.client.id = :clientId",
                ClientImage.class)
            .setParameter("clientId", clientId)
            .getResultList();
    }

}
