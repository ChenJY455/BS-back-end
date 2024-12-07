package Server.Controller;

import Server.Service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/goods")
public class GoodsController {
	@Autowired
	GoodsService goodsService;
	
	@GetMapping("/list")
	public ResponseEntity<String> HandleGoodsList(@RequestParam Map<String, String> params) {
		String keyword = params.get("keyword");
		goodsService.UpdateGoodsService(keyword);
		return ResponseEntity.ok("Success");
	}
	
	@GetMapping("history")
	public ResponseEntity<String> HandleGoodsHistory(@RequestParam Map<String, String> params) {
		return ResponseEntity.ok("Success");
	}
}
