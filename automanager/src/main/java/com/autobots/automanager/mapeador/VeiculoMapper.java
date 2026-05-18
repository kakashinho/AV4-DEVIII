package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.requisicao.VeiculoRequest;
import com.autobots.automanager.dto.resposta.VeiculoResponse;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(VeiculoRequest request, Usuario proprietario) {
        Veiculo veiculo = new Veiculo();
        veiculo.setTipo(request.getTipo());
        veiculo.setModelo(request.getModelo());
        veiculo.setPlaca(request.getPlaca());
        veiculo.setProprietario(proprietario);
        return veiculo;
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        VeiculoResponse response = new VeiculoResponse();
        response.setId(veiculo.getId());
        response.setTipo(veiculo.getTipo());
        response.setModelo(veiculo.getModelo());
        response.setPlaca(veiculo.getPlaca());
        if (veiculo.getProprietario() != null) {
            response.setProprietarioId(veiculo.getProprietario().getId());
            response.setProprietarioNome(veiculo.getProprietario().getNome());
        }
        return response;
    }
}