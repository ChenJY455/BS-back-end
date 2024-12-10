package Server.Crawler;

import Server.Entities.History;
import Server.Entities.JDGoods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JDCrawler implements Crawler {
    @Override
    public List<JDGoods> GetGoodsList(String keyword, int page) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        List<JDGoods> goods_list = new ArrayList<>();
        // Part 1
        String base_url = "https://search.jd.com/Search";
        String url = UriComponentsBuilder.fromHttpUrl(base_url)
                .queryParam("keyword", URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                .queryParam("enc", "utf-8")
                .queryParam("wq", URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                .queryParam("pvid", "3a7ebc5b4b74472280b7ce2eed54878")
                .toUriString();
        // Build headers
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Referer", "https://search.jd.com/")
                .header("Origin", "https://search.jd.com")
                .header("Cookie", "__jdv=76161171|direct|-|none|-|1733318282467; __jdu=17333182824671" +
                        "251911185; 3AB9D23F7A4B3CSS=jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZ" +
                        "D3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTSHJA3YAAAAAADWCMQN2NP5O2ZMX; _gia_d=1; areaId=1" +
                        "5; ipLoc-djd=15-1213-0-0; PCSYCityID=CN_330000_330100_0; shshshfpa=298afdff-995b-a010-64" +
                        "43-53e9c766801a-1733318285; shshshfpx=298afdff-995b-a010-6443-53e9c766801a-1733318285; _" +
                        "pst=jd_oRuYNDNqxgSD; unick=jd_2on3057z2q2vxf; pin=jd_oRuYNDNqxgSD; thor=2F95E3975A89DA34" +
                        "57D6C978388FA5FA6CCBCBA48BDB31A9EC2C1C908B2DCEE2870D5E72B8E8D74D0DC978DB812C0C3AD4DA9293" +
                        "C2889F4CFBF5322C942A49781F28F1C76B943B542E1D53503CA636C21493CE0A3A699FCF8334D0E649B472E7" +
                        "E639B8C2195AA0165BE6FE92E414563F33E88BC5ED625E4B4BC25752B347F04E452B03C5EC0CB6560C8AA002" +
                        "9DED2B8B3626B8AB8B9A1084E23D153D82116A75; _tp=GsVOiNf2KYg8qBvUAr0ERw%3D%3D; pinId=kiavRP" +
                        "ELr0xX-AvOVLFMCg; umc_count=1; jsavif=1; flash=3_vDgZ2rGDIjknKT2X7yhgEFqzDGmW7gPRq8lTCIg" +
                        "U5mkuioDXt60Iv5Ju82PG2niFIXVnIa4PwVMR-4GdFwZEG-fIT3lIgDO_L0ii3g9nWXLSGnIyR6cPLtg4j8fX3Mv" +
                        "CHJYDLyDuSHn-qplhfYzTbhK3u_e5H8HOram1sjb6Bk**; __jda=143920055.17333182824671251911185.1" +
                        "733318282.1733318282.1733318282.1; __jdb=143920055.5.17333182824671251911185|1.173331828" +
                        "2; __jdc=143920055; shshshfpb=BApXS-TXakvZAfFPvaxxOpNaKOp58oDHABnFwhapo9xJ1MjFtTYG2; 3AB" +
                        "9D23F7A4B3C9B=AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23X" +
                        "H2L3RJZNTQXJNA5M")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (" +
                        "KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body_str = response.body();
        Document doc = Jsoup.parse(body_str);
        Elements goods = doc.select(".gl-i-wrap");
        ExtractGoods(goods_list, goods, keyword);
        
        // Part 2
        for(int pg = 1; pg <= page; pg++) {
            base_url = "https://re.jd.com/search";
            if(pg == 1)
                url = UriComponentsBuilder.fromHttpUrl(base_url)
                        .queryParam("keyword", URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                        .queryParam("enc", "utf-8")
                        .build()
                        .toUriString();
            else
                url = UriComponentsBuilder.fromHttpUrl(base_url)
                        .queryParam("keyword", URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                        .queryParam("page", pg)
                        .queryParam("enc", "utf-8")
                        .build()
                        .toUriString();
            String keywordEncoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            // Build headers
            request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Referer", "https://re.jd.com/search?keyword=" + keywordEncoded + "&keywordid=172887" +
                            "082207&re_dcp=202m0QjIIg==&traffic_source=1004&enc=utf8&cu=true&utm_source=baidu-sear" +
                            "ch&utm_medium=cpc&utm_campaign=t_262767352_baidusearch&utm_term=172887082207_0_fd72e5" +
                            "4cc8b7400099792e17ba7486f3")
                    .header("Cookie", "__jdu=17333182824671251911185; areaId=15; PCSYCityID=CN_330000_" +
                            "330100_0; shshshfpa=298afdff-995b-a010-6443-53e9c766801a-1733318285; shshshfpx=298afd" +
                            "ff-995b-a010-6443-53e9c766801a-1733318285; _pst=jd_oRuYNDNqxgSD; unick=jd_2on3057z2q2" +
                            "vxf; pin=jd_oRuYNDNqxgSD; thor=2F95E3975A89DA3457D6C978388FA5FA6CCBCBA48BDB31A9EC2C1C" +
                            "908B2DCEE2870D5E72B8E8D74D0DC978DB812C0C3AD4DA9293C2889F4CFBF5322C942A49781F28F1C76B9" +
                            "43B542E1D53503CA636C21493CE0A3A699FCF8334D0E649B472E7E639B8C2195AA0165BE6FE92E414563F" +
                            "33E88BC5ED625E4B4BC25752B347F04E452B03C5EC0CB6560C8AA0029DED2B8B3626B8AB8B9A1084E23D1" +
                            "53D82116A75; _tp=GsVOiNf2KYg8qBvUAr0ERw%3D%3D; pinId=kiavRPELr0xX-AvOVLFMCg; ipLoc-dj" +
                            "d=15-1213-3038-59931; _reuuid=e005980a8be64442b4090b1f7a864f1f; umc_count=1; jsavif=1" +
                            "; 3AB9D23F7A4B3CSS=jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6U" +
                            "DJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTUEDZL6YAAAAACXWKU6CJPOE3EAX; _gia_d=1; flash=3_9te" +
                            "THFVPWFnyYYG-cUOyfjIi-LEGTLdnG5E0JsqYn8-H968-Oof6YeLXMnVW0MKEUJCjW4jKcEHvtQz678OwFY3-" +
                            "QkAfIbhUy-OyTpptEgXGt9BbOb9KDcvCx6UtJ-pg57XrylB8hkkr3Ye-_ryI9pExelhLfm2dIq9LRTUJzk**; " +
                            "user-key=7afa1723-e89a-45bf-8888-5c0c0f697e30; cn=0; shshshfpb=BApXShO4AovZAfFPvaxxOp" +
                            "NaKOp58oDHABnFwhapq9xJ1MjFtTYG2; hf_time=1733591507626; unpl=JF8EALRnNSttDx5SBk8GHkBC" +
                            "Qg5SW1wJSh8LaW8GAF1eGAQDHgscRRJ7XlVdWBRLFB9uYRRXXFNLVA4fAisSEXteU11bD00VB2xXVgQFDQ8WU" +
                            "UtBSUt-S1tXV1QOSh4AbGYDZG1bS2QFGjIbFBJJX1RXXgFIEgRpZwZQXVBLVAUSBisTIExtZG5YDEgWAmxXBG" +
                            "RcaAkAWRkKHhsQSxBUWF8KSRcKbG4GUVpeS1cBGwobEhBCWWRfbQs; re_mbp=CAgQ4uHuyQEaLzE3Mjg4NzA" +
                            "4MjIwN18wX2ZkNzJlNTRjYzhiNzQwMDA5OTc5MmUxN2JhNzQ4NmYzIABCAnJpWi8xNzI4ODcwODIyMDdfMF9m" +
                            "ZDcyZTU0Y2M4Yjc0MDAwOTk3OTJlMTdiYTc0ODZmMw; __jda=229668127.17333182824671251911185.1" +
                            "733318282.1733557932.1733573265.5; __jdb=229668127.4.17333182824671251911185|5.173357" +
                            "3265; __jdc=229668127; __jdv=229668127|baidu-search|t_262767352_baidusearch|cpc|17288" +
                            "7082207_0_fd72e54cc8b7400099792e17ba7486f3|1733573512248; 3AB9D23F7A4B3C9B=AKYIJ453U7" +
                            "RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5M")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHT" +
                            "ML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0")
                    .GET()
                    .build();
            
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            body_str = response.body();
            doc = Jsoup.parse(body_str);
            String pageData = doc.select("script").get(2).html();
            JSONObject data = new JSONObject(pageData.substring(15, pageData.length() - 1));
            JSONArray dataList = data.getJSONArray("result");
            for(int i = 0; i < dataList.length(); i++) {
                JSONObject goodData = dataList.getJSONObject(i);
                goods_list.add(new JDGoods(
                    goodData.optLong("sku_id"),
                    goodData.optString("image_url"),
                    goodData.optString("ad_title"),
                    goodData.optDouble("sku_price"),
                    goodData.optString("click_url"),
                    "",
                    keyword
                ));
            }
        }
        return goods_list;
    }
    
    public double GetPrice(long gid, String name) throws IOException, InterruptedException {
        List<JDGoods> jdGoods = GetGoodsList(name, 1);
        for(JDGoods jdGood: jdGoods) {
            if(jdGood.getGid() == gid)
                return jdGood.getPrice();
        }
        throw new RuntimeException("商品不存在");
    }
    
    private void ExtractGoods(List<JDGoods> goods_list, Elements goods, String keyword) {
        for (Element good: goods) {
            long id = Long.parseLong(good.attr("id").split("_")[1]);
            String imgUrl = "https:" + good.select(".p-img > a > img").attr("data-lazy-img");
            double price =Double.parseDouble(good.select(".p-price > strong > i").text());
            String name = good.select(".p-name > a > em").text();
            String clickUrl = "https://item.jd.com/" + id + ".html";
            String factory = good.select(".p-shop > span > a").text();
            goods_list.add(new JDGoods(id, imgUrl, name, price, clickUrl, factory, keyword));
        }
    }
}
