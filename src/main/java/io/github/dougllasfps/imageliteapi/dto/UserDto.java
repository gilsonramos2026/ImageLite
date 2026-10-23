package io.github.dougllasfps.imageliteapi.dto;

import lombok.Builder;

@Builder
public record UserDto (
      String name,
      String email,
      String password
){}
