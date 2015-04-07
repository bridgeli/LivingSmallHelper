package cn.bridgeli.livingsmallhelper.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cn.bridgeli.livingsmallhelper.entity.BaiduPlace;
import cn.bridgeli.livingsmallhelper.entity.UserLocation;
import cn.bridgeli.livingsmallhelper.message.resp.Article;

public class BaiduMapUtil {

    public static void convertCoords(UserLocation userLocation) {
        String convertUrl = "http://api.map.baidu.com/geoconv/v1/?coords=LNG,LAT&from=3&ak=Nbx4Q9PtGhrRgkGdlvbgLaZm";
        convertUrl = convertUrl.replace("LNG", userLocation.getLng()).replace("LAT", userLocation.getLat());

        String baiduLocation = UrlUtil.httpRequest(convertUrl);

        JSONObject jsonObject = JSONObject.fromObject(baiduLocation);
        JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
        userLocation.setBd09_lng(result.getString("x"));
        userLocation.setBd09_lat(result.getString("y"));
    }

    public static List<BaiduPlace> searchPlace(String poi, String bd09_lng, String bd09_lat) {
        String requestUrl = "http://api.map.baidu.com/place/v2/search?&query=POI&location=BD09_LAT,BD09_LNG&radius=2000&output=xml&scope=2&page_size=10&ak=Nbx4Q9PtGhrRgkGdlvbgLaZm";
        poi = UrlUtil.urlEncodeUTF8(poi);
        requestUrl = requestUrl.replace("POI", poi).replace("BD09_LAT", bd09_lat).replace("BD09_LNG", bd09_lng);
        String respXml = UrlUtil.httpRequest(requestUrl);
        List<BaiduPlace> baiduPlaces = parsePlaceXml(respXml);
        return baiduPlaces;
    }

    @SuppressWarnings("unchecked")
    private static List<BaiduPlace> parsePlaceXml(String respXml) {
        List<BaiduPlace> baiduPlaces = null;
        try {
            Document document = DocumentHelper.parseText(respXml);
            Element root = document.getRootElement();
            Element resultsElement = root.element("results");
            List<Element> resultElements = resultsElement.elements("result");
            if (null != resultElements && resultElements.size() > 0) {
                baiduPlaces = new ArrayList<BaiduPlace>();
                Element nameElement = null;
                Element addressElement = null;
                Element locationElement = null;
                Element telephoneElement = null;
                Element detainInfoElement = null;
                Element distanceElement = null;
                for (Element element : resultElements) {
                    nameElement = element.element("name");
                    addressElement = element.element("address");
                    locationElement = element.element("location");
                    telephoneElement = element.element("telephone");
                    detainInfoElement = element.element("detail_info");

                    BaiduPlace baiduPlace = new BaiduPlace();
                    baiduPlace.setName(nameElement.getText());
                    baiduPlace.setAddress(addressElement.getText());
                    baiduPlace.setLng(locationElement.element("lng").getText());
                    baiduPlace.setLat(locationElement.element("lat").getText());
                    if (null != telephoneElement) {
                        baiduPlace.setTelephone(telephoneElement.getText());
                    }
                    if (null != detainInfoElement) {
                        distanceElement = detainInfoElement.element("distance");
                        if (null != distanceElement) {
                            baiduPlace.setDistance(Integer.parseInt(distanceElement.getText()));
                        }
                    }
                    baiduPlaces.add(baiduPlace);
                }
                Collections.sort(baiduPlaces);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return baiduPlaces;
    }

    public static List<Article> markArticles(List<BaiduPlace> baiduPlaces, String bd09_lng, String bd09_lat) {
        String basePath = "http://livingsmallhelper.sinaapp.com/";
        List<Article> articles = new ArrayList<Article>();
        BaiduPlace baiduPlace = null;
        for (int i = 0; i < baiduPlaces.size(); i++) {
            baiduPlace = baiduPlaces.get(i);
            Article article = new Article();
            article.setTitle(baiduPlace.getName() + "\n距离约为" + baiduPlace.getDistance() + "米");
            article.setUrl(String.format(basePath + "index.jsp?p1=%s,%s&p2=%s,%s", bd09_lng, bd09_lat, baiduPlace.getLng(), baiduPlace.getLat()));
            if (0 == i) {
                article.setPicUrl(basePath + "image/poisearch.jpg");
            } else {
                article.setPicUrl(basePath + "image/navi.jpg");
            }
            articles.add(article);
        }
        return articles;
    }

}
