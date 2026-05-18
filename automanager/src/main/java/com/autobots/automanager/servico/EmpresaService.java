package com.autobots.automanager.servico;

import com.autobots.automanager.dto.EmpresaResumo;
import com.autobots.automanager.dto.requisicao.EmpresaRequest;
import com.autobots.automanager.dto.resposta.EmpresaResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.entidade.Empresa;

import java.util.List;
import java.util.Set;

public interface EmpresaService {

    List<EmpresaResumo> listarTodas();
    EmpresaResponse buscarPorId(Long id);
    EmpresaResponse cadastrar(EmpresaRequest request);
    EmpresaResponse atualizar(Long id, EmpresaRequest request);
    void remover(Long id);

    Set<UsuarioReferencia> listarUsuarios(Long empresaId);
    void associarUsuario(Long empresaId, Long usuarioId);
    void desassociarUsuario(Long empresaId, Long usuarioId);

    Empresa obterEmpresa(Long id);
}