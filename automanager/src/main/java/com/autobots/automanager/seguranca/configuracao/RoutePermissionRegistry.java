package com.autobots.automanager.seguranca.configuracao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class RoutePermissionRegistry {

    public record Permissao(HttpMethod metodo, String padrao, String[] perfis) {}

    private final List<Permissao> permissoes = new ArrayList<>();

    public RoutePermissionRegistry() {
        configurar();
    }

    private void configurar() {

        //  USUARIOS
        // ADMIN: CRUD total  |  GERENTE: CRUD gerente/vendedor/cliente
        // VENDEDOR: CRUD apenas de clientes (ownership validado no serviço)
        // CLIENTE: lê apenas o próprio cadastro e sub-recursos (ownership validado no controller)
        // A regra exata "/api/usuarios" deve vir ANTES de "/**" — Spring Security aplica a 1ª que casar.
        
        // --- LEITURA (GET) ---
        registrar(HttpMethod.GET,    "/api/usuarios",                      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/usuarios/*/credenciais",        "ADMIN", "GERENTE"); 
        registrar(HttpMethod.GET,    "/api/usuarios/**",                   "ADMIN", "GERENTE", "VENDEDOR", "CLIENTE");

        // --- CRIAÇÃO E ASSOCIAÇÃO (POST) ---
        registrar(HttpMethod.POST,   "/api/usuarios/*/vendas/*",           "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/usuarios/*/credenciais",        "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/usuarios/**",                   "ADMIN", "GERENTE", "VENDEDOR");

        // --- ATUALIZAÇÃO (PUT) ---
        registrar(HttpMethod.PUT,    "/api/usuarios/*/credenciais/*",      "ADMIN", "GERENTE");
        registrar(HttpMethod.PUT,    "/api/usuarios/**",                   "ADMIN", "GERENTE", "VENDEDOR");

        // --- DELEÇÃO E DESASSOCIAÇÃO (DELETE) ---
        // Vendedor não deleta vendas e não apaga credenciais, mas pode deletar telefones/veículos de clientes
        registrar(HttpMethod.DELETE, "/api/usuarios/*/vendas/*",           "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/usuarios/*/credenciais/*",      "ADMIN", "GERENTE"); 
        registrar(HttpMethod.DELETE, "/api/usuarios/**",                   "ADMIN", "GERENTE", "VENDEDOR");
        //  EMPRESAS
        // ADMIN: CRUD total  |  outros: leitura
        
        // --- LEITURA (SET) ---
        registrar(HttpMethod.GET,    "/api/empresas/*/vendas",             "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/empresas/*/vendas/*",           "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/empresas/**",                   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/empresas",                      "ADMIN", "GERENTE", "VENDEDOR");

        // --- CRIAÇÃO E ASSOCIAÇÃO (POST) ---
        registrar(HttpMethod.POST,   "/api/empresas/*/mercadorias",        "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/empresas/*/mercadorias/*",      "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/empresas/*/servicos",           "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/empresas/*/servicos/*",         "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/empresas/*/usuarios/*",         "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/empresas/*/vendas",             "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/empresas/*/vendas/*",           "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/empresas/*/telefones/*",        "ADMIN");
        registrar(HttpMethod.POST,   "/api/empresas/**",                   "ADMIN");
        registrar(HttpMethod.POST,   "/api/empresas",                      "ADMIN");

        // --- ATUALIZAÇÃO (PUT) ---
        registrar(HttpMethod.PUT,    "/api/empresas/**",                   "ADMIN");

        // --- DELEÇÃO E DESASSOCIAÇÃO (DELETE) ---
        registrar(HttpMethod.DELETE, "/api/empresas/*/mercadorias/*",      "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/empresas/*/servicos/*",         "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/empresas/*/usuarios/*",         "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/empresas/*/vendas/*",           "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/empresas/*/telefones/*",        "ADMIN");
        registrar(HttpMethod.DELETE, "/api/empresas/*/endereco",           "ADMIN");
        registrar(HttpMethod.DELETE, "/api/empresas/**",                   "ADMIN");
        

        //  MERCADORIAS 
        // ADMIN + GERENTE: CRUD  |  VENDEDOR: somente leitura
        registrar(HttpMethod.GET,    "/api/mercadorias/**", "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/mercadorias",    "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/mercadorias/**", "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/mercadorias",    "ADMIN", "GERENTE");
        registrar(HttpMethod.PUT,    "/api/mercadorias/**", "ADMIN", "GERENTE");
        registrar(HttpMethod.PATCH,  "/api/mercadorias/**", "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/mercadorias/**", "ADMIN", "GERENTE");

        //  SERVICOS 
        // ADMIN + GERENTE: CRUD  |  VENDEDOR: somente leitura
        registrar(HttpMethod.GET,    "/api/servicos/**",    "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/servicos",       "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/servicos/**",    "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/servicos",       "ADMIN", "GERENTE");
        registrar(HttpMethod.PUT,    "/api/servicos/**",    "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/servicos/**",    "ADMIN", "GERENTE");

        //  VENDAS
        // ADMIN + GERENTE: CRUD  |  VENDEDOR: criar e ler (filtro de "próprias" fica na camada de serviço)
        // CLIENTE: lê apenas as próprias vendas (filtro aplicado em VendaServiceImpl)
        registrar(HttpMethod.GET,    "/api/vendas",         "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/vendas/**",      "ADMIN", "GERENTE", "VENDEDOR", "CLIENTE");
        registrar(HttpMethod.POST,   "/api/vendas/**",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/vendas",         "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/vendas/**",      "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/vendas/**",      "ADMIN", "GERENTE");

        //  VEICULOS 
        registrar(HttpMethod.GET,    "/api/veiculos/**",    "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/veiculos",       "ADMIN", "GERENTE", "VENDEDOR", "CLIENTE");
        registrar(HttpMethod.POST,   "/api/veiculos/**",    "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/veiculos",       "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/veiculos/**",    "ADMIN", "GERENTE");
        registrar(HttpMethod.DELETE, "/api/veiculos/**",    "ADMIN");

        //  CREDENCIAIS 
        registrar(HttpMethod.GET,    "/api/credenciais/**", "ADMIN");
        registrar(HttpMethod.GET,    "/api/credenciais",    "ADMIN");
        registrar(HttpMethod.POST,   "/api/credenciais/**", "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/credenciais",    "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/credenciais/**", "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.DELETE, "/api/credenciais/**", "ADMIN");

        //  DOCUMENTOS 
        registrar(HttpMethod.GET,    "/api/documentos/**",  "ADMIN", "GERENTE");
        registrar(HttpMethod.GET,    "/api/documentos",     "ADMIN", "GERENTE");
        registrar(HttpMethod.POST,   "/api/documentos/**",  "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/documentos",     "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/documentos/**",  "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.DELETE, "/api/documentos/**",  "ADMIN", "GERENTE");

        //  EMAILS 
        registrar(HttpMethod.GET,    "/api/emails/**",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/emails",         "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/emails/**",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/emails",         "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/emails/**",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.DELETE, "/api/emails/**",      "ADMIN", "GERENTE");

        //  TELEFONES 
        registrar(HttpMethod.GET,    "/api/telefones/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/telefones",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/telefones/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/telefones",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/telefones/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.DELETE, "/api/telefones/**",   "ADMIN", "GERENTE");

        //  ENDERECOS 
        registrar(HttpMethod.GET,    "/api/enderecos/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.GET,    "/api/enderecos",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/enderecos/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.POST,   "/api/enderecos",      "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.PUT,    "/api/enderecos/**",   "ADMIN", "GERENTE", "VENDEDOR");
        registrar(HttpMethod.DELETE, "/api/enderecos/**",   "ADMIN", "GERENTE");
    }

    private void registrar(HttpMethod metodo, String padrao, String... perfis) {
        permissoes.add(new Permissao(metodo, padrao, perfis));
    }

    public List<Permissao> getPermissoes() {
        return Collections.unmodifiableList(permissoes);
    }
}
