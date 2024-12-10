package Server.Repository;

import Server.Entities.Likes;
import Server.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepsitory extends JpaRepository<Likes, Long> {
	List<Likes> findAllByUser(User user);
}
