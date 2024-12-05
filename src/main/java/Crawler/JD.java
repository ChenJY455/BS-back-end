package Crawler;

import Goods.Goods;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.LinkedHashMap;
import java.util.List;

public class JD implements Crawler{
    public List<Goods> GetGoodsList(String keyword) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder().create();
        HttpClient client = HttpClient.newBuilder().build();
        List<Goods> goods_list = new ArrayList<>();
        // Part 1
        String base_url = "https://search.jd.com/Search";
        String url = UriComponentsBuilder.fromHttpUrl(base_url)
                .queryParam("keyword", keyword)
                .queryParam("enc", "utf-8")
                .queryParam("wq", keyword)
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
        ExtractGoods(goods_list, goods);
        
        // Part 2
        base_url = "https://api.m.jd.com/";
        LinkedHashMap<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("keyword", keyword);
        bodyMap.put("wq", keyword);
        bodyMap.put("pvid", "3a7ebc5b4b74472280b7ce2eed548789");
        bodyMap.put("page", "2");
        bodyMap.put("s", "29");
        bodyMap.put("scrolling", "y");
        bodyMap.put("log_id", "1733318317697.6696");
        bodyMap.put("tpl", "1_M");
        bodyMap.put("isList", 0);
        bodyMap.put("show_items", "");
        String body = gson.toJson(bodyMap).replaceAll("\"", "\\\"");
        String body_encode = URLEncoder.encode(body, StandardCharsets.UTF_8);
        
        url = UriComponentsBuilder.fromHttpUrl(base_url)
                .queryParam("appid", "search-pc-java")
                .queryParam("functionId", "pc_search_s_new")
                .queryParam("client", "pc")
                .queryParam("clientVersion", "1.0.0")
                .queryParam("t", System.currentTimeMillis())
                .queryParam("body", body_encode)
                .queryParam("loginType", "3")
                .queryParam("uuid", "143920055.17333182824671251911185.1733318282.1733318282.1733318282.1")
                .queryParam("area", "15_1213_0_0")
                .queryParam("h5st", "20241204211959724;lfeye050k4ir9kk9;f06cc;tk03w85e01be118naOPVnfA3g" +
                        "10Z--X1RMMRfbAF9xeVL_XxZBcsjWK2jdtbleAsPdUkTMNFISqPR5P-9laiS1UymMxc;9752e15e2d40b360d1aa26cc" +
                        "549e4fd419abde56e374458c98d4d453c9d552c0;4.9;1733318399724;pjbMhjZdA_Vd5GleAqYe7XVNGSlQJrJdJ" +
                        "rESJrpjh7pf5rJdJz1TIipjLDrgJLIeFSoS5TYdHeYe0j4e6r4TIWFe7T1eISVS3nVSKa1TJrJdJrEa-OFTGOEjLrJp-" +
                        "jJSJOYf1XYT4f4fHqIT5j1e6jFeJiVf5XVd6nIS0jFS7jpjxj5PKSEQKeFjLrJp-jZe9HIg3T0UG6VRFuWeDipjxjJOJ" +
                        "rpjh7Jjj6lZdC0bgOld6D0XJrJdJ31QHyVT5ipjLDrgJj4f9G1WJrJdJTlPJrpjh7ZMLrJp7rJdJLYOJipjLrpjh75f4" +
                        "rJdJTYOJipjLrpjh7pfLDIj2XETJrpjLrJp-rojxjpe2iFjLrpjLDrg0fojxj5f2iFjLrpjLDrg53pjxjJf2iFjLrpjL" +
                        "DrgJTIg6zpfJrJdJnYOJipjLrpjh7pfLDIjAOEjLrpjLDrg2rJdJfkQJrpjLrJp-rojxjpQJrpjLrJp-rojxjpS0ipjL" +
                        "rpjh-kjxjpS9WlOzWFjLrJp-3kjLDLjne2SzmEYnyXV2i2QMaFRJrJdJjoPJrpjLrJpwqJdJrkPJrpjh7Jj3ToNL-oe1" +
                        "zVRUq5d7zpf6rpWdq5P0ulS9G1WJrJdJnVO4ipjLD7N;873d485387558a416cfc8ae036d05eb70c569608f664e983" +
                        "cf770c7a261b0b32")
                .queryParam("x-api-eid-token", "jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ" +
                        "7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTSHJA3YAAAAAADWCMQN2NP5O2ZMX")
                .build()
                .toUriString();
        
        // Build headers
        request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Referer", "https://search.jd.com/")
                .header("Origin", "https://search.jd.com")
                .header("Cookie", "__jdv=76161171|direct|-|none|-|1733318282467; __jdu=17333182824671251" +
                        "911185; 3AB9D23F7A4B3CSS=jdd03AKYIJ453U7RKTIL3OK34TXUWMZBDJGCJJWQPI73X76S3CYXGHJ7GBHZD3KL6U" +
                        "DJZJ77UZ6V23XH2L3RJZNTQXJNA5MAAAAMTSHJA3YAAAAAADWCMQN2NP5O2ZMX; _gia_d=1; areaId=15; ipLoc-" +
                        "djd=15-1213-0-0; PCSYCityID=CN_330000_330100_0; shshshfpa=298afdff-995b-a010-6443-53e9c7668" +
                        "01a-1733318285; shshshfpx=298afdff-995b-a010-6443-53e9c766801a-1733318285; _pst=jd_oRuYNDNq" +
                        "xgSD; unick=jd_2on3057z2q2vxf; pin=jd_oRuYNDNqxgSD; thor=2F95E3975A89DA3457D6C978388FA5FA6C" +
                        "CBCBA48BDB31A9EC2C1C908B2DCEE2870D5E72B8E8D74D0DC978DB812C0C3AD4DA9293C2889F4CFBF5322C942A4" +
                        "9781F28F1C76B943B542E1D53503CA636C21493CE0A3A699FCF8334D0E649B472E7E639B8C2195AA0165BE6FE92" +
                        "E414563F33E88BC5ED625E4B4BC25752B347F04E452B03C5EC0CB6560C8AA0029DED2B8B3626B8AB8B9A1084E23" +
                        "D153D82116A75; _tp=GsVOiNf2KYg8qBvUAr0ERw%3D%3D; pinId=kiavRPELr0xX-AvOVLFMCg; umc_count=1;" +
                        " jsavif=1; flash=3_vDgZ2rGDIjknKT2X7yhgEFqzDGmW7gPRq8lTCIgU5mkuioDXt60Iv5Ju82PG2niFIXVnIa4P" +
                        "wVMR-4GdFwZEG-fIT3lIgDO_L0ii3g9nWXLSGnIyR6cPLtg4j8fX3MvCHJYDLyDuSHn-qplhfYzTbhK3u_e5H8HOram" +
                        "1sjb6Bk**; __jda=143920055.17333182824671251911185.1733318282.1733318282.1733318282.1; __jd" +
                        "b=143920055.5.17333182824671251911185|1.1733318282; __jdc=143920055; shshshfpb=BApXS-TXakvZ" +
                        "AfFPvaxxOpNaKOp58oDHABnFwhapo9xJ1MjFtTYG2; 3AB9D23F7A4B3C9B=AKYIJ453U7RKTIL3OK34TXUWMZBDJGC" +
                        "JJWQPI73X76S3CYXGHJ7GBHZD3KL6UDJZJ77UZ6V23XH2L3RJZNTQXJNA5M")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHT" +
                        "ML, like Gecko) Chrome/130.0.0.0 Safari/537.36 Edg/130.0.0.0")
                .GET()
                .build();
        
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        body_str = response.body();
        doc = Jsoup.parse(body_str);
        goods = doc.select(".gl-i-wrap");
        ExtractGoods(goods_list, goods);
        // If error, return null
        return goods_list;
    }
    
    private void ExtractGoods(List<Goods> goods_list, Elements goods) {
        for (Element good: goods) {
            String imgUrl = "https:" + good.select(".p-img > a > img").attr("data-lazy-img");
            double price =Double.parseDouble(good.select(".p-price > strong > i").text());
            String name = good.select(".p-name > a > em").text();
            String clickUrl = "https://item.jd.com/" + good.attr("id").split("_")[1] + ".html";
            String factory = good.select(".p-shop > span > a").text();
            goods_list.add(new Goods(imgUrl, name, price, clickUrl, factory));
        }
    }
}
