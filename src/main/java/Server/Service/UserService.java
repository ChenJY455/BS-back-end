package Server.Service;

import Server.Entities.JDGoods;
import Server.Entities.Likes;
import Server.Entities.TBGoods;
import Server.Entities.User;
import Server.Exception.UnAuthedException;
import Server.Repository.JDRepository;
import Server.Repository.LikesRepsitory;
import Server.Repository.UserRepository;
import Server.Utils.Utils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LikesRepsitory likesRepsitory;
	@Autowired
	private JDRepository jdRepository;
	
	public void LoginService(Map<String, String> params) {
		String username = params.get("username");
		String password = params.get("password");
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UnAuthedException("用户不存在"));
		if(!BCrypt.checkpw(password, user.getPassword()))
			throw new UnAuthedException("用户名或密码错误");
	}
	
	public void RegisterService(Map<String, Object> body) {
		String username = body.get("username").toString();
		String password = body.get("password").toString();
		String email = body.get("email").toString();
		if(username.length() < 6 || password.length() < 6)
			throw new UnAuthedException("用户名或密码不符合要求");
		userRepository.save(new User(
				username,
				BCrypt.hashpw(password, BCrypt.gensalt()),
				email
		));
	}
	
	public void AddLikesService(Map<String, Object> body) {
		long uid = Long.parseLong(body.get("uid").toString());
		long gid = Long.parseLong(body.get("gid").toString());
		Utils.WebsiteType website = Utils.WebsiteType.fromString(body.get("website").toString());
		switch (website) {
			case JD -> {
				String name = jdRepository.findById(uid).get().getName();
				likesRepsitory.save(new Likes(
					new User(uid),
					Utils.WebsiteType.JD,
					name,
					new JDGoods(gid),
					null
				));
			}
			case TB -> {
				likesRepsitory.save(new Likes(
					new User(uid),
					Utils.WebsiteType.TB,
					"",
					null,
					new TBGoods(gid)
				));
			}
		}
	}
}
