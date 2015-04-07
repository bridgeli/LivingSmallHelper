package cn.bridgeli.livingsmallhelper.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.BaiduPlace;
import cn.bridgeli.livingsmallhelper.entity.UserLocation;
import cn.bridgeli.livingsmallhelper.mapper.UserLocationMapper;
import cn.bridgeli.livingsmallhelper.message.resp.Article;
import cn.bridgeli.livingsmallhelper.message.resp.NewsMessage;
import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;
import cn.bridgeli.livingsmallhelper.service.NearSearchService;
import cn.bridgeli.livingsmallhelper.util.BaiduMapUtil;
import cn.bridgeli.livingsmallhelper.util.MessageUtil;

@Service
public class NearSearchServiceImpl implements NearSearchService {

    @Resource
    private UserLocationMapper userLocationMapper;

    @Override
    public String searchPlace(String poi, String fromUserName, String toUserName, TextMessage textMessage) {

        String respMessage = "";

        UserLocation userLocation = userLocationMapper.getLastLocation(fromUserName);
        String bd09_lng = userLocation.getBd09_lng();
        String bd09_lat = userLocation.getBd09_lat();
        List<BaiduPlace> baiduPlaces = BaiduMapUtil.searchPlace(poi, bd09_lng, bd09_lat);
        if (null == baiduPlaces || baiduPlaces.isEmpty()) {
            textMessage.setContent("很抱歉，在您的附近未搜到和" + poi + "相关的地方，请换个关键词，由此给你带来的不便，请您谅解！");
            respMessage = MessageUtil.textMessageToXml(textMessage);
        } else {
            List<Article> articles = BaiduMapUtil.markArticles(baiduPlaces, bd09_lng, bd09_lat);
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setFromUserName(toUserName);
            newsMessage.setToUserName(fromUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            newsMessage.setArticles(articles);
            newsMessage.setArticleCount(articles.size());

            respMessage = MessageUtil.newsMessageToXml(newsMessage);
        }
        return respMessage;
    }
}
