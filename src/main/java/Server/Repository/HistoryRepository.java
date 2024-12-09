package Server.Repository;

import Server.Entities.History;
import Server.Entities.JDGoods;
import Server.Entities.TBGoods;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
	List<History> findAllByJdGids(JDGoods jdGood);
	List<History> findAllByTbGids(TBGoods tbGood);
}
