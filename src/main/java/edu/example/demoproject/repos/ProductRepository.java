package edu.example.demoproject.repos;

import edu.example.demoproject.dtos.product.ProductCriteriaDto;
import edu.example.demoproject.dtos.product.ProductDto;
import edu.example.demoproject.entities.ProductEntity;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository extends BaseRepository<ProductEntity, Number> {


    public ProductEntity getReferenceById(Long id) {
        return findById(ProductEntity.class, id);
    }

    public ProductDto getFullInfoById(Long id) {
        return em.createQuery("""
                              select new edu.example.demoproject.dtos.product.ProductDto(
                              p.id, p.name, p.description, p.initialPrice, p.discount, 
                              p.currentPrice, p.newProduct, p.brand, p.category)
                              from ProductEntity p
                              where p.id = :id
                              """, ProductDto.class)
                 .setParameter("id", id)
                 .getSingleResult();
    }
    public List<ProductDto> getAllProducts(){
        return em.createQuery("""
                        select new edu.example.demoproject.dtos.product.ProductDto(
                        p.id, p.name, p.description, p.initialPrice, p.discount, 
                        p.currentPrice, p.newProduct, p.brand, p.category)
                        from ProductEntity p
                        """, ProductDto.class)
                .getResultList();
    }
    public List<ProductDto> findCaseInsensitiveBy(String title){
        return em.createQuery("""
                              select new edu.example.demoproject.dtos.product.ProductDto(
                              p.id, p.name, p.description, p.initialPrice, p.discount, 
                              p.currentPrice, p.newProduct, p.brand, p.category)
                              from ProductEntity p
                              where lower(p.name) like concat('%',:title,'%') 
                              or lower(p.description) like concat('%',:title,'%')
                              """, ProductDto.class)
                .setParameter("title",
                        title.toLowerCase()
                                .strip())
                .getResultList();
    }


    public void updateProduct(Long id, ProductEntity dto) {
        StringBuilder querySb = new StringBuilder("""
                                                  update ProductEntity s
                                                  set 
                                                  """);
        Map<String, Object> params = new HashMap<>();

        querySb.append("s.name = :name");
        params.put("name", dto.getName());
        querySb.append(", s.description = :description");
        params.put("description", dto.getDescription());
        querySb.append(", s.initialPrice = :initialPrice");
        params.put("initialPrice", dto.getInitialPrice());
        querySb.append(", s.currentPrice = :currentPrice");
        params.put("currentPrice", dto.getCurrentPrice());
        querySb.append(", s.storageQty = :storageQty");
        params.put("storageQty", dto.getStorageQty());
        querySb.append(", s.discount = :discount");
        params.put("discount", dto.getDiscount());
        querySb.append(", s.brand = :brand");
        params.put("brand", dto.getBrand());
        querySb.append(", s.category = :category");
        params.put("category", dto.getCategory());

        querySb.append(" where s.id =:id");
        params.put("id", id);

        Query query = em.createQuery(querySb.toString());
        params.forEach(query::setParameter);
        query.executeUpdate();
    }

    public List<ProductEntity> findByCriteria(ProductCriteriaDto dto) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> from = criteriaQuery.from(ProductEntity.class);
        CriteriaQuery<ProductEntity> select = criteriaQuery.select(from);
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getName() != null) {
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(from.get("name")), "%" + dto.getName().toLowerCase() + "%");
            criteriaQuery.where(predicate);
            predicates.add(predicate);
        }
        if (dto.getDescription() != null) {
            Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(from.get("description")), "%" + dto.getDescription().toLowerCase() + "%");
            criteriaQuery.where(predicate);
            predicates.add(predicate);
        }

        if (dto.isNewProduct()) {
            Predicate predicate = criteriaBuilder.equal(from.get("newProduct"), dto.isNewProduct());
            criteriaQuery.where(predicate);
            predicates.add(predicate);
        }

        if (dto.getBrand() != null) {
            Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(from.get("brand")), dto.getBrand().toLowerCase());
            criteriaQuery.where(predicate);
            predicates.add(predicate);
        }

        if (dto.getCategory() != null) {
            Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(from.get("category")), dto.getCategory().toLowerCase());
            criteriaQuery.where(predicate);
            predicates.add(predicate);
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(Predicate[]::new)));
        TypedQuery<ProductEntity> typedQuery = em.createQuery(select);

        return typedQuery.getResultList();
    }

    public void delete(Long id) {
        Query query = em.createQuery("""
                              delete
                              from ProductEntity p
                              where p.id = :id
                              """)
                .setParameter("id", id);
        query.executeUpdate();
    }

}