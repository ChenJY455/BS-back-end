package Server.Crawler;

import Server.Entities.Goods;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Crawler {
	public List<? extends Goods> GetGoodsList(String keyword, int page) throws NoSuchAlgorithmException, IOException, InterruptedException;
}
