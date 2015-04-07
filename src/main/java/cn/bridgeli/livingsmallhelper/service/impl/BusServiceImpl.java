package cn.bridgeli.livingsmallhelper.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.bridgeli.livingsmallhelper.bus.entity.Bus;
import cn.bridgeli.livingsmallhelper.bus.entity.BusInfo;
import cn.bridgeli.livingsmallhelper.bus.entity.Line;
import cn.bridgeli.livingsmallhelper.bus.entity.LineInfo;
import cn.bridgeli.livingsmallhelper.bus.entity.Segment;
import cn.bridgeli.livingsmallhelper.service.KeyService;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

import com.google.gson.Gson;

public class BusServiceImpl implements KeyService {

    private static final Logger LOG = LoggerFactory.getLogger(BusServiceImpl.class);

    @Override
    public String excute(String busValue) {
        String bus = "";
        String[] values = busValue.split("@");
        String[] busKey = values[1].split("至");
        if (null != busKey && 2 == busKey.length) {
            bus = transferSearch(values[0], busKey[0], busKey[1]);
        } else {
            bus = lineSearch(values[0], values[1]);
        }
        return bus;
    }

    private String transferSearch(String city, String start_addr, String end_addr) {
        String requestUrl = "http://openapi.aibang.com/bus/transfer?app_key=2e98b4c85c1311341c0b34988588e5a5&city=CITY&start_addr=START_ADDR&end_addr=END_ADDR&alt=json";
        try {
            city = URLEncoder.encode(city, "UTF-8");
            start_addr = URLEncoder.encode(start_addr, "UTF-8");
            end_addr = URLEncoder.encode(end_addr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestUrl = requestUrl.replace("CITY", city).replace("START_ADDR", start_addr).replace("END_ADDR", end_addr);
        String jsonObject = UrlUtil.httpRequest(requestUrl);
        StringBuffer busInfos = new StringBuffer();
        Gson gson = new Gson();
        BusInfo busInfo = gson.fromJson(jsonObject, BusInfo.class);
        for (Bus bus : busInfo.getBuses().getBus()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("距离： " + bus.getDist() + "米\n");
            stringBuffer.append("时间： " + bus.getTime() + "分钟\n");
            for (Segment segment : bus.getSegments().getSegment()) {
                stringBuffer.append("线路名： " + segment.getLine_name() + "\n");
                stringBuffer.append("起始站： " + segment.getStart_stat() + "\n");
                stringBuffer.append("终点站： " + segment.getEnd_stat() + "\n");
                stringBuffer.append("途经站： " + segment.getStats() + "\n\n");
            }
            busInfos.append(stringBuffer);
        }
        return busInfos.toString();
    }

    private String lineSearch(String city, String q) {
        String requestUrl = "http://openapi.aibang.com/bus/lines?app_key=2e98b4c85c1311341c0b34988588e5a5&city=CITY&q=Q&alt=json";
        try {
            city = URLEncoder.encode(city, "UTF-8");
            q = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("UnsupportedEncodingException", e);
        }
        requestUrl = requestUrl.replace("CITY", city).replace("Q", q);
        String jsonObject = UrlUtil.httpRequest(requestUrl);
        StringBuffer busInfos = new StringBuffer();
        Gson gson = new Gson();
        LineInfo lineInfo = gson.fromJson(jsonObject, LineInfo.class);
        for (Line line : lineInfo.getLines().getLine()) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("线路名：" + line.getName() + "\n");
            stringBuffer.append("线路信息： " + line.getInfo() + "\n");
            stringBuffer.append("途经站： " + line.getStats() + "\n\n");
            busInfos.append(stringBuffer);
        }
        return busInfos.toString();
    }

    @Override
    public String getUsage() {
        return WeiXinUtil.getBusUsage();
    }

}
