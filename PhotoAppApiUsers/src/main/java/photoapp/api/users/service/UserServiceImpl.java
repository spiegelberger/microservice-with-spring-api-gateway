package photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import photoapp.api.users.data.AlbumsServiceClient;
import photoapp.api.users.data.UserEntity;
import photoapp.api.users.data.UsersRepository;
import photoapp.api.users.shared.UserDto;
import photoapp.api.users.ui.model.AlbumResponseModel;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	UsersRepository userRepository;
	BCryptPasswordEncoder encoder;
	Environment env;
	AlbumsServiceClient albumsServiceClient;
	//RestTemplate restTemplate;
	
	@Autowired
	public UserServiceImpl(UsersRepository userRepository, BCryptPasswordEncoder encoder,
							 Environment env, AlbumsServiceClient albumsServiceClient
							 //RestTemplate restTemplate
							 ) {
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.env = env;
		this.albumsServiceClient = albumsServiceClient;
		//this.restTemplate = restTemplate;
	}


	
	@Override
	public UserDto createUser(UserDto userDetails) {
		
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(encoder.encode(userDetails.getPassword()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
				
		userRepository.save(userEntity);
		
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//we use email as username
		UserEntity userEntity = userRepository.findByEmail(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), 
				true, true, true, true, new ArrayList<>());
	}



	@Override
	public UserDto getUserByEmail(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new ModelMapper().map(userEntity, UserDto.class);
	}



	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
			if(userEntity == null) {
				throw new UsernameNotFoundException("User not found");
			}
			
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
		
//	    Use these lines with RestTemplate:	
/*		String albumsUrl = String.format(env.getProperty("albums.url"), userId);
		
		ResponseEntity<List<AlbumResponseModel>>albumsListResponse = restTemplate
				  		  .exchange(albumsUrl, HttpMethod.GET, null, 
						  new ParameterizedTypeReference<List<AlbumResponseModel>>() {					  
				  });
		List<AlbumResponseModel>albumList =albumsListResponse.getBody();
*/

		log.info("BEFORE calling Albums Ms");
//		And this line with Feign Client:
		List<AlbumResponseModel> albumList = albumsServiceClient.getAlbums(userId);
		log.info("AFTER calling Albums Ms");
		
		userDto.setAlbums(albumList);
		
		return userDto;
	}





}
