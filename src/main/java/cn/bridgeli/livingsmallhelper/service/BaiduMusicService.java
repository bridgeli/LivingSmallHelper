package cn.bridgeli.livingsmallhelper.service;

import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;

public interface BaiduMusicService {
    String searchMusic(String song, String fromUserName, String toUserName, TextMessage textMessage);
}
