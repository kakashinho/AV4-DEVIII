package com.autobots.automanager.porta;

import com.autobots.automanager.enumeracao.TipoVeiculo;

public record VeiculoInfo(Long id, String placa, String modelo, TipoVeiculo tipo, Long proprietarioId) {}
