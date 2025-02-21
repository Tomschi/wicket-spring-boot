package com.giffing.wicket.spring.boot.example.web.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.giffing.wicket.spring.boot.starter.web.servlet.websocket.WicketSessionResolver;

/**
 * Default Spring Boot Wicket security getting started configuration. Its only
 * active if there is not other {@link WebSecurityConfigurerAdapter} present.
 * 
 * Holds hard coded users which should only be used to get started
 * 
 * @author Marc Giffing
 *
 */
@Configuration
@EnableWebSecurity
public class WicketWebSecurityAdapterConfig extends WebSecurityConfigurerAdapter  {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests().antMatchers("/**").permitAll()
			.and().logout().permitAll();
		http.headers().frameOptions().disable();
	}
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
	}
	
	@Bean( name="authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	//TODO Add Wicket Issue - problem with semicolon in wicket websocket url. Allow semicolon.
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall fw = new StrictHttpFirewall();
		fw.setAllowSemicolon(true);
	    return fw;
	}

	@Bean
	public UserDetailsService userDetailsService() {
	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	    manager.createUser(
	    		User.withUsername("admin")
	    		 .password(passwordEncoder().encode("admin"))
	    		 .authorities("USER", "ADMIN")
	    		 .build());
	    manager.createUser(
	    		User.withUsername("customer")
	    		 .password(passwordEncoder().encode("customer"))
	    		 .authorities("USER", "ADMIN")
	    		 .build());
	    return manager;
	}
	
	//@Bean
	//public WicketSessionResolver springSecurityWicketSessionResolver() {
	//	return new SpringSecurityWicketSessionResolver();
	//}

}
