package photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import photoapp.api.users.shared.UserDto;

public interface UserService extends UserDetailsService{

	UserDto createUser(UserDto userDto);
	
	UserDto getUserByEmail(String email);

	UserDto getUserByUserId(String userId);
	
}
