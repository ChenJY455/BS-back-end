package Server.Service;

import Server.Crawler.JDCrawler;
import Server.Crawler.TBCrawler;
import Server.Entities.TBGoods;
import Server.Entities.JDGoods;
import Server.Repository.JDRepository;
import Server.Repository.TBRepository;
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
	
	public void UpdateGoodsService(String keyword) {
		try {
			// TODO: 更新覆盖
			tbCrawler.GetGoodsList(keyword);   // Update token
			List<TBGoods> tbGoods = tbCrawler.GetGoodsList(keyword);
			List<JDGoods> jdGoods = jdCrawler.GetGoodsList(keyword);
			tbRepository.saveAll(tbGoods);
			jdRepository.saveAll(jdGoods);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void GetGoodsService(Map<String, String> params) {
	
	}
}
