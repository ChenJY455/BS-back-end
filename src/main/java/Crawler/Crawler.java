package Crawler;

import Goods.Goods;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Crawler {
	List<Goods> GetGoodsList(String keyword)
			throws NoSuchAlgorithmException, IOException, InterruptedException;
}
