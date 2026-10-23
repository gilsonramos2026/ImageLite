package io.github.dougllasfps.imageliteapi.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ImageDto(
     String url,
     String name,
     String extension,
     Long  size,
     LocalDate uploadDate
) {
}
