package com.ImageTrip.image.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

@Service
public class JsonDataService {
    // url정보에 발급받은 REST API 키 정보를 넣어줘서 데이터를 출력
    // 출력 데이터자체가 JSON 데이터 구조
    public String getJsonData(HttpURLConnection con) {
        StringBuilder response = new StringBuilder();
        System.out.println(response);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            System.out.println(e);
            // IOException 처리 로직
        } finally {
            con.disconnect();
        }
        System.out.println(response.toString());
        return response.toString();
    }

    //  JSON데이터를 String으로 변환
    public String getAddressFromKakaoResponse(String jsonString) {
        String address = "";
        try {
            JSONObject jObj = new JSONObject(jsonString);
            JSONArray documents = jObj.getJSONArray("documents");
            if (documents.length() > 0) {
                JSONObject firstDocument = documents.getJSONObject(0);
                JSONObject roadAddress = firstDocument.optJSONObject("road_address");   // 도로명 주소 : 시도+시군구+(읍/면)+도로명
                if (roadAddress != null) {
                    address = roadAddress.optString("address_name", "");
                } else {
                    JSONObject addressInfo = firstDocument.getJSONObject("address");    // 지번 주소 : 시도+시군구+(읍/면)+도로명
                    address = addressInfo.optString("address_name", "");
                }
            }
        } catch (JSONException e) {
            System.out.println(e);
            // JSON 파싱 중 오류 처리
        }
        return address;
    }
}
