package com.autobots.automanager.servico;

import com.autobots.automanager.dto.requisicao.EmailRequest;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.excecao.EmailAssociadoException;
import com.autobots.automanager.excecao.EmailDuplicadoException;
import com.autobots.automanager.excecao.EmailNaoEncontradoException;
import com.autobots.automanager.excecao.ValidationException;
import com.autobots.automanager.repositorio.RepositorioEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final RepositorioEmail repositorio;

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("O ID deve ser maior que zero.");
        }
    }

    @Override
    @Transactional
    public Email criar(EmailRequest request) {
        String enderecoNorm = request.getEndereco().trim().toLowerCase();
        if (repositorio.existsByEndereco(enderecoNorm)) {
            throw new EmailDuplicadoException(enderecoNorm);
        }
        Email email = new Email();
        email.setEndereco(enderecoNorm);
        return repositorio.save(email);
    }

    @Override
    public Email buscarPorId(Long id) {
        validarId(id);
        return repositorio.findById(id)
                .orElseThrow(() -> new EmailNaoEncontradoException(id));
    }

    @Override
    public List<Email> listarTodos() {
        return repositorio.findAll();
    }

    @Override
    @Transactional
    public Email atualizar(Long id, EmailRequest request) {
        Email email = buscarPorId(id);
        String enderecoNorm = request.getEndereco().trim().toLowerCase();
        if (!email.getEndereco().equals(enderecoNorm) && repositorio.existsByEndereco(enderecoNorm)) {
            throw new EmailDuplicadoException(enderecoNorm);
        }
        email.setEndereco(enderecoNorm);
        return repositorio.save(email);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Email email = buscarPorId(id);
        if (repositorio.existeAssociadoAUsuario(id)) {
            throw new EmailAssociadoException(id);
        }
        repositorio.delete(email);
    }
}
