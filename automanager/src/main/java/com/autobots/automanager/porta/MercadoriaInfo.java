package com.autobots.automanager.porta;

import java.math.BigDecimal;

public record MercadoriaInfo(Long id, String nome, BigDecimal valor, long quantidade) {}
