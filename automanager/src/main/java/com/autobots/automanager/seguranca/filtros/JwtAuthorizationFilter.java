package com.autobots.automanager.seguranca.filtros;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.autobots.automanager.seguranca.adaptadores.UserDetailsServiceCodigoBarraImpl;
import com.autobots.automanager.seguranca.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.seguranca.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserDetailsServiceCodigoBarraImpl userDetailsServiceCodigoBarra;

    public JwtAuthorizationFilter(JwtService jwtService,
                                  UserDetailsServiceImpl userDetailsService,
                                  UserDetailsServiceCodigoBarraImpl userDetailsServiceCodigoBarra) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userDetailsServiceCodigoBarra = userDetailsServiceCodigoBarra;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String cabecalho = request.getHeader("Authorization");

        if (cabecalho == null || !cabecalho.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = cabecalho.substring(7);

        try {
            String nomeUsuario = jwtService.extrairNomeUsuario(jwt);

            if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = nomeUsuario.startsWith("CB:")
                        ? userDetailsServiceCodigoBarra.loadUserByCodigo(Long.parseLong(nomeUsuario.substring(3)))
                        : userDetailsService.loadUserByUsername(nomeUsuario);

                if (jwtService.validarToken(jwt, userDetails) && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken autenticacao =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    autenticacao.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(autenticacao);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            log.warn("JWT expirado: {}", ex.getMessage());
        } catch (io.jsonwebtoken.JwtException ex) {
            log.warn("JWT inválido: {}", ex.getMessage());
        } catch (Exception ex) {
            log.warn("Falha ao processar JWT [{}]: {}", ex.getClass().getSimpleName(), ex.getMessage());
        }

        chain.doFilter(request, response);
    }
}
