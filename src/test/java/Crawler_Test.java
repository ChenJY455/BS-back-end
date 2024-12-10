import Server.Crawler.TBCrawler;
import Server.Crawler.JDCrawler;
import Server.Entities.JDGoods;
import Server.Entities.TBGoods;

import java.util.List;

public class Crawler_Test {
	public static void main(String[] args) {
		System.out.println("JD Test: ");
		JDTest();
		System.out.println();
		System.out.println("TB Test: ");
		TBTest();
	}
	private static void JDTest() {
		try {
			JDCrawler jdCrawler = new JDCrawler();
			List<JDGoods> goodsArray = jdCrawler.GetGoodsList("冰箱", 10);
			for(JDGoods goods : goodsArray) {
				System.out.println(goods.getGid() + " " +
						goods.getName() + " " +
						goods.getPrice() + " " +
						goods.getFactory() + " " +
						goods.getClick_url());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void TBTest() {
		try {
			TBCrawler tbCrawler = new TBCrawler();
			List<TBGoods> goodsArray = tbCrawler.GetGoodsList("粮油米面", 10);
			for(TBGoods goods : goodsArray) {
				System.out.println(goods.getGid() + " " +
						goods.getName() + " " +
						goods.getPrice() + " " +
						goods.getFactory() + " " +
						goods.getClick_url());
			}
			double price = tbCrawler.GetPrice(839801567320L);
			System.out.println(price);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
