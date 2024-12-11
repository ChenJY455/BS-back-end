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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	@Autowired
	private LikesRepsitory likesRepsitory;
	
	TBCrawler tbCrawler = new TBCrawler();
	JDCrawler jdCrawler = new JDCrawler();

	
	public List<Goods> GetGoodsService(Map<String, String> params) {
		String keyword = params.get("keyword");
		String websiteStr = params.get("website");
		Utils.WebsiteType website = Utils.WebsiteType.fromString(websiteStr);
		int page = Integer.parseInt(params.get("page"));
		if(page == 1)
			UpdateGoods(keyword, website, 1);
		// 用一个新的线程去更新剩余的商品
		Thread thread = new Thread(() -> {
			// 先看需不需要更新
			Sort sort = Sort.by(Sort.Order.asc("t"));
			Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
			switch (website) {
				case JD -> {
					List<Goods> goodsList = jdRepository.findByKeywordContaining(keyword, pageable);
					if(goodsList == null || goodsList.isEmpty() || goodsList.get(0).OverDue()) {
						UpdateGoods(keyword, Utils.WebsiteType.JD, 20);
					}
				}
				case TB -> {
					List<Goods> goodsList = tbRepository.findByKeywordContaining(keyword, pageable);
					if(goodsList == null || goodsList.isEmpty() || goodsList.get(0).OverDue()) {
						UpdateGoods(keyword, Utils.WebsiteType.TB, 20);
					}
				}
			}
			UpdateGoods(keyword, website, 10);
		});
		thread.start();
		Pageable pageable = PageRequest.of(page - 1, 30);
		return switch (website) {
			case JD -> jdRepository.findByKeywordContaining(keyword, pageable);
			case TB -> tbRepository.findByKeywordContaining(keyword, pageable);
		};
	}
	
	public List<History> GetHistoryService(Map<String, String> params) {
		// TODO: 需要改成定时更新
		UpdateHistory();
		String website = params.get("website");
		long gid = Long.parseLong(params.get("gid"));
		List<History> historyList;
		Sort sort = Sort.by(Sort.Order.asc("t"));
		if(website.equals(Utils.WebsiteType.JD.name())) {
			historyList = historyRepository.findAllByJdGid(gid, sort);
			if(historyList == null || historyList.isEmpty())
				throw new NotFoundException("历史信息未找到");
		}
		else if(website.equals(Utils.WebsiteType.TB.name())) {
			historyList = historyRepository.findAllByTbGid(gid, sort);
			if(historyList == null || historyList.isEmpty())
				throw new NotFoundException("历史信息未找到");
		}
		else
			throw new NotFoundException("未知网站");
		return historyList;
	}
	
	private void UpdateGoods(String keyword, Utils.WebsiteType website, int page) {
		try {
			if(website == Utils.WebsiteType.JD) {
				List<JDGoods> jdGoods = jdCrawler.GetGoodsList(keyword, page);
				jdRepository.saveAll(jdGoods);
			}
			else {  // website == Utils.WebsiteType.TB
				List<TBGoods> tbGoods = tbCrawler.GetGoodsList(keyword, page);
				tbRepository.saveAll(tbGoods);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void UpdateHistory() {
		List<Likes> likes = likesRepsitory.findAll();
		for(var like: likes) {
			Utils.WebsiteType website = like.getWebsite();
			String name = like.getName();
			switch (website) {
				case JD -> {
					try {
						long gid = like.getJdGid();
						double price = jdCrawler.GetPrice(gid, name);
						List<History> historyList= historyRepository.findAllByJdGid(
								gid,
								Sort.by(Sort.Order.desc("t")));
						if(historyList == null ||
								historyList.isEmpty() ||
								historyList.get(0).getPrice() != price)
						{
							historyRepository.save(new History(0, gid, price));
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				case TB -> {
					try {
						long gid = like.getTbGid();
						double price = tbCrawler.GetPrice(gid);
						List<History> historyList= historyRepository.findAllByTbGid(
								gid,
								Sort.by(Sort.Order.desc("t")));
						if(historyList == null ||
								historyList.isEmpty() ||
								historyList.get(0).getPrice() != price)
						{
							historyRepository.save(new History(gid, 0, price));
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			
		}
	}
}