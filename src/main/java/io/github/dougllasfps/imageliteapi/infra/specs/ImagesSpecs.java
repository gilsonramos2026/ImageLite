package io.github.dougllasfps.imageliteapi.infra.specs;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import org.springframework.data.jpa.domain.Specification;

public class ImagesSpecs {

    // Construtor privado corrigido para bater com o nome da classe
    private ImagesSpecs() {
    }

    public static Specification<Image> extensionEquals(ImageExtension extension) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("extension"), extension);
    }

    public static Specification<Image> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<Image> tagsLike(String tags) {
        // CORREÇÃO: Alterado de root.get("name") para root.get("tags")
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")), "%" + tags.toUpperCase() + "%");
    }
}
