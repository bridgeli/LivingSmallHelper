package cn.bridgeli.livingsmallhelper.accesstoken.service;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bridgeli.livingsmallhelper.util.UrlUtil;

public class AccessTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenService.class);

    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static String getAccessToken(String appid, String appsecret) {
        String accessToken = null;

        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = UrlUtil.httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = jsonObject.getString("access_token");
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                LOG.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }
}
