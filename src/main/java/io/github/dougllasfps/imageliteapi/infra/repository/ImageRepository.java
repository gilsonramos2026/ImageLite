package io.github.dougllasfps.imageliteapi.infra.repository;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import io.github.dougllasfps.imageliteapi.infra.specs.GenericSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import java.util.List;

// Importações estáticas das especificações de busca para manter o código limpo
import static io.github.dougllasfps.imageliteapi.infra.specs.GenericSpecs.*;
import static io.github.dougllasfps.imageliteapi.infra.specs.ImagesSpecs.*;
import static org.springframework.data.jpa.domain.Specification.anyOf;
import static org.springframework.data.jpa.domain.Specification.where;

public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    /**
     * Realiza uma busca dinâmica de imagens filtrando por extensão e/ou termo de busca.
     *
     * @param imageExtension Extensão da imagem (opcional)
     * @param query Termo para buscar no nome ou nas tags (opcional)
     * @return Lista de imagens filtradas de acordo com os parâmetros passados
     *
     * SQL equivalente gerado no banco de dados:
     * SELECT * FROM TB_IMAGE WHERE 1 = 1
     *   AND EXTENSION = 'PNG' -- (Apenas se enviado)
     *   AND (UPPER(IMAGE_NAME) LIKE '%TERMO%' OR UPPER(TAGS) LIKE '%TERMO%') -- (Apenas se enviado)
     */
    default List<Image> findByExtensionAndNameOrTagsLike(ImageExtension imageExtension, String query) {

        // 1. Cria a especificação base "conjunction", gerando um "WHERE 1 = 1" neutro no SQL inicial
        Specification<Image> spec = where(conjunction());

        // 2. Filtro condicional por Extensão: se o parâmetro for enviado, concatena uma condição "AND"
        if (imageExtension != null) {
            spec = spec.and(extensionEquals(imageExtension));
        }

        // 3. Filtro de Texto: se houver texto enviado na busca, concatena as condições parciais (LIKE)
        if (StringUtils.hasText(query)) {

            // anyOf funciona como o operador "OR" agrupando as consultas entre parênteses: (name LIKE ... OR tags LIKE ...)
            // O spec.and junta esse bloco "OR" isolado ao restante do SQL principal garantindo a precedência correta
            spec = spec.and(anyOf(nameLike(query), tagsLike(query)));
        }

        // 4. Executa a consulta JPA mapeando a especificação acumulada para os filtros do banco de dados
        return findAll(spec);
    }
}
