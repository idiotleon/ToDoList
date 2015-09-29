package com.example.tek.first.servant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HAN KE on 2015/8/29.
 */
public class ProcessJSON {

    public static void processJson(String jsonStuff, ArrayList result_Array) throws JSONException {
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        JSONObject region = json.getJSONObject("region");
        JSONObject center = region.getJSONObject("center");
        String now_latitude = center.getString("latitude");
        String now_longitude = center.getString("longitude");
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            String name = business.getString("name");
            float rating = Float.parseFloat(business.getString("rating"));
            int review_count = Integer.parseInt(business.getString("review_count"));
            String category = business.getString("categories");
            String category_result = handlecategory(category);
            String image_url = business.getString("image_url");
            String snippet_text =business.getString("snippet_text");
            String phone_number = business.getString("display_phone");

            String mobile_url = business.getString("mobile_url");
            JSONObject location = business.getJSONObject("location");
            String  display_address = location.getString("display_address");
            String address = handleaddress(display_address);

            JSONObject coordinate = location.getJSONObject("coordinate");
            String res_latitude = coordinate.getString("latitude");
            String res_longitude = coordinate.getString("longitude");
            Double distance = Distance.getDistance(Double.parseDouble(now_latitude), Double.parseDouble(now_longitude), Double.parseDouble(res_latitude), Double.parseDouble(res_longitude));
            InfoCollection Info = new InfoCollection(name,rating,review_count,category_result,image_url,snippet_text,address
                    ,phone_number,mobile_url,distance);
            result_Array.add(Info);
        }
    }
    private static String handleaddress(String display_address) {
        String[] s = display_address.split("\"");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<s.length;i++){
            if(s[i].length()>1){
                sb.append(s[i]);
                sb.append("\n");
            }
        }
        return sb.toString().substring(0,sb.length()-1);

    }

    private static String handlecategory(String category) {

        String res[] = category.split("\"|,");
        StringBuilder sb = new StringBuilder();
        ArrayList<String> list = new ArrayList<>();
        for(int i=0;i<res.length;i++){
            if(res[i].length()>2){
                list.add(res[i]);
            }
        }
        for(int i=0;i<list.size();i+=2){
            sb.append(list.get(i));
            sb.append(",");
        }
        return sb.toString().substring(0,sb.length()-1);

    }

}
