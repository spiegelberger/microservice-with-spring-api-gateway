package photoapp.api.users.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private Environment environment;

	@GetMapping("/status/check")
	public String status() {
		return "UsersController is working on port " + environment.getProperty("local.server.port");
	}
}
