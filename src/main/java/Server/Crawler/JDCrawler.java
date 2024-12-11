package Server.Crawler;

import Server.Entities.JDGoods;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
                .queryParam("keyword", keyword)
                .queryParam("enc", "utf-8")
                .toUriString();
        // Build headers
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", "__jdu=17333182824671251911185; areaId=15; PCSYCityID=CN_330000_330100_0; shshshfpa=298afdff-995b-a010-6443-53e9c766801a-1733318285; shshshfpx=298afdff-995b-a010-6443-53e9c766801a-1733318285; ipLoc-djd=15-1213-3038-59931; qrsc=3; user-key=7afa1723-e89a-45bf-8888-5c0c0f697e30; rkv=1.0; jsavif=1; umc_count=1; pinId=3B9Hy-IeniS1qy8dd7-DCkvWB9rZ95Do; pin=jd_8tIZxgXboHQAaBM; unick=jd_7c7577hspe9d4p; _tp=21OT6JrxL1aE%2FaPlv%2FZqd9X0cYPnX9jBdHPZ1uoTAiM%3D; _pst=jd_8tIZxgXboHQAaBM; unpl=JF8EALRnNSttDx9dVRILHBEQTg1UWwgKSB8KP2ACUV1RHgQEEgtJFhB7XlVdWBRLFB9vZxRUXVNOXA4YASsSEXteU11bD00VB2xXVgQFDQ8WUUtBSUt-S1tXV1QOSh4AbGYDZG1bS2QFGjIbFBJJX1RXXgFIEgRpZwZQXVBLVAUSBisTIExtZG5fDUMeC21XBGRcaAkAWR8KHBcSQxBUWF8KSRcKbG4GUVpeS1cBGwobEhBCWWRfbQs; __jdv=229668127|baidu-search|t_262767352_baidusearch|cpc|172887082207_0_fe8a886214a14d3298a66418da088c51|1733888963149; mt_xid=V2_52007VwMUUF9aUlMcQBpZAmEDEVZdUFJaH0AdbFU3BkFVXF9VRhZLHgsZYldABUELUw5IVR0IUG4GEQIPD1FaHXkaXQZiHxJRQVhSSx9KEl0MbAMTYl9oUWocSB9UAGIzEVVdXg%3D%3D; avif=1; jsavif=1; TrackID=1VX78I4aE0D67WiiLZmcHd7_2JZ-ZbBY4Av0ceFDKKm_FII28V__mtAGU_gY9gdiggxudDaJTXHiCaja5C8e4zBko6kDgPaGQFOxrxaU_2Ib1rG7MKJ5WLMCm9wCFFv7i; thor=487B2C4A2268FD7AD8291C1E3E9E63993D72C9FAA87823F03DD827C422D98C018414F77F0490820A381DF7CE6832562EA12BE222130885719EC4F23A2B57315777AE2A0E813CD4337C60ED4BE9257EB2DC767DEFA4A146AFE73DA5546746818B179074D715691D2BD2F418DBE7AC9A34DDB8A36D1170836C23C27E1EBCD2F4A69407D1D620903CA39DEFDE43F5F08F55D786BA54E52D9013A078B28749B92C8E; light_key=AASBKE7rOxgWQziEhC_QY6yadRPZlQi6kNlHvqlvaulmS4MM5ogEwjj91p0fguufZp9o4rUH; ceshi3.com=000; xapieid=jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTWQP2FDYAAAAAC7ZA4XMPLL26DAX; 3AB9D23F7A4B3CSS=jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTWQQZ72AAAAAACXICOBMDEYKFCQX; _gia_d=1; flash=3_UjJIbgJ_koiqnNJuQ0tukyMmvVj6vjgj7Rec55iSnzUqsnsiPJwGD86GuH_lHjFdI88-sPC9RmG_USOnQtQLeVJoPNO7v-4-Htoref5zuLAadDvtnqrqfgyFaCnrxsmSHUPBLieoXwf9iuBClOiZ63LEGGwF2pjgZWUf6wjdLI6NsVM8ixog5f-T; 3AB9D23F7A4B3C9B=AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5M; joyya=1733893926.1733893929.27.1eldhbn; __jda=76161171.17333182824671251911185.1733318282.1733887562.1733893792.15; __jdb=76161171.9.17333182824671251911185|15.1733893792; __jdc=76161171; shshshfpb=BApXSu1Qpt_ZAfFPvaxxOpNaKOp58oDHABnFwhapq9xJ1MjFtTYG2")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
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
                String name = goodData.optString("ad_title");
                if(name.length() > 255)
                    name = name.substring(0, 255);
                goods_list.add(new JDGoods(
                    goodData.optLong("sku_id"),
                    "https://img1.360buyimg.com/n6/" + goodData.optString("image_url"),
                    name,
                    goodData.optDouble("sku_price"),
                    goodData.optString("click_url"),
                    "京东商城",
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
