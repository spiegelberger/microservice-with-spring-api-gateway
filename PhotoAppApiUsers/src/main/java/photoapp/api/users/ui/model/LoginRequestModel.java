package photoapp.api.users.ui.model;

import lombok.Data;

@Data
public class LoginRequestModel {

	private String email;
	private String password;
}
