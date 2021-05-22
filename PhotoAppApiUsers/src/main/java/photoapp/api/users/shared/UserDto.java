package photoapp.api.users.shared;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import photoapp.api.users.ui.model.AlbumResponseModel;

@Data
public class UserDto implements Serializable{

	
	private static final long serialVersionUID = -5080566695315505590L;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String userId;
	private String encryptedPassword;
	private List<AlbumResponseModel>albums;
	
}
