package cn.bridgeli.livingsmallhelper.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import cn.bridgeli.livingsmallhelper.service.KeyService;
import cn.bridgeli.livingsmallhelper.util.UrlUtil;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;
import cn.bridgeli.livingsmallhelper.weather.entity.Index;
import cn.bridgeli.livingsmallhelper.weather.entity.Weather;
import cn.bridgeli.livingsmallhelper.weather.entity.WeatherData;

import com.google.gson.Gson;

public class WeatherServiceImpl implements KeyService {

    @Override
    public String excute(String city) {
        String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location=LOCATION&output=json&ak=Nbx4Q9PtGhrRgkGdlvbgLaZm";
        try {
            city = URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestUrl = requestUrl.replace("LOCATION", city);
        String weatherInfoResult = UrlUtil.httpRequest(requestUrl);
        JSONObject weatherJsonResult = JSONObject.fromObject(weatherInfoResult);
        String weatherResult = weatherJsonResult.getString("results");
        if (null != weatherResult || !"".equals(weatherResult)) {
            Gson gson = new Gson();
            String weatherJson = weatherResult.substring(1, weatherResult.length() - 1);
            Weather weather = gson.fromJson(weatherJson, Weather.class);

            StringBuffer weathers = new StringBuffer();
            weathers.append("当前城市：" + weather.getCurrentCity() + "\t" + "pm25：" + weather.getPm25() + "\n");
            for (Index index : weather.getIndex()) {
                weathers.append(index.getTitle() + "：" + index.getDes() + "\n");
            }
            for (WeatherData weatherData : weather.getWeather_data()) {
                weathers.append("时间：" + weatherData.getDate() + "\n");
                weathers.append("天气：" + weatherData.getWeather() + "，" + weatherData.getWind() + ";");
                weathers.append("温度：" + weatherData.getTemperature() + "\n");
            }
            return weathers.toString();
        }
        return "很抱歉，获取天气预报数据失败，请稍后再试";
    }

    @Override
    public String getUsage() {
        return WeiXinUtil.getWeatherUsage();
    }

}
