package edu.example.demoproject.mappers;

import edu.example.demoproject.contextHelpers.ImageMappingContext;
import edu.example.demoproject.dtos.image.ImageDto;
import edu.example.demoproject.dtos.image.ImageType;
import edu.example.demoproject.entities.image.ClientImage;
import edu.example.demoproject.entities.image.ImageEntity;
import edu.example.demoproject.entities.image.ProductImage;
import edu.example.demoproject.entities.image.UserImage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    public abstract ImageEntity toEntity(ImageDto dto, @Context ImageMappingContext ctx);

    @ObjectFactory
    protected ImageEntity create(ImageDto dto) {
        if (dto == null) return null;
        return switch (dto.getType()) {
            case PRODUCT -> new ProductImage();
            case USER    -> new UserImage();
            case CLIENT  -> new ClientImage();
        };
    }

    @AfterMapping
    protected void wireOwner(ImageDto dto, @MappingTarget ImageEntity target, @Context ImageMappingContext ctx) {
        if (dto == null || target == null) return;

        target.setId(dto.getId());
        target.setImageName(dto.getImageName());

        switch (dto.getType()) {
            case PRODUCT -> ((ProductImage) target).setProduct(ctx.refProduct(dto.getOwnerId()));
            case USER    -> ((UserImage) target).setUser(ctx.refUser(dto.getOwnerId()));
            case CLIENT  -> ((ClientImage) target).setClient(ctx.refClient(dto.getOwnerId()));
        }
    }

    // ---------- Entity -> DTO ----------
    public ImageDto toDto(ImageEntity e) {
        if (e == null) return null;
        if (e instanceof ProductImage pi) {
            return new ImageDto(e.getId(), e.getImageName(), ImageType.PRODUCT,
                pi.getProduct() != null ? pi.getProduct().getId() : null);
        } else if (e instanceof UserImage ui) {
            return new ImageDto(e.getId(), e.getImageName(), ImageType.USER,
                ui.getUser() != null ? ui.getUser().getId() : null);
        } else if (e instanceof ClientImage ci) {
            return new ImageDto(e.getId(), e.getImageName(), ImageType.CLIENT,
                ci.getClient() != null ? ci.getClient().getId() : null);
        }
        throw new IllegalArgumentException("Unknown ImageEntity subtype: " + e.getClass());
    }

}
