// ***LICENSE*** This file is licensed under GPLv2 with Classpath Exception. See LICENSE file under project root for more info

package net.cassite.desktop.chara.chat;

import net.cassite.desktop.chara.ThreadUtils;
import net.cassite.desktop.chara.util.Logger;
import vclient.HttpClient;
import vclient.impl.Http1ClientImpl;
import vfd.IP;
import vfd.IPPort;
import vjson.JSON;
import vproxybase.dns.Resolver;
import vproxybase.util.Callback;

import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class TianxingTuling extends AbstractChatbot implements Chatbot {
    private static final String HOSTNAME = "api.tianapi.com";

    private String apiKey;
    private HttpClient httpClient;

    public TianxingTuling() {
        super("tianxing");
    }

    @Override
    public void init(String config) throws Exception {
        if (config.isBlank()) {
            throw new Exception("chatbot config should be \"" + name() + ":your_api_key\"");
        }
        apiKey = config;
    }

    @Override
    public void takeMessage(String msg) {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    Resolver.getDefault().resolve(HOSTNAME, new Callback<>() {
                        @Override
                        protected void onSucceeded(IP ip) {
                            httpClient = new Http1ClientImpl(new IPPort(ip, 80), ThreadUtils.get().getLoop(), 5000);
                            sendRequest(msg);
                        }

                        @Override
                        protected void onFailed(UnknownHostException err) {
                            Logger.error("cannot resolve " + HOSTNAME, err);
                        }
                    });
                    return;
                }
            }
        }
        sendRequest(msg);
    }

    private void sendRequest(String msg) {
        httpClient.get("/txapi/tuling/index?key=" + apiKey + "&question=" + URLEncoder.encode(msg, StandardCharsets.UTF_8))
            .header("Host", HOSTNAME)
            .send((err, resp) -> {
                if (err != null) {
                    Logger.error("request TianxingTuling failed", err);
                    return;
                }
                //noinspection rawtypes
                JSON.Instance inst;
                try {
                    inst = JSON.parse(new String(resp.body().toJavaArray(), StandardCharsets.UTF_8));
                } catch (Exception e) {
                    Logger.error("TianxingTuling response is not json: " + resp.bodyAsString(), e);
                    return;
                }
                Logger.info("TianxingTuling return " + inst.stringify());
                try {
                    JSON.Object o = (JSON.Object) inst;
                    if (o.getInt("code") != 200) {
                        Logger.error("TianxingTuling response code is not 200");
                        return;
                    }
                    var arr = o.getArray("newslist");
                    String[] ret = new String[arr.length()];
                    for (int i = 0; i < ret.length; ++i) {
                        ret[i] = arr.getObject(i).getString("reply");
                    }
                    sendMessage(ret);
                } catch (RuntimeException e) {
                    Logger.error("TianxingTuling response is not valid: " + resp.bodyAsString(), e);
                    //noinspection UnnecessaryReturnStatement
                    return;
                }
            });
    }
}
