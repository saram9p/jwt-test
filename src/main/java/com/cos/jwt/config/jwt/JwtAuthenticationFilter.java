package com.cos.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작을 함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		
		// 1. username, password를 받아서
		try {
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			ObjectMapper om = new ObjectMapper(); // 이 클래스는 json 데이터를 파싱해준다.
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("===============================");
		
		// 2. 정상인지 로그인 시도를 해보는 것, authenticationManager로 로그인을 시도하면!
		// PrincipalDetailsService가 호출, loadUserByUsername() 함수 실행됨.
		
		// 3. PrincipalDetails를 세션에 담고 (이걸 세션에 안담으면 권한관리가 안된다, 세션에 값이 있어야 시큐리티가 권한관리를 해준다.)
		
		// 4. JWT토큰을 만들어서 응답해주면 됨.
		
		return super.attemptAuthentication(request, response);
	}
}
