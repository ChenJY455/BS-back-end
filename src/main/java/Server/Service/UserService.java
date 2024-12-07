package Server.Service;

import Server.Entities.User;
import Server.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public void LoginService(Map<String, String> params) {
		String username = params.get("username");
		String password = params.get("password");
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("用户不存在"));
		if(!BCrypt.checkpw(password, user.getPassword()))
			throw new IllegalArgumentException("用户名或密码错误");
	}
	
	public void RegisterService(Map<String, Object> body) {
		String username = body.get("username").toString();
		String password = body.get("password").toString();
		String email = body.get("email").toString();
		if(username.length() < 6 || password.length() < 6)
			throw new IllegalArgumentException("用户名或密码不符合要求");
		userRepository.save(new User(
				username,
				BCrypt.hashpw(password, BCrypt.gensalt()),
				email
		));
	}
}
