package Server.Repository;

import Server.Entities.Goods;
import Server.Entities.TBGoods;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TBRepository extends CrudRepository<TBGoods, Integer> {
	List<Goods> findAllByKeyword(String keyword);
}