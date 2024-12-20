package Server.Service;

import Server.Entities.*;
import Server.Exception.UnAuthedException;
import Server.Repository.HistoryRepository;
import Server.Repository.JDRepository;
import Server.Repository.LikesRepsitory;
import Server.Repository.UserRepository;
import Server.Utils.Utils;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LikesRepsitory likesRepsitory;
	@Autowired
	private HistoryRepository historyRepository;
	
	public User LoginService(Map<String, String> params) {
		String username = params.get("username");
		String password = params.get("password");
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UnAuthedException("用户不存在"));
		if(!BCrypt.checkpw(password, user.getPassword()))
			throw new UnAuthedException("用户名或密码错误");
		return user;
	}
	
	public User RegisterService(Map<String, Object> body) {
		String username = body.get("username").toString();
		String password = body.get("password").toString();
		String email = body.get("email").toString();
		if(username.length() < 6 || password.length() < 6)
			throw new UnAuthedException("用户名或密码不符合要求");
		return userRepository.save(new User(
				username,
				BCrypt.hashpw(password, BCrypt.gensalt()),
				email
		));
	}
	
	public List<Likes> GetLikesService(Map<String, String> params) {
		long uid = Long.parseLong(params.get("uid"));
		return likesRepsitory.findAllByUser(new User(uid));
	}
	
	public void AddLikesService(Map<String, Object> body) {
		long uid = Long.parseLong(body.get("uid").toString());
		long gid = Long.parseLong(body.get("gid").toString());
		double price = Double.parseDouble(body.get("price").toString());
		Utils.WebsiteType website = Utils.WebsiteType.fromString(body.get("website").toString());
		String name = body.get("name").toString();
		switch (website) {
			case JD ->  {
				likesRepsitory.save(new Likes(
				new User(uid),
				Utils.WebsiteType.JD,
				name,
				null,
				new JDGoods(gid)
			));
				historyRepository.save(new History(
						0,
						gid,
						price
				));
			}
			case TB -> {
				likesRepsitory.save(new Likes(
				new User(uid),
				Utils.WebsiteType.TB,
				"",
				new TBGoods(gid),
				null
			));
				historyRepository.save(new History(
						gid,
						0,
						price
				));
			}
		}
	}
	
	@Transactional
	public void RemoveLikesService(Map<String, Object> body) {
		long uid = Long.parseLong(body.get("uid").toString());
		long gid = Long.parseLong(body.get("gid").toString());
		Utils.WebsiteType website = Utils.WebsiteType.fromString(body.get("website").toString());
		switch (website) {
			case JD -> likesRepsitory.deleteByUserAndJdGoods(
					new User(uid),
					new JDGoods(gid)
			);
			case TB -> likesRepsitory.deleteByUserAndTbGoods(
					new User(uid),
					new TBGoods(gid)
			);
		}
	}
	
	public User ModifyUserService(Map<String, Object> body) {
		long uid = Long.parseLong(body.get("uid").toString());
		String username = body.get("username").toString();
		String password = body.get("password").toString();
		String email = body.get("email").toString();
		return userRepository.save(new User(uid, username, BCrypt.hashpw(password, BCrypt.gensalt()), email));
	}
}
