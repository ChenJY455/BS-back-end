import Server.Crawler.Crawler;
import Server.Crawler.TBCrawler;
import Server.Crawler.JDCrawler;
import Server.Entities.Goods;

import java.util.List;

public class Crawler_Test {
	public static void main(String[] args) {
//		System.out.println("TB Test: ");
//		Test(new TBCrawler());
//		System.out.println();
		System.out.println("JD Test: ");
		Test(new JDCrawler());
	}
	private static void Test(Crawler crawler) {
		try {
			for(int i = 0; i < 3; i++) {
				List<? extends Goods> goodsArray = crawler.GetGoodsList("冰箱");
				if(goodsArray == null)
					continue;
				break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
