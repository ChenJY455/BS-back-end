package Server;

import Goods.Goods;
import Crawler.JD;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

// 配置类，添加 @Configuration 注解
@Configuration
class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/*") // 允许所有路径
                .allowedOrigins("http://localhost:8081") // 指定允许的跨域来源
                .allowedMethods("GET", "POST") // 允许的 HTTP 方法
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true); // 允许携带认证信息
    }
}

@RestController
@RequestMapping("/api")
class GenericController {
    @GetMapping("/{path}")
    public ResponseEntity<String> HandleGet(@PathVariable String path, @RequestParam Map<String, String> params) {
        RestTemplate restTemplate = new RestTemplate();
        JD jdCrawler = new JD();
        // 处理 GET 请求
        switch(path) {
            case "login":
                if(params.get("username").equals("admin") && params.get("password").equals("admin")) {
                    return ResponseEntity.ok("Received GET request for path: " + path + " with params: " + params.toString());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
                }
            case "register":
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            case "getGoodsList":
                try {
                    List<Goods> goodsList = jdCrawler.GetGoodsList(params.get("keyword"));
                    return BuildRespBody(goodsList);
                }
                catch (Exception e) {
                    System.out.println(e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                }
            default:
                return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{path}")
    public ResponseEntity<String> HandlePost(@PathVariable String path, @RequestBody Map<String, Object> body) {
        // 处理 POST 请求
        return ResponseEntity.ok("Received POST request for path: " + path + " with body: " + body.toString());
    }

    private ResponseEntity<String> BuildRespBody(List<Goods> goodsList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(goodsList);
            return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
