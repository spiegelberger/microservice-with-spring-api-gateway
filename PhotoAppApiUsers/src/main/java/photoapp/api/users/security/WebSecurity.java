package photoapp.api.users.security;



import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import photoapp.api.users.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private Environment environment;
	private UserService userService;
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder encoder) {
		this.environment = environment;
		this.userService = userService;
		this.encoder = encoder;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//We use jwt tokens so we can disable csrf:
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/**").hasIpAddress(environment.getProperty("gateway.ip"))
			.and()
			.addFilter(getAuthenticationFilter());
		
		//to see h2 console:
		http.headers().frameOptions().disable();
	}






	private Filter getAuthenticationFilter() throws Exception {
		
		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(encoder);
	}
}
