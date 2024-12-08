package Server.Repository;

import Server.Entities.Goods;
import Server.Entities.JDGoods;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JDRepository extends CrudRepository<JDGoods, Integer> {
	List<Goods> findAllByKeyword(String keyword);
}