package edu.example.demoproject.dtos.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ImageDto {
    private Long id;
    private String imageName;
    private ImageType type;
    private Long ownerId;
}
