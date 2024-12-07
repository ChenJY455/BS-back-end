package Server.Crawler;

import Server.Entities.TBGoods;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static Server.Utils.Utils.getJsonObject16;
import static java.lang.Thread.sleep;

public class TBCrawler implements Crawler {
    private static String em_token = "";
    private static String em_token_enc = "";
    private static int totalResults = 4800;
    private static int sourceS = 0;
    private static int bcoffset = 0;
    private static int ntoffset = 0;
    
    @Override
    public List<TBGoods> GetGoodsList(String keyword) throws NoSuchAlgorithmException, IOException, InterruptedException {
        // Get client
        HttpClient client = HttpClient.newBuilder().build();
        List<TBGoods> goods_list = new ArrayList<>();
        for(int pg = 1; pg <= 10; pg++) {
            // Build Url with args
            String ep_data = BuildDataBody(keyword, pg);
            long t = System.currentTimeMillis();
            String sign = BuildSign(ep_data, t);
            String base_url = "https://h5api.m.taobao.com/h5/mtop.relationrecommend.wirelessrecommend.recommend/2.0/";
            String url = UriComponentsBuilder.fromHttpUrl(base_url)
                    .queryParam("jsv", "2.7.2")
                    .queryParam("appKey", "12574478")
                    .queryParam("t", t)
                    .queryParam("sign", sign)
                    .queryParam("api", "mtop.relationrecommend.wirelessrecommend.recommend")
                    .queryParam("v", "2.0")
                    .queryParam("type", "jsonp")
                    .queryParam("dataType", "jsonp")
                    .queryParam("callback", "mtopjsonp16")
                    .queryParam("data", ep_data)
                    .toUriString();
            
            String cookies_builder =
                    "cookie2=190e89cf252058f6ab7bb03457da180a; " +
                            "t=5d55b812d9e70f69aebc940145d1ae8a; " +
                            "_tb_token_=3150d963e13ab; " +
                            "mtop_partitioned_detect=1; " +
                            "_m_h5_tk=" + em_token + "; " +
                            "_m_h5_tk_enc=" + em_token_enc + "; " +
                            "thw=xx; " +
                            "cna=EMLWHyN7yW4BASQIhkLkbi2p; " +
                            "xlly_s=1; " +
                            "_samesite_flag_=true; " +
                            "3PcFlag=1733284983154; " +
                            "tfstk=f6rSd3jppQAWmMbP5pBqGmMZY1mQeWsNFpMLIJKyp0nJv9NUgkuKTaVIRWPmJuozUSGL_SGJYwcyADwQtWPQKTDK" +
                            "9WFITz7Vb82oxDCwAGSaEbWHsdaIw3dApYDpQNnY-82oxKv29NzYEBZv99w-pXBjDvMBwD3JynBjdAGKvb3pME" +
                            "HtMDhL92pvkADBv33KJ-BjKjH-vDH_2OHc7j253obkkZTHYHcvvHERxuG1YXDmKoMSPf3YQHH4cYgS68EPmz4x" +
                            "Horj7qJFGmegu7HjfgOiFres2vZhBeGIRumjdrfWz5Nop-3ba98nhywLj0m6pgejVbgTD4jCI5ebpPub0OIiV0" +
                            "h8802eC_ybV7yucJJpk0izlVZSXGA-Try_AvZhtiN_pR4KlkO54HtZ1aLJRKgMAxGNhtTH-cVeqRB0yOPoyxD4" +
                            "ot6X7W3-nxGNhtTH-4Hm3RXfhFPd; " +
                            "isg=BCEhLt_h4r_P0E5LJ7bdwd0lMO07zpXAoB_PhoPxvCn66k28mx_ckB9sTB7sIi34";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Cookie", cookies_builder)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                            " (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36 Edg/131.0.0.0")
                    .GET()
                    .build();
            
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            byte[] body_byte = response.body();
            JSONObject jsonObject = getJsonObject16(body_byte);
            String ret = jsonObject.get("ret").toString();
            if(!ret.equals("[\"SUCCESS::调用成功\"]")) {
                // Update cookie
                HttpHeaders headers = response.headers();
                List<String> set_cookies = headers.allValues("Set-Cookie");
                for (String cookie : set_cookies) {
                    if (cookie.contains("_m_h5_tk") && !cookie.contains("_m_h5_tk_enc")) {
                        em_token = cookie.split("=")[1].split(";")[0];
                    }
                    if (cookie.contains("_m_h5_tk_enc")) {
                        em_token_enc = cookie.split("=")[1].split(";")[0];
                    }
                }
                System.out.println(ret);
                return null;
            }
            // Success
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray data_arr = (JSONArray) data.get("itemsArray");
            for(int i = 0; i < data_arr.length(); i++) {
                JSONObject good_obj = data_arr.getJSONObject(i);
                JSONObject price_show = (JSONObject) good_obj.get("priceShow");
                JSONObject shop_info = (JSONObject) good_obj.get("shopInfo");
                goods_list.add(new TBGoods(
                        good_obj.getLong("item_id"),
                        good_obj.optString("pic_path"),
                        good_obj.optString("title"),
                        price_show.optDouble("price"),
                        good_obj.optString("clickUrl"),
                        shop_info.optString("title")
                ));
            }
            
            // Updata page args
            JSONObject pageArgs = (JSONObject) data.get("mainInfo");
            totalResults = pageArgs.optInt("totalResults");
            sourceS = pageArgs.optInt("sourceS");
            bcoffset = pageArgs.optInt("bcoffset");
            ntoffset = pageArgs.optInt("ntoffset");
            
            sleep(100);
        }
        return goods_list;
    }
    
    private static String BuildDataBody(String keyword, int page) {
        Gson gson = new GsonBuilder().create();
        LinkedHashMap<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("appId", "43356");
        
        LinkedHashMap<String, Object> paramsMap = new LinkedHashMap<>();
        paramsMap.put("device", "HMA-AL00");
        paramsMap.put("isBeta", "false");
        paramsMap.put("grayHair", "false");
        paramsMap.put("from", "nt_history");
        paramsMap.put("brand", "HUAWEI");
        paramsMap.put("info", "wifi");
        paramsMap.put("index", "4");
        paramsMap.put("rainbow", "");
        paramsMap.put("schemaType", "auction");
        paramsMap.put("elderHome", "false");
        paramsMap.put("isEnterSrpSearch", "true");
        paramsMap.put("newSearch", "false");
        paramsMap.put("network", "wifi");
        paramsMap.put("subtype", "");
        paramsMap.put("hasPreposeFilter", "false");
        paramsMap.put("prepositionVersion", "v2");
        paramsMap.put("client_os", "Android");
        paramsMap.put("gpsEnabled", "false");
        paramsMap.put("searchDoorFrom", "srp");
        paramsMap.put("debug_rerankNewOpenCard", "false");
        paramsMap.put("homePageVersion", "v7");
        paramsMap.put("searchElderHomeOpen", "false");
        paramsMap.put("search_action", "initiative");
        paramsMap.put("sugg", "_4_1");
        paramsMap.put("sversion", "13.6");
        paramsMap.put("style", "list");
        paramsMap.put("ttid", "600000@taobao_pc_10.7.0");
        paramsMap.put("needTabs", "true");
        paramsMap.put("areaCode", "CN");
        paramsMap.put("vm", "nw");
        paramsMap.put("countryNum", "156");
        paramsMap.put("m", "pc_sem");
        paramsMap.put("page", page + "");
        paramsMap.put("n", 48);
        paramsMap.put("q", keyword);
        paramsMap.put("qSource", "url");
        paramsMap.put("pageSource", "");
        paramsMap.put("tab", "all");
        paramsMap.put("pageSize", 48);
        paramsMap.put("totalPage", 100);
        paramsMap.put("totalResults", totalResults + "");
        paramsMap.put("sourceS", sourceS + "");
        paramsMap.put("sort", "_coefp");
        paramsMap.put("bcoffset", bcoffset + "");
        paramsMap.put("ntoffset", ntoffset + "");
        paramsMap.put("filterTag", "");
        paramsMap.put("service", "");
        paramsMap.put("prop", "");
        paramsMap.put("loc", "");
        paramsMap.put("start_price", "null");
        paramsMap.put("end_price", "null");
        paramsMap.put("startPrice", "null");
        paramsMap.put("endPrice", "null");
        paramsMap.put("itemIds", "null");
        paramsMap.put("p4pIds", "null");
        paramsMap.put("categoryp", "");
        paramsMap.put("myCNA", "EMLWHyN7yW4BASQIhkLkbi2p");
        paramsMap.put("clk1", "94f6d275b09367c294d8e965fae64ca9");
        paramsMap.put("refpid", "mm_2898300158_3078300397_115665800437");
        jsonMap.put("params", gson.toJson(paramsMap).replaceAll("\"", "\\\""));
        return gson.toJson(jsonMap);
    }

    private static String BuildSign(String ep_data, long eC) throws NoSuchAlgorithmException {
        String eS = "12574478";
        String body = em_token.split("_")[0] + "&" + eC + "&" + eS + "&" + ep_data;

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashBytes = messageDigest.digest(body.getBytes());
        return HexFormat.of().formatHex(hashBytes);
    }
}
