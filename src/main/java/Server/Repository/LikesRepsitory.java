package Server.Repository;

import Server.Entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepsitory extends JpaRepository<Likes, Long> {}
