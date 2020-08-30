package com.example.administrator.hearinghelp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 瀹搞儱鍙跨猾锟�
 */
public class HttpUtil {

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 閹垫挸绱戦崪瀛禦L娑斿妫块惃鍕箾閹猴拷
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 鐠佸墽鐤嗛柅姘辨暏閻ㄥ嫯顕Ч鍌氱潣閹拷
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 瀵版鍩岀拠閿嬬湴閻ㄥ嫯绶崙鐑樼ウ鐎电钖�
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 瀵よ櫣鐝涚�圭偤妾惃鍕箾閹猴拷
        connection.connect();
        // 閼惧嘲褰囬幍锟介張澶婃惙鎼存柨銇旂�涙顔�
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 闁秴宸婚幍锟介張澶屾畱閸濆秴绨叉径鏉戠摟濞堬拷
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 鐎规矮绠� BufferedReader鏉堟挸鍙嗗ù浣规降鐠囪褰嘦RL閻ㄥ嫬鎼锋惔锟�
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }
}
