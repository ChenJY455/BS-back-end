package Server.Repository;

import Server.Entities.History;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
	List<History> findAllByJdGid(long gid, Sort sort);
	List<History> findAllByTbGid(long gid, Sort sort);
}
