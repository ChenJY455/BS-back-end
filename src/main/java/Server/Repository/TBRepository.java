package Server.Repository;

import Server.Entities.TBGoods;
import org.springframework.data.repository.CrudRepository;

public interface TBRepository extends CrudRepository<TBGoods, Integer> {}