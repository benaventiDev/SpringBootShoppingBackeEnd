package com.example.gtshop.service.image;

import com.example.gtshop.dto.ImageDto;
import com.example.gtshop.exceptions.ResourceNotFoundException;
import com.example.gtshop.model.Image;
import com.example.gtshop.model.Product;
import com.example.gtshop.repository.ImageRepository;
import com.example.gtshop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public ImageDto getImageDtoById(Long id) {
        Image image = getImageById(id);
        return convertImageToDto(image);
    }
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }


    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () ->
        {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });
    }


    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for(MultipartFile file :  files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedimage = imageRepository.save(image);

                savedimage.setDownloadUrl(buildDownloadUrl + savedimage.getId());
                imageRepository.save(savedimage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedimage.getId());
                imageDto.setFileName(savedimage.getFileName());
                imageDto.setDownloadUrl(savedimage.getDownloadUrl());
                savedImageDto.add(imageDto);
            }catch (IOException| SQLException e){
                throw new RuntimeException(e.getMessage());
            }

        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setFileType(file.getContentType());
            imageRepository.save(image);
        }catch (IOException|SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private ImageDto convertImageToDto(Image image){
        return modelMapper.map(image, ImageDto.class);
    }
}
