package cn.bridgeli.livingsmallhelper.service.impl;

import cn.bridgeli.livingsmallhelper.service.KeyService;
import cn.bridgeli.livingsmallhelper.translate.entity.TranslateResult;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

import com.google.gson.Gson;

public class BaiduTranslateServiceImpl implements KeyService {

    @Override
    public String excute(String source) {
        String dst = null;

        // 组装查询地址
        String requestUrl = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id=Nbx4Q9PtGhrRgkGdlvbgLaZm&q={keyWord}&from=auto&to=auto";
        // 对参数q的值进行urlEncode utf-8编码
        requestUrl = requestUrl.replace("{keyWord}", UrlUtil.urlEncodeUTF8(source));

        // 查询并解析结果
        try {
            // 查询并获取返回结果
            String json = UrlUtil.httpRequest(requestUrl);
            // 通过Gson工具将json转换成TranslateResult对象
            TranslateResult translateResult = new Gson().fromJson(json, TranslateResult.class);
            // 取出translateResult中的译文
            dst = translateResult.getTrans_result().get(0).getDst();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == dst)
            dst = "翻译系统异常，请稍候尝试！";
        return dst;
    }

    @Override
    public String getUsage() {
        return WeiXinUtil.getTranslationUsage();
    }

}
