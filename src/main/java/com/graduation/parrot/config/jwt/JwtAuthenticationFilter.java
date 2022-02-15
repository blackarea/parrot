package com.graduation.parrot.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.graduation.parrot.config.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	
	// Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
	// 인증 요청시에 실행되는 함수 => /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 진입");

		try {
			/*BufferedReader br = request.getReader();
			String input = null;
			while((input = br.readLine()) != null){
				System.out.println(input);
			}*/

			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getLogin_id(), user.getPassword());
			System.out.println("1========================");
			//PrincipalDetailsService.loadUserByUsername 실행
			//authentication은 session에 저장됨
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			System.out.println("2========================");
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getLogin_id());
			System.out.println("3========================");

			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*// JWT Token 생성해서 response에 담아주기
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject(principalDetailis.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetailis.getUser().getId())
				.withClaim("username", principalDetailis.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}*/
	
}
