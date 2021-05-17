package photoapp.api.users.shared;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable{

	
	private static final long serialVersionUID = -5080566695315505590L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String userId;
	private String encryptedPassword;
	
}
