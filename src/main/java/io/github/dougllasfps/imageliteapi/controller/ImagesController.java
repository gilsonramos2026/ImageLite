package io.github.dougllasfps.imageliteapi.controller;

import io.github.dougllasfps.imageliteapi.domain.entity.Image;
import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import io.github.dougllasfps.imageliteapi.dto.ImageDto;
import io.github.dougllasfps.imageliteapi.mapper.ImageMapper;
import io.github.dougllasfps.imageliteapi.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/images")
@Slf4j
@RequiredArgsConstructor
public class ImagesController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("tags") List<String> tags
    ) throws IOException {
        log.info("Imagem recebida: name: {}, size: {}", file.getOriginalFilename(), file.getSize());

        Image image = imageMapper.mapToImage(file, name, tags);
        Image savedImage = imageService.save(image);
        URI imageUri = buildImageURL(savedImage);

        return ResponseEntity.created(imageUri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        var possibleImage = imageService.findById(id);

        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var image = possibleImage.get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType());
        headers.setContentLength(image.getSize());
        headers.set("Content-Disposition", "inline; filename=\"" + image.getFileName() + "\"");

        return new ResponseEntity<>(image.getFile(), headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> search(
            @RequestParam(value = "extension", required = false) String extension,
            @RequestParam(value = "query", required = false) String query) {

        ImageExtension imageExtension = null;

        if (extension != null && !extension.trim().isEmpty()) {
            try {
                imageExtension = ImageExtension.valueOf(extension.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Extensão inválida recebida: {}", extension);
                return ResponseEntity.badRequest().build();
            }
        }

        var result = imageService.search(imageExtension, query);

        var images = result.stream().map(image -> {
            var url = buildImageURL(image);
            return imageMapper.imageToDTO(image, url.toString());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(images);
    }

    // Adicione o endpoint de atualização no corpo da classe:
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ImageDto dto) {
        log.info("Requisição para atualizar a imagem ID: {}", id);

        var possibleImage = imageService.findById(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Image image = possibleImage.get();
        image.setName(dto.name()); // Atualiza o nome com o dado vindo do Frontend

        imageService.update(image);
        return ResponseEntity.noContent().build(); // Retorna status 204
    }

    // 🟢 NOVO ENDPOINT DELETAR ADICIONADO: Conecta a lixeira do Next.js ao banco de dados
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        log.info("Requisição para deletar a imagem ID: {}", id);

        var possibleImage = imageService.findById(id);
        if (possibleImage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        imageService.delete(possibleImage.get()); // Executa a deleção através do seu service
        return ResponseEntity.noContent().build(); // Retorna status 204 (Sucesso sem conteúdo)
    }

    private URI buildImageURL(Image image) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/v1/images/{id}")
                .buildAndExpand(image.getId())
                .toUri();
    }
}
