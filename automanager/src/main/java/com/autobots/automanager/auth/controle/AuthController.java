package com.autobots.automanager.auth.controle;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.auth.dto.LoginCodigoBarraRequest;
import com.autobots.automanager.auth.dto.LoginRequest;
import com.autobots.automanager.auth.dto.LoginResponse;
import com.autobots.automanager.seguranca.adaptadores.UserDetailsCodigoBarraImpl;
import com.autobots.automanager.seguranca.adaptadores.UserDetailsImpl;
import com.autobots.automanager.seguranca.adaptadores.UserDetailsServiceCodigoBarraImpl;
import com.autobots.automanager.seguranca.jwt.JwtService;
import com.autobots.automanager.servico.CredencialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceCodigoBarraImpl userDetailsServiceCodigoBarra;
    private final CredencialService credencialService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsServiceCodigoBarraImpl userDetailsServiceCodigoBarra,
                          CredencialService credencialService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsServiceCodigoBarra = userDetailsServiceCodigoBarra;
        this.credencialService = credencialService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.nomeUsuario(), request.senha()));

        UserDetailsImpl userDetails = (UserDetailsImpl) autenticacao.getPrincipal();
        String token = jwtService.gerarToken(userDetails);
        credencialService.registrarAcesso(userDetails.getCredencialId());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/login/codigo-barra")
    public ResponseEntity<LoginResponse> loginCodigoBarra(
            @RequestBody @Valid LoginCodigoBarraRequest request) {
        UserDetailsCodigoBarraImpl userDetails = (UserDetailsCodigoBarraImpl) userDetailsServiceCodigoBarra.loadUserByCodigo(request.codigo());
        if (!userDetails.isEnabled()) {
            throw new BadCredentialsException("Credencial de código de barras inativa");
        }
        String token = jwtService.gerarToken(userDetails);
        credencialService.registrarAcesso(userDetails.getCredencialId());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
