package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.requisicao.UsuarioUpdateRequest;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;

import java.util.List;

public interface UsuarioService {
    Usuario cadastrar(UsuarioRequest request);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
    Usuario atualizar(Long id, UsuarioUpdateRequest request);
    void remover(Long id);

    Telefone associarTelefone(Long usuarioId, Long telefoneId);
    void desassociarTelefone(Long usuarioId, Long telefoneId);

    Email associarEmail(Long usuarioId, Long emailId);
    void desassociarEmail(Long usuarioId, Long emailId);

    Documento associarDocumento(Long usuarioId, Long documentoId);
    void desassociarDocumento(Long usuarioId, Long documentoId);

    Credencial associarCredencial(Long usuarioId, Long credencialId);
    void desassociarCredencial(Long usuarioId, Long credencialId);

    List<Veiculo> listarVeiculos(Long usuarioId);
    Veiculo associarVeiculo(Long usuarioId, Long veiculoId);
    void desassociarVeiculo(Long usuarioId, Long veiculoId);

    // Usado por EmpresaService ao remover uma empresa
    void desassociarEmpresa(Long usuarioId);
}
