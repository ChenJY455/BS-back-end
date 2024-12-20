package Server.Controller;

import Server.Entities.Goods;
import Server.Entities.History;
import Server.Repository.HistoryRepository;
import Server.Service.GoodsService;
import Server.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/goods")
public class GoodsController {
	@Autowired
	GoodsService goodsService;
	
	@GetMapping("/get-list")
	public ResponseEntity<List<Goods>> HandleGoodsList(@RequestParam Map<String, String> params) {
		List<Goods> goods = goodsService.GetGoodsService(params);
		return ResponseEntity.ok(goods);
	}
	
	@GetMapping("/get-history")
	public ResponseEntity<List<History>> HandleGoodsHistory(@RequestParam Map<String, String> params) {
		List<History> history = goodsService.GetHistoryService(params);
		return ResponseEntity.ok(history);
	}
}
