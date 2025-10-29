package br.com.foodhub.config.security;

import br.com.foodhub.service.auth.CustomUserDetailsService;
import br.com.foodhub.service.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.awt.image.ImageFilter;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String CUSTOMER_PATH = "/api/v1/customers";
    private static final String OWNER_PATH = "/api/v1/owners";
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        log.warn("CHECKING PATH: Method={} URI='{}'", method, path);

        if (method.equals("POST")) {

            boolean isCustomerRegister = pathMatcher.match(CUSTOMER_PATH, path);
            boolean isOwnerRegister = pathMatcher.match(OWNER_PATH, path);

            if (isCustomerRegister || isOwnerRegister) {
                log.warn("Filter SKIPPED for public POST: URI='{}'", path);
                return true;
            }
        }

        log.warn("Filter ACTIVE for protected path: URI='{}'", path);
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            String loginIdentifier = tokenService.getSubject(token);
            if (loginIdentifier != null && !loginIdentifier.isEmpty()) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginIdentifier);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
