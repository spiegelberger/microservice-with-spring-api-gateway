package photoapp.api.users.security;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import photoapp.api.users.service.UserService;
import photoapp.api.users.shared.UserDto;
import photoapp.api.users.ui.model.LoginRequestModel;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private UserService userService;
	private Environment env;
	AuthenticationManager authenticationManager;
	
	
	@Autowired
	public AuthenticationFilter(UserService userService, Environment env,
								AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.env = env;
		super.setAuthenticationManager(authenticationManager);
	}


	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
							throws AuthenticationException {
		
		try {
			LoginRequestModel creds = new ObjectMapper()
					.readValue(req.getInputStream(), LoginRequestModel.class);
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(),
							creds.getPassword(),
							new ArrayList<>()));
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
	}	
	
	
	@Override
    protected void successfulAuthentication(
		    			HttpServletRequest req, HttpServletResponse res,
		    			FilterChain chain, Authentication auth) 
		            	throws IOException, ServletException {
		
		// We use email as username
		String userName = ((User)auth.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserByEmail(userName);
		
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + 
						Long.parseLong(env.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
				.compact();
		
		// return the token and public userId in the response header
		res.addHeader("token", token);
		res.addHeader("userId", userDetails.getUserId());
	}
		
	
}
