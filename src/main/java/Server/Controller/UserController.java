package Server.Controller;

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
	public ResponseEntity<String> HandleLogin(@RequestParam Map<String, String> params) {
		userService.LoginService(params);
		return ResponseEntity.ok("Success");
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> HandleRegister(@RequestBody Map<String, Object> body) {
		userService.RegisterService(body);
		return ResponseEntity.ok("Success");
	}
	
	@PostMapping("/add-likes")
	public ResponseEntity<String> HandleAddLikes(@RequestBody Map<String, Object> body) {
		userService.AddLikesService(body);
		return ResponseEntity.ok("Success");
	}
}
