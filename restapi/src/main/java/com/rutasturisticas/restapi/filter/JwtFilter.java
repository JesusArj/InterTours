package com.rutasturisticas.restapi.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rutasturisticas.restapi.data.repository.UsuarioRepository;
import com.rutasturisticas.restapi.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getCookies() == null) {
			filterChain.doFilter(request, response);
			return;
		}

		// Get authorization header and validate
		Optional<Cookie> jwtOpt = Arrays.stream(request.getCookies()).filter(cookie -> "jwt".equals(cookie.getName()))
				.findAny();

		if (jwtOpt.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtOpt.get().getValue();

		UserDetails userDetails = null;

		try {
			userDetails = usuarioRepository.findByUsername(jwtUtil.getUsernameFromToken(token)).orElse(null);
		} catch (ExpiredJwtException | SignatureException e) {
			filterChain.doFilter(request, response);
			return;
		}

		// Get jwt token and validate
		if (!jwtUtil.validateToken(token, userDetails)) {
			filterChain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails == null ? List.of() : userDetails.getAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		// this is where the authentication magic happens and the user is now valid!
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);

	}
}
