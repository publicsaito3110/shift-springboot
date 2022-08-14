package com.shift.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author saito
 *
 */
@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.formLogin(login -> login

				//ログイン画面のURL
				.loginProcessingUrl("/login")
				.loginPage("/login")
				//ログイン成功時のURL
				.defaultSuccessUrl("/login/auth")
				//ログイン失敗時のURL
				.failureUrl("/login?error")
				.permitAll()

				).logout(logout -> logout
						//ログアウト時のURL
						.logoutSuccessUrl("/logout")

						).authorizeHttpRequests(authz -> authz
								.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

								//View(static)の許容するパス
								.mvcMatchers("/css/**").permitAll()
								.mvcMatchers("/js/**").permitAll()
								.mvcMatchers("/png/**").permitAll()
								);
		return httpSecurity.build();
	}
}
