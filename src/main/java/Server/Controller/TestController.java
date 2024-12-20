package Server.Controller;

import Server.Service.EmailService;
import Server.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/test")
public class TestController {
	@Autowired
	EmailService emailService;
	@Autowired
	TestService testService;
	
	@GetMapping("/email")
	public ResponseEntity<String> HandleEmail() {
		emailService.sendEmail("3220105455@zju.edu.cn", "测试邮件", "内容");
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/change-price")
	public ResponseEntity<String> HandleChangePrice(@RequestBody Map<String, Object> body) {
		testService.changePriceService(body);
		return ResponseEntity.ok().build();
	}
}