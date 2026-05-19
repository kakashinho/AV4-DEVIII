package com.autobots.automanager.servico;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.requisicao.EmpresaUpdateRequest;
import com.autobots.automanager.dto.requisicao.MercadoriaRequest;
import com.autobots.automanager.dto.requisicao.ServicoRequest;
import com.autobots.automanager.dto.requisicao.VendaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.MercadoriaResponse;
import com.autobots.automanager.dto.resposta.ServicoResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.dto.resposta.VendaResponse;
import com.autobots.automanager.entidade.Empresa;
import com.autobots.automanager.entidade.Venda;
import com.autobots.automanager.enumeracao.PerfilUsuario;

import java.util.List;
import java.util.Set;

public interface EmpresaService {

    // ─── CRUD principal ───────────────────────────────────────────────────────
    List<EmpresaResumo> listarTodas();
    EmpresaResponse buscarPorId(Long id);
    EmpresaResponse cadastrar(EmpresaRequest request);
    EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request);
    void remover(Long id);

    // ─── Usuários da empresa ──────────────────────────────────────────────────
    Set<UsuarioReferencia> listarUsuarios(Long empresaId, PerfilUsuario perfil);
    void associarUsuario(Long empresaId, Long usuarioId);
    void desassociarUsuario(Long empresaId, Long usuarioId);

    // ─── Mercadorias da empresa ───────────────────────────────────────────────
    List<MercadoriaResponse> listarMercadorias(Long empresaId);
    MercadoriaResponse criarMercadoria(Long empresaId, MercadoriaRequest request);
    MercadoriaResponse associarMercadoria(Long empresaId, Long mercadoriaId);
    void desassociarMercadoria(Long empresaId, Long mercadoriaId);

    // ─── Serviços da empresa ──────────────────────────────────────────────────
    List<ServicoResponse> listarServicos(Long empresaId);
    ServicoResponse criarServico(Long empresaId, ServicoRequest request);
    void removerServico(Long empresaId, Long servicoId);

    // ─── Vendas da empresa ────────────────────────────────────────────────────
    List<Venda> listarVendas(Long empresaId);
    Venda obterVenda(Long empresaId, Long vendaId);
    Venda criarVenda(Long empresaId, VendaRequest request);

    // ─── Apoio (uso interno entre controllers) ────────────────────────────────
    Empresa obterEmpresa(Long id);
}
