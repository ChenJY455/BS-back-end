package Server.Controller;

import Server.Entities.User;
import Server.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public ResponseEntity<User> HandleLogin(@RequestParam Map<String, String> params) {
		User user = userService.LoginService(params);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/register")
	public ResponseEntity<User> HandleRegister(@RequestBody Map<String, Object> body) {
		User user = userService.RegisterService(body);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping("/add-likes")
	public ResponseEntity<String> HandleAddLikes(@RequestBody Map<String, Object> body) {
		userService.AddLikesService(body);
		return ResponseEntity.ok("Success");
	}
}
