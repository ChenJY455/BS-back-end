package Server.Repository;

import Server.Entities.JDGoods;
import Server.Entities.Likes;
import Server.Entities.TBGoods;
import Server.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikesRepsitory extends JpaRepository<Likes, Long> {
	List<Likes> findAllByUser(User user);
	List<Likes> findAllByTbGoods(TBGoods tbGoods);
	List<Likes> findAllByJdGoods(JDGoods jdGoods);
	
	void deleteByUserAndTbGoods(User user, TBGoods tbGoods);
	void deleteByUserAndJdGoods(User user, JDGoods jdGoods);
	
}
