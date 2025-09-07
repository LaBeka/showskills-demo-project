package edu.example.demoproject.repos;

import edu.example.demoproject.dtos.item.ItemCreateDto;
import edu.example.demoproject.dtos.item.ItemDto;
import edu.example.demoproject.entities.ItemEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepository extends BaseRepository<ItemEntity, Number> {

    public List<ItemDto> getItemsByCartId(Long cartId) {
        return em.createQuery("""
                              select new edu.example.demoproject.dtos.item.ItemDto(
                              i.id, i.cartId, i.productId, i.qty)
                              from ItemEntity i
                              where i.cartId = :cartId
                              """, ItemDto.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public Optional<ItemDto> ifItemInCartExists(ItemCreateDto dto) {
        return em.createQuery("""
                        select new edu.example.demoproject.dtos.item.ItemDto(
                        i.id, i.cartId, i.productId, i.qty)
                        from ItemEntity i
                        where i.productId = :productId
                        and i.cartId = :cartId
                        """, ItemDto.class)
                .setParameter("productId", dto.getProductId())
                .setParameter("cartId", dto.getCartId())
                .getResultStream()
                .findFirst();
    }

    public void incrementQty(ItemDto dto) {
        em.createQuery("update ItemEntity i set i.qty = :qty where i.id = :id")
                .setParameter("qty", dto.getQty() + 1)
                .setParameter("id", dto.getId())
                .executeUpdate();
    }

    public void decrementQty(ItemDto dto) {
        em.createQuery("update ItemEntity i set i.qty = :qty where i.id = :id")
                .setParameter("qty", dto.getQty() - 1)
                .setParameter("id", dto.getId())
                .executeUpdate();
    }

    public void deleteIfQtyNull(ItemDto dto) {
        em.createQuery("delete ItemEntity i where i.qty = 0 and i.id = :id")
                .setParameter("id", dto.getId())
                .executeUpdate();
    }

    public void delete(Long productId) {
        em.createQuery("delete ItemEntity i where i.productId = :productId")
                .setParameter("productId", productId)
                .executeUpdate();
    }

    public ItemDto getItemByItsProductId(Long productId) {
        return em.createQuery("""
                              select new edu.example.demoproject.dtos.item.ItemDto(
                              i.id, i.cartId, i.productId, i.qty)
                              from ItemEntity i
                              where i.productId = :productId
                              """, ItemDto.class)
                .setParameter("productId", productId)
                .getSingleResult();
    }
}
