// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chat;

import net.cassite.desktop.chara.util.Logger;
import vjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TianxingTuling implements Chatbot {
    private String apiKey;

    @Override
    public String name() {
        return "tianxing";
    }

    @Override
    public void init(String config) throws Exception {
        if (config.isBlank()) {
            throw new Exception("chatbot config should be \"" + name() + ":your_api_key\"");
        }
        apiKey = config;
    }

    public String[] takeMessage(String msg) {
        String httpUrl = "http://api.tianapi.com/txapi/tuling/index?key=" + apiKey + "&question=" +
            URLEncoder.encode(msg, StandardCharsets.UTF_8);
        String jsonResult = request(httpUrl);
        //noinspection rawtypes
        JSON.Instance inst;
        try {
            inst = JSON.parse(jsonResult);
        } catch (Exception e) {
            Logger.error("TianxingTuling response is not json: " + jsonResult, e);
            return null;
        }
        Logger.info("TianxingTuling return " + inst.stringify());
        try {
            JSON.Object o = (JSON.Object) inst;
            if (o.getInt("code") != 200) {
                Logger.error("TianxingTuling response code is not 200");
                return null;
            }
            var arr = o.getArray("newslist");
            String[] ret = new String[arr.length()];
            for (int i = 0; i < ret.length; ++i) {
                ret[i] = arr.getObject(i).getString("reply");
            }
            return ret;
        } catch (RuntimeException e) {
            Logger.error("TianxingTuling response is not valid: " + jsonResult, e);
            return null;
        }
    }

    private static String request(String httpUrl) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String result = null;

        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
                sb.append("\r\n");
            }
            reader.close();
            result = sb.toString();
        } catch (Exception e) {
            Logger.error("request TianxingTuling failed", e);
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ignore) {
            }
        }
        return result;
    }
}
