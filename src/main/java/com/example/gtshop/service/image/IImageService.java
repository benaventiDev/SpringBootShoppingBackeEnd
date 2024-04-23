package com.example.gtshop.service.image;

import com.example.gtshop.dto.ImageDto;
import com.example.gtshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    ImageDto getImageDtoById(Long id);
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
