package edu.example.demoproject.services;

import edu.example.demoproject.dtos.product.ProductSearchDto;
import edu.example.demoproject.dtos.product.ProductCreateDto;
import edu.example.demoproject.entities.ProductEntity;
import edu.example.demoproject.repos.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final EntityManager enManager;

    public boolean isAllowedAddNewProd(ProductCreateDto dto){
        Optional<ProductEntity> findIdenticalProduct = productRepository.findByNameAndBrand(dto.getName(), dto.getBrand());
        return findIdenticalProduct.isPresent();
    }

    public Optional<ProductEntity> saveProduct(ProductCreateDto dto) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        MultipartFile photo = dto.getImage();

        String newName = dtf.format(LocalDateTime.now()) +
                "-" + photo.getOriginalFilename();

        Path mainDirectoryForProdImages = Paths.get("storage");
        Files.createDirectories(mainDirectoryForProdImages);
        Path newFilePath = mainDirectoryForProdImages.resolve(newName); //ad this file name
        photo.transferTo(newFilePath);

        ProductEntity productEntity = ProductEntity.builder()
                .name(dto.getName())
                .image(newName)
                .description(dto.getDescription())
                .initialPrice(dto.getInitialPrice())
                .storageQty(dto.getStorageQty())
                .discount(dto.getDiscount())
                .newProduct(true)
                .brand(dto.getBrand())
                .category(dto.getCategory())
                .build();

        Optional<ProductEntity> saved = Optional.of(productRepository.save(productEntity));
        return saved;
    }

    public Optional<ProductEntity> findProductById(Long id) {
        return productRepository.findById(id);
    }

    private Long getTotalCount(CriteriaBuilder criteriaBuilder, Predicate predicate){
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> root = criteriaQuery.from(ProductEntity.class);
        criteriaQuery.select(criteriaBuilder.count(root));

        criteriaQuery.where(predicate);
        TypedQuery<Long> query = enManager.createQuery(criteriaQuery);
        return query.getSingleResult();
    }
    private Expression<String> getLoweredWord(String word, CriteriaBuilder criteriaBuilder){
        Expression<String> theWord = criteriaBuilder.literal("%" + word + "%");
       return criteriaBuilder.lower(theWord);
    }

    private Expression<String> getLoweredField(javax.persistence.criteria.Path<String> field, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lower(field);
    }

    private PageImpl<ProductEntity> returnSearch(Pageable pageable, CriteriaBuilder criteriaBuilder, CriteriaQuery<ProductEntity> criteriaQuery,
                                           Root<ProductEntity> from, CriteriaQuery<ProductEntity> select, Predicate predicates) {
        criteriaQuery.orderBy(criteriaBuilder.desc(from.get("currentPrice")));
        TypedQuery<ProductEntity> typedQuery = enManager.createQuery(select);

        typedQuery.setFirstResult(Math.toIntExact(pageable.getOffset()));
        typedQuery.setMaxResults(pageable.getPageSize());

        List<ProductEntity> resultList = typedQuery.getResultList();
        return new PageImpl<ProductEntity>(resultList, pageable, getTotalCount(criteriaBuilder, predicates));
    }

    public PageImpl<ProductEntity> rawSearch(ProductSearchDto dto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = enManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery =
                criteriaBuilder.createQuery(ProductEntity.class);

        Root<ProductEntity> from = criteriaQuery.from(ProductEntity.class);
        CriteriaQuery<ProductEntity> select = criteriaQuery.select(from);
        Predicate predicate = null;

        if (dto.getBrand() != null) {
            predicate = criteriaBuilder.equal(from.get("brand"), dto.getBrand());
        }

        if (dto.isNewProduct()) {
            predicate = criteriaBuilder.equal(from.get("newProduct"), dto.isNewProduct());
        }
        if (dto.getCategory() != null) {
            predicate = criteriaBuilder.equal(from.get("category"), dto.getCategory());
        }

        if (dto.isLoweredPrice()) {
            predicate= criteriaBuilder.between(from.get("discount"), 1, 99);
        }

        criteriaQuery.where(predicate);
        return returnSearch(pageable, criteriaBuilder, criteriaQuery, from, select, null);
    }
    public PageImpl<ProductEntity> searchInWord(String word, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = enManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> from = criteriaQuery.from(ProductEntity.class);
        CriteriaQuery<ProductEntity> select = criteriaQuery.select(from);

        Expression<String> lowSearchWord = getLoweredWord(word, criteriaBuilder);
        Expression<String> lowFieldName = getLoweredField(from.get("name"), criteriaBuilder);
        Predicate predicateName = criteriaBuilder.like(lowFieldName, lowSearchWord);

        Expression<String> loweredDescr = getLoweredField(from.get("description"), criteriaBuilder);
        Predicate predicateDescription = criteriaBuilder.like(loweredDescr, lowSearchWord);

        Predicate finalPredicate = criteriaBuilder.or(predicateName, predicateDescription);
        criteriaQuery.where(finalPredicate);

        return returnSearch(pageable, criteriaBuilder, criteriaQuery, from, select, finalPredicate);
    }

    public Optional<ProductEntity> getProduct(Long id) {
        return productRepository.findById(id);
    }
}
