package cn.bridgeli.livingsmallhelper.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.bridgeli.livingsmallhelper.entity.UserLocation;
import cn.bridgeli.livingsmallhelper.message.resp.TextMessage;
import cn.bridgeli.livingsmallhelper.service.BaiduMusicService;
import cn.bridgeli.livingsmallhelper.service.CoreService;
import cn.bridgeli.livingsmallhelper.service.EventKeyService;
import cn.bridgeli.livingsmallhelper.service.FaceService;
import cn.bridgeli.livingsmallhelper.service.NearSearchService;
import cn.bridgeli.livingsmallhelper.service.TextService;
import cn.bridgeli.livingsmallhelper.service.UserLocationService;
import cn.bridgeli.livingsmallhelper.util.BaiduMapUtil;
import cn.bridgeli.livingsmallhelper.util.MessageUtil;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

@Service
public class CoreServiceImpl implements CoreService {

    private static final Logger LOG = LoggerFactory.getLogger(CoreServiceImpl.class);

    @Resource
    private TextService textService;
    @Resource
    private FaceService faceService;
    @Resource
    private EventKeyService eventKeyService;
    @Resource
    private BaiduMusicService baiduMusicService;
    @Resource
    private UserLocationService userLocationService;
    @Resource
    private NearSearchService nearSearchService;

    @Override
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        String respContent = "";

        Map<String, String> requestMap = MessageUtil.parseXml(request);

        String fromUserName = requestMap.get("FromUserName");
        String toUserName = requestMap.get("ToUserName");
        String msgType = requestMap.get("MsgType");

        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);

        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            String content = requestMap.get("Content").trim();

            if (content.startsWith("歌曲")) {
                content = content.substring(2);
                if (null == content || "".equals(content)) {
                    respContent = WeiXinUtil.getSongUsage();
                } else {
                    return baiduMusicService.searchMusic(content, fromUserName, toUserName, textMessage);
                }
            } else if (content.startsWith("附近")) {
                content = content.substring(2);
                if (null == content || "".equals(content)) {
                    respContent = WeiXinUtil.getNearSearchUsage();
                } else {
                    return nearSearchService.searchPlace(content, fromUserName, toUserName, textMessage);
                }
            }
            if ("".equals(respContent)) {
                respContent = textService.handleText(content, fromUserName);
            }

        } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            // 取得图片地址
            String picUrl = requestMap.get("PicUrl");
            // 人脸检测
            respContent = faceService.detect(picUrl);
        } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {

            String lng = requestMap.get("Location_Y");
            String lat = requestMap.get("Location_X");

            UserLocation userLocation = new UserLocation();
            userLocation.setOpen_id(fromUserName);
            userLocation.setLng(lng);
            userLocation.setLat(lat);

            BaiduMapUtil.convertCoords(userLocation);

            userLocationService.save(userLocation);

            respContent = WeiXinUtil.getNearSearch();

        } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
            respContent = "您发送的是链接消息！";
        } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
            respContent = "您发送的是音频消息！";
        } else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
            String eventType = requestMap.get("Event");
            if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                respContent = "亲，谢谢您的关注！\n\n生活小助手能为您提供天气、公家、歌曲、聊天、笑话等服务；还能通过自然语言交互方式提供周边搜索（如厕所、ATM、KTV等，精确到米），是您生活娱乐的好帮手！\n\n快快点击菜单体验吧";
            } else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

            } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                String eventKey = requestMap.get("EventKey");
                respContent = eventKeyService.processKey(eventKey);
            }
        }
        int length = 0;
        try {
            length = respContent.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            LOG.warn("UnsupportedEncodingException", e);
        }

        if (length > 2048) {
            respContent = respContent.substring(0, 666) + "...";
        }

        textMessage.setContent(respContent);
        respMessage = MessageUtil.textMessageToXml(textMessage);

        return respMessage;
    }
}
