package Server.Service;

import Server.Crawler.JDCrawler;
import Server.Crawler.TBCrawler;
import Server.Entities.Goods;
import Server.Entities.TBGoods;
import Server.Entities.JDGoods;
import Server.Repository.JDRepository;
import Server.Repository.TBRepository;
import Server.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsService {
	@Autowired
	private JDRepository jdRepository;
	@Autowired
	private TBRepository tbRepository;
	
	TBCrawler tbCrawler = new TBCrawler();
	JDCrawler jdCrawler = new JDCrawler();
	
	private void UpdateGoodsService(String keyword, Utils.WebsiteType website) {
		try {
			if(website == Utils.WebsiteType.JD) {
				// TODO: 不一定要更新
				// TODO: 覆盖的问题
				List <Goods> GoodsList = jdRepository.findAllByKeyword(keyword);
				if(GoodsList == null || GoodsList.isEmpty() || GoodsList.get(0).OverDue()) {
					List<JDGoods> jdGoods = jdCrawler.GetGoodsList(keyword);
					jdRepository.saveAll(jdGoods);
				}
			}
			else {  // website == Utils.WebsiteType.TB
				List <Goods> GoodsList = tbRepository.findAllByKeyword(keyword);
				if(GoodsList == null || GoodsList.isEmpty() || GoodsList.get(0).OverDue()) {
					tbCrawler.GetGoodsList(keyword);   // Update token
					List<TBGoods> tbGoods = tbCrawler.GetGoodsList(keyword);
					tbRepository.saveAll(tbGoods);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Goods> GetGoodsService(Map<String, String> params) {
		String keyword = params.get("keyword");
		String websiteStr = params.get("website");
		Utils.WebsiteType website = Utils.WebsiteType.fromString(websiteStr);
		UpdateGoodsService(keyword, website);
		switch (website) {
			case JD:
				return jdRepository.findAllByKeyword(keyword);
			case TB:
				return tbRepository.findAllByKeyword(keyword);
		}
		return null;
	}
}
