package Server.Repository;

import Server.Entities.Goods;
import Server.Entities.JDGoods;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JDRepository extends JpaRepository<JDGoods, Long> {
	List<Goods> findByKeywordContaining(String keyword, Pageable pageable);
}