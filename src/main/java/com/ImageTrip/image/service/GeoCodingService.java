package com.ImageTrip.image.service;

import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GeoCodingService {
    // KAKAO Maps Geocoding API 이용해서 위치 데이터를 주소로 변환
    public String convertAddrKakao(double latitude, double longitude) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("KAKAO_API_KEY"); //YOUR_KAKAO_API_KEY
        String apiURL = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="
                + longitude + "&y=" + latitude + "&input_coord=WGS84";
        String addr = "";

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "KakaoAK " + apiKey);

        JsonDataService jsonDataService = new JsonDataService(); // 의존성 주입을 사용할 수도 있습니다.
        addr = jsonDataService.getAddressFromKakaoResponse(jsonDataService.getJsonData(con));

        return addr;
    }
}
