package com.example.administrator.hearinghelp;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main
{
    static private String clientId = "ERGeadfN3yIXSpc4WtNnPeWh";
    static private String clientSecret = "HaPfkiYAhgPxXH5uz4p49F25uleeYWcw";

    public static void main(String[] args) throws Exception
    {
        String soundFile="E:\\JAVA\\source\\SoundDetectTest\\ve_1.m4a";
        String res=null;
        for(int i=0;i<10;i++)
            res = soundDetectTest(soundFile);
        System.out.println(res);
    }

    public static String soundDetectTest(String soundFile) throws Exception
    {
        String access_token=null;
        Cache cache=new Cache();
        try
        {
            if(cache.ifTimeNull())
            {
                access_token=getTokenTest(clientId,clientSecret);
                cache.updateAll(access_token);
            }
            else
            if(cache.ifTimeOld())
            {
                access_token=getTokenTest(clientId,clientSecret);
                cache.updateAll(access_token);
            }
            else
                access_token=cache.getToken();
        } catch (Exception e)
        {
            System.err.println("cache error");
        }
        //System.out.println(access_token);

        return easydlSoundClassify(access_token, soundFile);
    }

    //获取token
    public static String getTokenTest(String ak,String sk)
    {

        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            //忽略证书请求
            if("https".equalsIgnoreCase(realUrl.getProtocol()))
            {
                SslUtils.ignoreSsl();
            }
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            /*
             *for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            */
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            //System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    //easydl声音分类
    public static String easydlSoundClassify(String token,String soundFile) throws Exception {
        //获取声音base64编码
        String base64Sound =getSoundStr(soundFile);
        //System.out.println(base64Sound);

        //声音识别url
        String soundUrl= "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/sound_cls/sound_cols";

        //参数封装为json
        Map<String, Object> map = new HashMap<>();
        map.put("sound", base64Sound);
        String param = JSON.toJSONString(map);

        //第三步，发送请求

        JSONObject response = new JSONObject(HttpUtil.post(soundUrl, token, "application/json", param));
        //获取回复
        //System.out.println(response);

        //截取类型名字段
        if(response.has("error_code"))
        {
            int error_code=response.getInt("error_code");
            if(error_code==110||error_code==111)
            {
                Cache cache =new Cache();
                cache.setTimeNull();
                response = new JSONObject(HttpUtil.post(soundUrl, getTokenTest(clientId, clientSecret), "application/json", param));
            }
            else {
                return "Token_error";
            }
        }
        org.json.JSONArray results=response.getJSONArray("results");
        String name=results.getJSONObject(0).getString("name");
        Double score=results.getJSONObject(0).getDouble("score");
        if(score>=0.5)
            return name;
        else {
            return "NoTagFind";
        }

    }

    //解析声音转成base64编码
    public static String getSoundStr(String soundFile) throws Exception {

        byte[] imgData1 = FileUtil.readFileByBytes(soundFile);
        return Base64Util.encode(imgData1);

    }


}
