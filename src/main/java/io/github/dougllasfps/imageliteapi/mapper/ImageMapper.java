package io.github.dougllasfps.imageliteapi.mapper;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import io.github.dougllasfps.imageliteapi.dto.ImageDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class ImageMapper {

    public Image mapToImage(MultipartFile file, String name, List<String> tags) throws IOException {
        return Image.builder()
                .name(name)
                .tags(String.join(",", tags))
                .size(file.getSize())
                .extension(ImageExtension.fromMediaType(MediaType.parseMediaType(file.getContentType())))
                .file(file.getBytes())
                .build();
    }

    public ImageDto imageToDTO(Image image, String url) {
        return ImageDto.builder()
                .url(url)
                .extension(image.getExtension().name())
                .name(image.getName())
                .size(image.getSize())
                // 🟢 CORREÇÃO: Mapeia o uploadDate da entidade para o DTO
                // Converte de LocalDateTime para LocalDate se você preferir trafegar apenas ano-mês-dia
                .uploadDate(image.getUploadDate() != null ? image.getUploadDate().toLocalDate() : null)
                .build();
    }
}
