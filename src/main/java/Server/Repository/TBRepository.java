package Server.Repository;

import Server.Entities.Goods;
import Server.Entities.TBGoods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TBRepository extends JpaRepository<TBGoods, Long> {
	List<Goods> findByKeywordContaining(String keyword);
}