package com.autobots.automanager.porta;

import java.util.List;

public interface ServicoPort {
    List<ServicoInfo> buscarPorIds(List<Long> ids);
}
