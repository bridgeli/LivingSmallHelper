package cn.bridgeli.livingsmallhelper.service;

import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;

public interface NearSearchService {

    String searchPlace(String poi, String fromUserName, String toUserName, TextMessage textMessage);

}
