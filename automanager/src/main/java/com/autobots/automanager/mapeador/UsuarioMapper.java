package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.requisicao.UsuarioRequest;
import com.autobots.automanager.dto.resposta.CredencialResponse;
import com.autobots.automanager.dto.resposta.DocumentoResponse;
import com.autobots.automanager.dto.resposta.EmailResponse;
import com.autobots.automanager.dto.resposta.EnderecoResponse;
import com.autobots.automanager.dto.resposta.TelefoneResponse;
import com.autobots.automanager.dto.resposta.UsuarioReferencia;
import com.autobots.automanager.dto.resposta.UsuarioResponse;
import com.autobots.automanager.dto.resposta.UsuarioResumo;
import com.autobots.automanager.entidade.Credencial;
import com.autobots.automanager.entidade.CredencialCodigoBarra;
import com.autobots.automanager.entidade.CredencialUsuarioSenha;
import com.autobots.automanager.entidade.Documento;
import com.autobots.automanager.entidade.Email;
import com.autobots.automanager.entidade.Endereco;
import com.autobots.automanager.entidade.Telefone;
import com.autobots.automanager.entidade.Usuario;
import com.autobots.automanager.excecao.ResourceNotFoundException;
import com.autobots.automanager.repositorio.RepositorioEndereco;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    private final RepositorioEndereco enderecoRepo;

    public UsuarioMapper(RepositorioEndereco enderecoRepo) {
        this.enderecoRepo = enderecoRepo;
    }

    public Usuario paraEntidade(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setNomeSocial(request.getNomeSocial());
        usuario.setPerfis(request.getPerfis());
        if (request.getEnderecoId() != null) {
            Endereco endereco = enderecoRepo.findById(request.getEnderecoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Endereço não encontrado com id: " + request.getEnderecoId()));
            usuario.setEndereco(endereco);
        }
        return usuario;
    }

    public UsuarioResponse paraResponse(Usuario usuario) {
        UsuarioResponse resp = new UsuarioResponse();
        resp.setId(usuario.getId());
        resp.setNome(usuario.getNome());
        resp.setNomeSocial(usuario.getNomeSocial());
        resp.setPerfis(usuario.getPerfis());

        if (usuario.getEndereco() != null)
            resp.setEndereco(paraEnderecoResponse(usuario.getEndereco()));

        resp.setTelefones(usuario.getTelefones().stream()
                .map(this::paraTelefoneResponse).collect(Collectors.toSet()));
        resp.setEmails(usuario.getEmails().stream()
                .map(this::paraEmailResponse).collect(Collectors.toSet()));
        resp.setDocumentos(usuario.getDocumentos().stream()
                .map(this::paraDocumentoResponse).collect(Collectors.toSet()));
        resp.setCredenciais(usuario.getCredenciais().stream()
                .map(this::paraCredencialResponse).collect(Collectors.toSet()));

        return resp;
    }

    public UsuarioResumo paraResumo(Usuario usuario) {
        UsuarioResumo resumo = new UsuarioResumo();
        resumo.setId(usuario.getId());
        resumo.setNome(usuario.getNome());
        resumo.setNomeSocial(usuario.getNomeSocial());
        resumo.setPerfis(usuario.getPerfis());
        return resumo;
    }

    public UsuarioReferencia paraReferencia(Usuario usuario) {
        UsuarioReferencia ref = new UsuarioReferencia();
        ref.setId(usuario.getId());
        ref.setNome(usuario.getNome());
        return ref;
    }

    public EnderecoResponse paraEnderecoResponse(Endereco end) {
        EnderecoResponse er = new EnderecoResponse();
        er.setId(end.getId());
        er.setEstado(end.getEstado());
        er.setCidade(end.getCidade());
        er.setBairro(end.getBairro());
        er.setRua(end.getRua());
        er.setNumero(end.getNumero());
        er.setCodigoPostal(end.getCodigoPostal());
        er.setInformacoesAdicionais(end.getInformacoesAdicionais());
        return er;
    }

    public TelefoneResponse paraTelefoneResponse(Telefone tel) {
        TelefoneResponse tr = new TelefoneResponse();
        tr.setId(tel.getId());
        tr.setDdd(tel.getDdd());
        tr.setNumero(tel.getNumero());
        return tr;
    }

    public EmailResponse paraEmailResponse(Email email) {
        EmailResponse er = new EmailResponse();
        er.setId(email.getId());
        er.setEndereco(email.getEndereco());
        return er;
    }

    public DocumentoResponse paraDocumentoResponse(Documento doc) {
        DocumentoResponse dr = new DocumentoResponse();
        dr.setId(doc.getId());
        dr.setTipo(doc.getTipo());
        dr.setNumero(doc.getNumero());
        dr.setDataEmissao(doc.getDataEmissao());
        return dr;
    }

    public CredencialResponse paraCredencialResponse(Credencial cred) {
        CredencialResponse cr = new CredencialResponse();
        cr.setId(cred.getId());
        cr.setCriacao(cred.getCriacao());
        cr.setUltimoAcesso(cred.getUltimoAcesso());
        cr.setInativo(cred.isInativo());
        if (cred instanceof CredencialUsuarioSenha cus) {
            cr.setTipo("SENHA");
            cr.setNomeUsuario(cus.getNomeUsuario());
        } else if (cred instanceof CredencialCodigoBarra ccb) {
            cr.setTipo("CODIGO");
            cr.setCodigo(ccb.getCodigo());
        }
        return cr;
    }
}
