package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.DocumentoRequest;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.enumeracao.TipoDocumento;
import com.autobots.automanager.excecao.DocumentoAssociadoException;
import com.autobots.automanager.excecao.DocumentoDuplicadoException;
import com.autobots.automanager.excecao.DocumentoNaoEncontradoException;
import com.autobots.automanager.excecao.ValidationException;
import com.autobots.automanager.repositorio.RepositorioDocumento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final RepositorioDocumento repositorio;

    // ─── Validação de ID ──────────────────────────────────────────────────────

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("O ID deve ser maior que zero.");
        }
    }

    // ─── Normalização: remove pontos, traços, barras e espaços ───────────────

    private String normalizar(String numero) {
        return numero.replaceAll("[.\\-/ ]", "").trim();
    }

    // ─── Validação semântica por tipo ─────────────────────────────────────────

    private void validarNumeroDocumento(TipoDocumento tipo, String numeroNorm) {
        boolean valido = switch (tipo) {
            case CPF        -> numeroNorm.matches("\\d{11}");
            case CNPJ       -> numeroNorm.matches("\\d{14}");
            case RG         -> numeroNorm.matches("[a-zA-Z0-9]{5,}");
            case CNH        -> numeroNorm.matches("\\d{11}");
            case PASSAPORTE -> numeroNorm.matches("[a-zA-Z0-9]{5,}");
        };
        if (!valido) {
            throw new ValidationException(mensagemValidacaoPorTipo(tipo));
        }
    }

    private String mensagemValidacaoPorTipo(TipoDocumento tipo) {
        return switch (tipo) {
            case CPF        -> "CPF deve conter exatamente 11 dígitos numéricos.";
            case CNPJ       -> "CNPJ deve conter exatamente 14 dígitos numéricos.";
            case RG         -> "RG deve conter no mínimo 5 caracteres alfanuméricos.";
            case CNH        -> "CNH deve conter exatamente 11 dígitos numéricos.";
            case PASSAPORTE -> "Passaporte deve conter no mínimo 5 caracteres alfanuméricos.";
        };
    }

    // ─── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public Documento criar(DocumentoRequest request) {
        String numeroNorm = normalizar(request.getNumero());
        validarNumeroDocumento(request.getTipo(), numeroNorm);

        if (repositorio.existsByTipoAndNumero(request.getTipo(), numeroNorm)) {
            throw new DocumentoDuplicadoException(request.getTipo(), numeroNorm);
        }

        Documento doc = new Documento();
        doc.setTipo(request.getTipo());
        doc.setNumero(numeroNorm);
        doc.setDataEmissao(request.getDataEmissao());
        return repositorio.save(doc);
    }

    @Override
    public Documento buscarPorId(Long id) {
        validarId(id);
        return repositorio.findById(id)
                .orElseThrow(() -> new DocumentoNaoEncontradoException(id));
    }

    @Override
    public List<Documento> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    @Transactional
    public Documento atualizar(Long id, DocumentoRequest request) {
        Documento doc = buscarPorId(id);
        String numeroNorm = normalizar(request.getNumero());
        validarNumeroDocumento(request.getTipo(), numeroNorm);

        boolean mudou = !doc.getTipo().equals(request.getTipo()) || !doc.getNumero().equals(numeroNorm);
        if (mudou && repositorio.existsByTipoAndNumero(request.getTipo(), numeroNorm)) {
            throw new DocumentoDuplicadoException(request.getTipo(), numeroNorm);
        }

        doc.setTipo(request.getTipo());
        doc.setNumero(numeroNorm);
        doc.setDataEmissao(request.getDataEmissao());
        return repositorio.save(doc);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Documento doc = buscarPorId(id);
        if (repositorio.existeAssociadoAUsuario(id)) {
            throw new DocumentoAssociadoException(id);
        }
        repositorio.delete(doc);
    }
}
