package com.autobots.automanager.excecao;

import com.autobots.automanager.modelo.ErroDeCampo;
import com.autobots.automanager.modelo.ResponsePadrao;
import com.autobots.automanager.enumeracao.TipoDocumento;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ManipuladorGlobalExcecoes {

    private static final Logger log = LoggerFactory.getLogger(ManipuladorGlobalExcecoes.class);

    // ─── 400 — PathVariable não conversível (ex: /telefones/abc) ────────────

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST, "O ID informado é inválido.");
    }

    // ─── 400 — body ausente, JSON malformado ou valor de tipo inválido ───────

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Required request body")) {
            return construirResposta(HttpStatus.BAD_REQUEST, "Corpo da requisição é obrigatório.");
        }
        Throwable causa = ex.getCause();
        if (causa instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();
            if (targetType != null) {
                if (TipoDocumento.class.equals(targetType)) {
                    return construirResposta(HttpStatus.BAD_REQUEST,
                            "Tipo de documento inválido. Valores aceitos: CPF, CNPJ, RG, CNH, PASSAPORTE.");
                }
                if (targetType.isEnum()) {
                    return construirResposta(HttpStatus.BAD_REQUEST, "Tipo inválido para o campo informado.");
                }
                if (LocalDate.class.isAssignableFrom(targetType) || LocalDateTime.class.isAssignableFrom(targetType)) {
                    return construirResposta(HttpStatus.BAD_REQUEST, "Data de emissão inválida.");
                }
            }
        }
        return construirResposta(HttpStatus.BAD_REQUEST, "JSON inválido ou mal formatado.");
    }

    // ─── 405 — Método HTTP não suportado para a rota ─────────────────────────

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        return construirResposta(HttpStatus.METHOD_NOT_ALLOWED,
                "Método HTTP '" + ex.getMethod() + "' não é suportado para este endpoint.");
    }

    // ─── 415 — Content-Type inválido ─────────────────────────────────────────

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return construirResposta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type deve ser application/json.");
    }

    // ─── 400 — @Validated em PathVariable / @RequestParam ────────────────────

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        String mensagem = ex.getConstraintViolations().stream()
                .map(v -> v.getMessage())
                .collect(Collectors.joining("; "));
        return construirResposta(HttpStatus.BAD_REQUEST, mensagem.isBlank() ? "Parâmetros inválidos." : mensagem);
    }

    // ─── 400 — payload inválido ───────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleValidacaoBean(MethodArgumentNotValidException ex) {
        List<String> mensagens = new ArrayList<>();
        List<ErroDeCampo> errosCampo = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> {
            errosCampo.add(new ErroDeCampo(e.getField(), e.getDefaultMessage()));
            mensagens.add(e.getField() + ": " + e.getDefaultMessage());
        });

        // Constraints de classe (@CredencialValida etc) — sem campo associado.
        ex.getBindingResult().getGlobalErrors().forEach(e -> {
            errosCampo.add(new ErroDeCampo(e.getObjectName(), e.getDefaultMessage()));
            mensagens.add(e.getDefaultMessage());
        });

        String mensagem = mensagens.isEmpty() ? "Dados inválidos." : String.join("; ", mensagens);
        return construirRespostaComErros(HttpStatus.BAD_REQUEST, mensagem, errosCampo);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleValidation(ValidationException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ─── 404 — recurso não encontrado ────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(TelefoneNaoEncontradoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleTelefoneNaoEncontrado(TelefoneNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmailNaoEncontradoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEmailNaoEncontrado(EmailNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DocumentoNaoEncontradoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleDocumentoNaoEncontrado(DocumentoNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CredencialNaoEncontradaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleCredencialNaoEncontrada(CredencialNaoEncontradaException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EmpresaNaoEncontradaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEmpresaNaoEncontrada(EmpresaNaoEncontradaException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ServicoNaoEncontradoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleServicoNaoEncontrado(ServicoNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MercadoriaNaoEncontradaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleMercadoriaNaoEncontrada(MercadoriaNaoEncontradaException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAssociadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleUsuarioNaoAssociado(UsuarioNaoAssociadoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ─── 409 — conflito de unicidade ─────────────────────────────────────────

    @ExceptionHandler(RecursoJaVinculadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleRecursoJaVinculado(RecursoJaVinculadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(TelefoneDuplicadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleTelefoneDuplicado(TelefoneDuplicadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DocumentoDuplicadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleDocumentoDuplicado(DocumentoDuplicadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEmailDuplicado(EmailDuplicadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CredencialDuplicadaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleCredencialDuplicada(CredencialDuplicadaException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UsuarioJaAssociadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleUsuarioJaAssociado(UsuarioJaAssociadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(TelefoneAssociadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleTelefoneAssociado(TelefoneAssociadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmailAssociadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEmailAssociado(EmailAssociadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DocumentoAssociadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleDocumentoAssociado(DocumentoAssociadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VeiculoBloqueadoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleVeiculoBloqueado(VeiculoBloqueadoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EmpresaComVendasException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEmpresaComVendas(EmpresaComVendasException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MercadoriaEmUsoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleMercadoriaEmUso(MercadoriaEmUsoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CredencialAssociadaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleCredencialAssociada(CredencialAssociadaException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UsuarioComVendasException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleUsuarioComVendas(UsuarioComVendasException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ServicoEmUsoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleServicoEmUso(ServicoEmUsoException ex) {
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Fallback para violações de constraint do banco não previstas pelo service.
    // Tenta inferir o campo pelo nome da constraint; nunca expõe detalhes técnicos.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleIntegridade(DataIntegrityViolationException ex) {
        String causa = ex.getMostSpecificCause().getMessage();
        log.warn("Violação de integridade no banco: {}", causa);
        String mensagem = inferirMensagemConflito(causa != null ? causa.toLowerCase() : "");
        return construirResposta(HttpStatus.CONFLICT, mensagem);
    }

    // ─── 400 — regras de negócio específicas ─────────────────────────────────

    @ExceptionHandler(EstoqueNegativoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleEstoqueNegativo(EstoqueNegativoException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ServicoInativoException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleServicoInativo(ServicoInativoException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ─── 422 — regra de negócio da venda ─────────────────────────────────────

    @ExceptionHandler(VendaNaoValidaException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleVendaNaoValida(VendaNaoValidaException ex) {
        HttpStatus status = ex.getErros().isEmpty()
                ? HttpStatus.UNPROCESSABLE_ENTITY
                : HttpStatus.BAD_REQUEST;
        return construirRespostaComErros(status, ex.getMessage(), ex.getErros());
    }

    // ─── 409 — conflito de escrita concorrente (otimistic locking) ──────────

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleOptimisticLock(OptimisticLockingFailureException ex) {
        log.warn("Conflito de concorrência: {}", ex.getMessage());
        return construirResposta(HttpStatus.CONFLICT,
                "O registro foi modificado por outra operação simultânea. Tente novamente.");
    }

    // ─── 422 — operação não suportada por regra de negócio ───────────────────

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleUnsupported(UnsupportedOperationException ex) {
        return construirResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    // ─── 401 — credenciais inválidas no login ────────────────────────────────

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleBadCredentials(BadCredentialsException ex) {
        return construirResposta(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponsePadrao<Object>> handleAuthentication(AuthenticationException ex) {
        return construirResposta(HttpStatus.UNAUTHORIZED, "Falha na autenticação.");
    }

    // ─── 500 — erro interno: log completo, resposta limpa ────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponsePadrao<Object>> handleGenerico(Exception ex) {
        log.error("Erro interno não tratado [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return construirResposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno. Tente novamente ou contate o suporte.");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private String inferirMensagemConflito(String causaLowercase) {
        if (causaLowercase.contains("uk_email") || causaLowercase.contains("email_endereco"))
            return "E-mail já cadastrado no sistema.";
        if (causaLowercase.contains("uk_documento") || causaLowercase.contains("documento_numero"))
            return "Documento já cadastrado no sistema.";
        if (causaLowercase.contains("nome_usuario") || causaLowercase.contains("nomeusuario"))
            return "Nome de usuário já utilizado no sistema.";
        if (causaLowercase.contains("uk_credencial_codigo") || causaLowercase.contains("codigo_barra"))
            return "Código de credencial já cadastrado no sistema.";
        if (causaLowercase.contains("usuario_telefone") || causaLowercase.contains("usuario_telefones"))
            return "Não é possível remover o telefone pois ele está associado a um usuário ou empresa.";
        if (causaLowercase.contains("usuario_email") || causaLowercase.contains("usuario_emails"))
            return "Não é possível remover o email pois ele está associado a um usuário.";
        if (causaLowercase.contains("uk_documento_tipo_numero") || causaLowercase.contains("usuario_documento") || causaLowercase.contains("usuario_documentos"))
            return "Não é possível remover o documento pois ele está associado a um usuário.";
        return "Conflito de dados: o valor informado já existe no sistema.";
    }

    private ResponseEntity<ResponsePadrao<Object>> construirResposta(HttpStatus status, String mensagem) {
        ResponsePadrao<Object> resposta = new ResponsePadrao<>();
        resposta.setStatus(status.value());
        resposta.setMensagem(mensagem);
        resposta.setTimestamp(Instant.now().toString());
        return ResponseEntity.status(status).body(resposta);
    }

    private ResponseEntity<ResponsePadrao<Object>> construirRespostaComErros(
            HttpStatus status, String mensagem, List<ErroDeCampo> erros) {
        ResponsePadrao<Object> resposta = new ResponsePadrao<>();
        resposta.setStatus(status.value());
        resposta.setMensagem(mensagem);
        resposta.setTimestamp(Instant.now().toString());
        if (erros != null && !erros.isEmpty()) {
            resposta.setErros(erros);
        }
        return ResponseEntity.status(status).body(resposta);
    }
}
