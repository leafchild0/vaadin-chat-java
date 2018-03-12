package com.leafchild0.chat

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.vaadin.spring.events.annotation.EnableEventBus

/**
 * @author victor
 * @date 3/9/18
 */
@Configuration
@EnableWebSecurity
@EnableEventBus
open class SecurityConfiguration : WebSecurityConfigurerAdapter() {

	@Throws(Exception::class)
	override fun configure(http: HttpSecurity) {
		http
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
			.authorizeRequests()
			.antMatchers("/VAADIN/**", "/vaadinServlet/**").permitAll()
			.antMatchers("/login*", "/styles*").anonymous()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login.html")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.failureUrl("/login.html?error=true")
			.and()
			.logout().logoutSuccessUrl("/login.html")
	}

	@Bean
	public override fun userDetailsService(): UserDetailsService {
		// User in memory auth with default encoder
		// Not recommended for prod use
		val manager = InMemoryUserDetailsManager()
		manager.createUser(User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build())
		manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build())
		return manager
	}
}
