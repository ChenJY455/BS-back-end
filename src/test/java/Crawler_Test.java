import Server.Crawler.Crawler;
import Server.Crawler.TB;
import Server.Crawler.JD;
import Server.Entities.Goods;

import java.util.List;

public class Crawler_Test {
	public static void main(String[] args) {
		System.out.println("TB Test: ");
		Crawler_Test(new TB());
		System.out.println();
		System.out.println("JD Test: ");
		Crawler_Test(new JD());
	}
	private static void Crawler_Test(Crawler crawler) {
		try {
			for(int i = 0; i < 3; i++) {
				List<Goods> goodsArray = crawler.GetGoodsList("冰箱");
				if(goodsArray == null)
					continue;
				for (var obj : goodsArray) {
					System.out.println(
							obj.name + " " +
							obj.price + " " +
							obj.imgUrl + " " +
							obj.factory + " " +
							obj.clickUrl + " "
					);
				}
				break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
