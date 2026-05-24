package com.autobots.automanager.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginCodigoBarraRequest(@NotNull Long codigo) {}
