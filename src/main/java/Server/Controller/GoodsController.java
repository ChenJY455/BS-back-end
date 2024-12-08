package Server.Controller;

import Server.Entities.Goods;
import Server.Service.GoodsService;
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
	
	@GetMapping("/list")
	public ResponseEntity<List<Goods>> HandleGoodsList(@RequestParam Map<String, String> params) {
		List<Goods> goods = goodsService.GetGoodsService(params);
		return ResponseEntity.ok(goods);
	}
	
	@GetMapping("history")
	public ResponseEntity<String> HandleGoodsHistory(@RequestParam Map<String, String> params) {
		return ResponseEntity.ok("Success");
	}
}
