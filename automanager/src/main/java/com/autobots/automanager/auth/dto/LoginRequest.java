package com.autobots.automanager.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String nomeUsuario,
        @NotBlank String senha) {
}
