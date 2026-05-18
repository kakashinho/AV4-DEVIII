package com.autobots.automanager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.autobots.automanager.entidade.*;
import com.autobots.automanager.enumeracao.*;
import com.autobots.automanager.repositorio.*;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired
	private RepositorioEmpresa repositorioEmpresa;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Car service toyota ltda");
		empresa.setNomeFantasia("Car service manutenção veicular");
		empresa.setCadastro(LocalDateTime.now());

		Endereco enderecoEmpresa = new Endereco();
		enderecoEmpresa.setEstado("São Paulo");
		enderecoEmpresa.setCidade("São Paulo");
		enderecoEmpresa.setBairro("Centro");
		enderecoEmpresa.setRua("Av. São João");
		enderecoEmpresa.setNumero("00");
		enderecoEmpresa.setCodigoPostal("01035-000");
		empresa.setEndereco(enderecoEmpresa);

		Telefone telefoneEmpresa = new Telefone();
		telefoneEmpresa.setDdd("011");
		telefoneEmpresa.setNumero("986454527");
		empresa.getTelefones().add(telefoneEmpresa);

		Usuario funcionario = new Usuario();
		funcionario.setNome("Pedro Alcântara");
		funcionario.setNomeSocial("Dom Pedro");
		funcionario.getPerfis().add(PerfilUsuario.FUNCIONARIO);

		Email emailFuncionario = new Email();
		emailFuncionario.setEndereco("a@a.com");
		funcionario.getEmails().add(emailFuncionario);
		funcionario.setEmpresa(empresa);
		empresa.getUsuarios().add(funcionario);

		Usuario fornecedor = new Usuario();
		fornecedor.setNome("Fornecedor Auto");
		fornecedor.setNomeSocial("Loja do carro");
		fornecedor.getPerfis().add(PerfilUsuario.FORNECEDOR);

		Email emailFornecedor = new Email();
		emailFornecedor.setEndereco("f@f.com");
		fornecedor.getEmails().add(emailFornecedor);
		fornecedor.setEmpresa(empresa);
		empresa.getUsuarios().add(fornecedor);

		Mercadoria roda1 = new Mercadoria();
		roda1.setCadastro(LocalDateTime.now());
		roda1.setFabricacao(LocalDate.now());
		roda1.setNome("Roda Toyota");
		roda1.setValidade(LocalDate.now().plusYears(2));
		roda1.setQuantidade(30);
		roda1.setValor(new BigDecimal("300.00"));
		roda1.setDescricao("Original");
		roda1.setDisponivel(true);

		Mercadoria roda2 = new Mercadoria();
		roda2.setCadastro(LocalDateTime.now());
		roda2.setFabricacao(LocalDate.now());
		roda2.setNome("Roda genérica");
		roda2.setValidade(LocalDate.now().plusYears(2));
		roda2.setQuantidade(30);
		roda2.setValor(new BigDecimal("150.00"));
		roda2.setDescricao("Segunda linha");
		roda2.setDisponivel(true);

		empresa.getMercadorias().add(roda1);
		empresa.getMercadorias().add(roda2);
		fornecedor.getMercadorias().add(roda1);

		Usuario cliente = new Usuario();
		cliente.setNome("Cliente");
		cliente.getPerfis().add(PerfilUsuario.CLIENTE);

		Email emailCliente = new Email();
		emailCliente.setEndereco("cliente@exemplo.com");
		cliente.getEmails().add(emailCliente);
		cliente.setEmpresa(empresa);
		empresa.getUsuarios().add(cliente);

		Veiculo veiculo = new Veiculo();
		veiculo.setPlaca("ABC-0000");
		veiculo.setModelo("Corolla Cross");
		veiculo.setTipo(TipoVeiculo.SUV);
		veiculo.setProprietario(cliente);
		cliente.getVeiculos().add(veiculo);

		Servico s1 = new Servico();
		s1.setNome("Troca de rodas");
		s1.setValor(new BigDecimal("50.00"));
		s1.setEmpresa(empresa);

		Servico s2 = new Servico();
		s2.setNome("Alinhamento");
		s2.setValor(new BigDecimal("50.00"));
		s2.setEmpresa(empresa);

		empresa.getServicos().add(s1);
		empresa.getServicos().add(s2);

		Venda v1 = new Venda();
		v1.setCadastro(LocalDateTime.now());
		v1.setCliente(cliente);
		v1.setFuncionario(funcionario);
		v1.setIdentificacao("123");
		v1.setVeiculo(veiculo);
		v1.getMercadorias().add(roda1);
		v1.getServicos().add(s1);
		v1.getServicos().add(s2);
		empresa.getVendas().add(v1);
		veiculo.getVendas().add(v1);

		Servico s3 = new Servico();
		s3.setNome("Balanceamento");
		s3.setValor(new BigDecimal("30.00"));
		s3.setEmpresa(empresa);

		Venda v2 = new Venda();
		v2.setCadastro(LocalDateTime.now());
		v2.setCliente(cliente);
		v2.setFuncionario(funcionario);
		v2.setIdentificacao("456");
		v2.setVeiculo(veiculo);
		v2.getMercadorias().add(roda2);
		v2.getServicos().add(s2);
		v2.getServicos().add(s3);
		empresa.getVendas().add(v2);
		veiculo.getVendas().add(v2);

		repositorioEmpresa.save(empresa);
	}
}
