package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.EnderecoRequest;
import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.requisicao.UsuarioUpdateRequest;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Endereco;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.entidade.Veiculo;
import com.autobots.automanager.entidade.Venda;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsuarioService {
    Usuario cadastrar(UsuarioRequest request, Authentication authentication);
    Usuario buscarPorId(Long id);
    List<Usuario> listarTodos();
    Usuario atualizar(Long id, UsuarioUpdateRequest request, Authentication authentication);
    void remover(Long id, Authentication authentication);

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

    Endereco buscarEndereco(Long usuarioId);
    Endereco definirEndereco(Long usuarioId, EnderecoRequest request);
    void removerEndereco(Long usuarioId);

    List<Venda> listarVendas(Long usuarioId, Authentication authentication);
    Venda associarVenda(Long usuarioId, Long vendaId, Authentication authentication);
    void desassociarVenda(Long usuarioId, Long vendaId, Authentication authentication);

    // Usado por EmpresaService ao remover uma empresa
    void desassociarEmpresa(Long usuarioId);
}
