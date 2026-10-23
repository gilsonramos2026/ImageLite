package io.github.dougllasfps.imageliteapi.dto;

import lombok.Builder;

@Builder
public record CredentialDto(
      String email,
      String password
){}
