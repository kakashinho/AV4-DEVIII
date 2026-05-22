package com.autobots.automanager.porta;

import java.util.List;

public interface MercadoriaPort {
    List<MercadoriaInfo> buscarPorIds(List<Long> ids);
}
