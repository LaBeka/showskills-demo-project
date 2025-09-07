package edu.example.demoproject.repos;

import edu.example.demoproject.entities.image.ClientImage;
import edu.example.demoproject.entities.image.UserImage;
import edu.example.demoproject.entities.image.ProductImage;
import edu.example.demoproject.entities.image.ImageEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageRepository extends BaseRepository<ImageEntity, Long>{

    public Optional<ProductImage> findProductImageById(Long id) {
        return em.createQuery(
                "select p from ProductImage p " +
                    "where p.product.id = :pid " +
                    "order by p.createdAt desc", ProductImage.class)
            .setParameter("pid", id)
            .setMaxResults(1)
            .getResultStream()
            .findFirst();
    }

    public List<ProductImage> findByProductId(Long productId) {
        return em.createQuery(
                "SELECT i FROM ProductImage i WHERE i.product.id = :productId",
                ProductImage.class)
            .setParameter("productId", productId)
            .getResultList();
    }

    public Optional<UserImage> findUserImageById(Long userId) {
        return em.createQuery(
                "select i from UserImage i " +
                    "where i.user.id = :uid " +
                    "order by i.createdAt desc", UserImage.class)
            .setParameter("uid", userId)
            .setMaxResults(1)
            .getResultStream()
            .findFirst();
    }

    public List<UserImage> findByUserId(Long userId) {
        return em.createQuery(
                "SELECT i FROM UserImage i WHERE i.user.id = :userId",
                UserImage.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    public Optional<ClientImage> findClientImageById(Long id) {
        return em.createQuery(
                "select c from ClientImage c " +
                    "where c.client.id = :cid " +
                    "order by c.createdAt desc", ClientImage.class)
            .setParameter("cid", id)
            .setMaxResults(1)
            .getResultStream()
            .findFirst();
    }

    public List<ClientImage> findByClientId(Long clientId) {
        List<ClientImage> clientId1 = em.createQuery(
                "SELECT i FROM ClientImage i WHERE i.client.id = :clientId",
                ClientImage.class)
            .setParameter("clientId", clientId)
            .getResultList();
        return clientId1;
    }

    public ImageEntity findAnyById(Long id) {
        return em.find(ImageEntity.class, id);
    }

    public void deleteById(Long id) {
        var managed = findAnyById(id);
        if (managed != null) em.remove(managed);
    }
}
