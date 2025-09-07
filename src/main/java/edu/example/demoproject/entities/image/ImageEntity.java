package edu.example.demoproject.entities.image;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter @Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type", length = 16) // PRODUCT / USER / CLIENT
@Table(name="image_entities")
public abstract class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name="image_name", nullable = false, length = 256)
    private String imageName;

    @Column(name = "created_at", nullable = false)
    protected Instant createdAt = Instant.now();

    @Column(name = "content_type", length = 128)
    protected String contentType;

    @Column(name = "object_key", length = 512) // e.g., MinIO key
    protected String objectKey;
}
