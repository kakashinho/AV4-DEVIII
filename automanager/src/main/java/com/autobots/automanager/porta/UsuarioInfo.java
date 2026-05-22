package com.autobots.automanager.porta;

import com.autobots.automanager.enumeracao.PerfilUsuario;

import java.util.Set;

public record UsuarioInfo(Long id, String nome, Set<PerfilUsuario> perfis) {}
