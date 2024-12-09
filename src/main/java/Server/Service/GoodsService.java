package Server.Service;

import Server.Crawler.JDCrawler;
import Server.Crawler.TBCrawler;
import Server.Entities.*;
import Server.Exception.NotFoundException;
import Server.Repository.HistoryRepository;
import Server.Repository.JDRepository;
import Server.Repository.LikesRepsitory;
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
	@Autowired
	private HistoryRepository historyRepository;
	
	TBCrawler tbCrawler = new TBCrawler();
	JDCrawler jdCrawler = new JDCrawler();
	@Autowired
	private LikesRepsitory likesRepsitory;
	
	public List<Goods> GetGoodsService(Map<String, String> params) {
		String keyword = params.get("keyword");
		String websiteStr = params.get("website");
		Utils.WebsiteType website = Utils.WebsiteType.fromString(websiteStr);
		UpdateGoods(keyword, website);
		return switch (website) {
			case JD -> jdRepository.findByKeywordContaining(keyword);
			case TB -> tbRepository.findByKeywordContaining(keyword);
		};
	}
	
	public List<History> GetHistoryService(Map<String, String> params) {
		String website = params.get("website");
		long gid = Long.parseLong(params.get("gid"));
		List<History> historyList;
		if(website.equals(Utils.WebsiteType.JD.name())) {
			historyList = historyRepository.findAllByJdGids(new JDGoods(gid));
			if(historyList == null || historyList.isEmpty())
				throw new NotFoundException("历史信息未找到");
		}
		else if(website.equals(Utils.WebsiteType.TB.name())) {
			historyList = historyRepository.findAllByTbGids(new TBGoods(gid));
			if(historyList == null || historyList.isEmpty())
				throw new NotFoundException("历史信息未找到");
		}
		else
			throw new NotFoundException("未知网站");
		return historyList;
	}
	
	private void UpdateGoods(String keyword, Utils.WebsiteType website) {
		try {
			if(website == Utils.WebsiteType.JD) {
				// TODO: 不一定要更新
				List <Goods> GoodsList = jdRepository.findByKeywordContaining(keyword);
				if(GoodsList == null || GoodsList.isEmpty() || GoodsList.get(0).OverDue()) {
					List<JDGoods> jdGoods = jdCrawler.GetGoodsList(keyword);
					jdRepository.saveAll(jdGoods);
				}
			}
			else {  // website == Utils.WebsiteType.TB
				List <Goods> GoodsList = tbRepository.findByKeywordContaining(keyword);
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
	
	private void UpdateHistory() {
		List<Likes> likes = likesRepsitory.findAll();
		for(var like: likes) {
			User user = like.getUser();
			Goods goods = like.getJd_goods() != null?
					like.getJd_goods() : like.getTb_goods();
			// TODO: Single good crawler
		}
	}
}