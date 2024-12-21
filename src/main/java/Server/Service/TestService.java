package Server.Service;

import Server.Entities.*;
import Server.Exception.NotFoundException;
import Server.Exception.UnAuthedException;
import Server.Repository.HistoryRepository;
import Server.Repository.JDRepository;
import Server.Repository.LikesRepsitory;
import Server.Repository.TBRepository;
import Server.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestService {
	@Autowired
	JDRepository jdRepository;
	@Autowired
	TBRepository tbRepository;
	@Autowired
	LikesRepsitory likesRepsitory;
	@Autowired
	HistoryRepository historyRepository;
	@Autowired
	EmailService emailService;
	
	public void changePriceService(Map<String, Object> body) {
		Utils.WebsiteType websiteType = Utils.WebsiteType.valueOf(body.get("website").toString());
		long gid = Long.parseLong(body.get("gid").toString());
		double price = Double.parseDouble(body.get("price").toString());
		List<Likes> likesList = null;
		// 更新goods、history、likes等
		switch (websiteType) {
			case TB ->  {
				TBGoods tbGoods = tbRepository.findById(gid).orElseThrow(() -> new NotFoundException("商品不存在"));
				tbGoods.setPrice(price);
				tbRepository.save(tbGoods);
				likesList = likesRepsitory.findAllByTbGoods(tbGoods);
				if(likesList == null || likesList.isEmpty()) {
					return;
				}
			}
			case JD -> {
				JDGoods jdGoods = jdRepository.findById(gid).orElseThrow(() -> new NotFoundException("商品不存在"));
				jdGoods.setPrice(price);
				jdRepository.save(jdGoods);
				likesList = likesRepsitory.findAllByJdGoods(jdGoods);
				if(likesList == null || likesList.isEmpty()) {
					return;
				}
			}
		}
		
		List<History> historyList= historyRepository.findAllByTbGid(
				gid,
				Sort.by(Sort.Order.desc("t")));
		if(historyList != null &&
				!historyList.isEmpty() &&
				historyList.get(0).getPrice() != price) {
			if(price < historyList.get(0).getPrice()) {
				// 发送降价信息
				for(Likes likes: likesList) {
					User user = likes.getUser();
					emailService.sendEmail(user.getEmail(),
							"降价提醒",
							"亲爱的用户：\n" +
									"   您关注的商品降价啦，快去看看吧！");
					
				}
			}
			long tbGid = websiteType == Utils.WebsiteType.TB ? gid : 0;
			long jdGid = websiteType == Utils.WebsiteType.JD ? gid : 0;
			historyRepository.save(new History(tbGid, jdGid, price));
		}
	}
}
