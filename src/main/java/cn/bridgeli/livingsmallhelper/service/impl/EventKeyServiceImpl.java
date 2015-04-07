package cn.bridgeli.livingsmallhelper.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.service.EventKeyService;
import cn.bridgeli.livingsmallhelper.service.JokeService;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

@Service
public class EventKeyServiceImpl implements EventKeyService {

    @Resource
    private JokeService jokeService;

    @Override
    public String processKey(String eventKey) {
        int key = Integer.parseInt(eventKey);
        String respContent = "";
        switch (key) {
        case 11:
            respContent = WeiXinUtil.getWeatherUsage();
            break;
        case 12:
            respContent = WeiXinUtil.getBusUsage();
            break;
        case 13:
            respContent = WeiXinUtil.getNearSearchUsage();
            break;
        case 15:
            respContent = WeiXinUtil.getMoviceUsage();
            break;
        case 21:
            respContent = WeiXinUtil.getSongUsage();
            break;
        case 22:
            respContent = WeiXinUtil.getGuessNumberUsage();
            break;
        case 24:
            respContent = WeiXinUtil.getFaceUsage();
            break;
        case 25:
            respContent = WeiXinUtil.getChatUsage();
            break;
        case 31:
            respContent = WeiXinUtil.getFriend();
            break;
        case 32:
            respContent = jokeService.getJoke().getContent();
            break;
        case 33:
            respContent = WeiXinUtil.getTranslationUsage();
            break;
        case 34:
            respContent = WeiXinUtil.getTodayInHistory();
            break;

        default:
            break;
        }
        return respContent;
    }

}
