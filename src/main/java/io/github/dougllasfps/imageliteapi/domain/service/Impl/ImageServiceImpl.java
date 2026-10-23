package io.github.dougllasfps.imageliteapi.domain.service.Impl;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import io.github.dougllasfps.imageliteapi.infra.repository.ImageRepository;
import io.github.dougllasfps.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public List<Image> search(ImageExtension extension, String query) {
        return imageRepository.findByExtensionAndNameOrTagsLike(extension, query);
    }

    @Override
    public void update(Image image) {
        imageRepository.save(image); // O save do Spring Data JPA atualiza automaticamente se o ID já existir
    }


    // Procure por essa classe no seu projeto e adicione o método no final dela:
    @Override
    public void delete(Image image) {
        // 🟢 Executa o método delete nativo do JpaRepository (Ex: imageRepository.delete(image))
        imageRepository.delete(image);
    }

}
