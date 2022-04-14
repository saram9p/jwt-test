package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;

import lombok.RequiredArgsConstructor;

@Configuration // ioc 할 수 있게 만듬
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
	http.csrf().disable();
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다는 것(stateless 서버로 만듬)
	.and()
	.addFilter(corsFilter) // 모든 요청은 corsFilter를 탄다, @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
	.formLogin().disable()
	.httpBasic().disable()
	.addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManger
	.authorizeRequests()
	.antMatchers("api/v1/user/**")
	.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	.antMatchers("api/v1/manager/**")
	.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	.antMatchers("api/v1/admin/**")
	.access("hasRole('ROLE_ADMIN')")
	.anyRequest().permitAll(); // 다른 요청은 전부 권한없이 들어갈 수 있다.
}
}
